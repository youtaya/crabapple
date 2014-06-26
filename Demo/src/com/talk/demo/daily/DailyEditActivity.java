package com.talk.demo.daily;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.share.AddFriendsActivity;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawRecord;
import com.talk.demo.util.TalkUtil;

import org.apache.http.ParseException;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyEditActivity extends Activity {
	private static String TAG = "DailyEditActivity";
	private EditText edit_content;
	private String pre_content;
	private TextView tv;
	private ImageView content_bg;
	private ImageView add_photo;
	
	private String fileName = null;
	private DBManager mgr;
	private RecordManager rMgr;
	private static final int GET_FRIEND = 101;
	private String friend = null;
	private TimeRecord tr = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_dailyedit);
		Bundle bundle = getIntent().getExtras();
		pre_content = bundle.getString("precontent");
		edit_content = (EditText) findViewById(R.id.edit_content);
		edit_content.setText(pre_content);
		edit_content.setSelection(pre_content.length());
		content_bg = (ImageView) findViewById(R.id.content_bg);
		add_photo = (ImageView) findViewById(R.id.add_photo);
		
		add_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		        intent.setType("image/*");
		        /*
		        intent.putExtra("crop", "true");
		        intent.putExtra("aspectX", 2);
		        intent.putExtra("aspectY", 1);
		        intent.putExtra("outputX", 600);
		        intent.putExtra("outputY", 300);
		        intent.putExtra("scale", true);
		        intent.putExtra("return-data", false);
		        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		        intent.putExtra("noFaceDetection", true); // no face detection
		        */
		        startActivityForResult(Intent.createChooser(intent,
		                "Select Picture"), TalkUtil.REQUEST_SELECT_PICTURE);
			}
			
		});

		tv = (TextView) findViewById(R.id.bluestone);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// call friends, and choose one
				startFriendActivity();
			}

		});
		mgr = new DBManager(this);
		rMgr = new RecordManager(mgr);

		new Thread(new Runnable() {

			@Override
			public void run() {
				applyBlur();
			}
		}).start();

	}

	private void applyBlur() {
		content_bg.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						content_bg.getViewTreeObserver()
								.removeOnPreDrawListener(this);
						content_bg.buildDrawingCache();

						Bitmap bmp = content_bg.getDrawingCache();
						blur(bmp, edit_content);
						return true;
					}
				});
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private void blur(Bitmap bkg, View view) {
		long startMs = System.currentTimeMillis();

		float radius = 20;

		Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth()),
				(int) (view.getMeasuredHeight()), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(overlay);

		canvas.translate(-view.getLeft(), -view.getTop());
		canvas.drawBitmap(bkg, 0, 0, null);

		RenderScript rs = RenderScript.create(this);

		Allocation overlayAlloc = Allocation.createFromBitmap(rs, overlay);

		ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs,
				overlayAlloc.getElement());

		blur.setInput(overlayAlloc);

		blur.setRadius(radius);

		blur.forEach(overlayAlloc);

		overlayAlloc.copyTo(overlay);

		view.setBackground(new BitmapDrawable(getResources(), overlay));

		rs.destroy();
	}

	private void startFriendActivity() {
		Intent mIntent = new Intent(this, AddFriendsActivity.class);
		this.startActivityForResult(mIntent, GET_FRIEND);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.confirm_actions, menu);
		return true;
	}

	private String shareToFriend(TimeRecord time, String name) {
		String result = "ok";
		RawRecord raw = RawRecord.create("jinxp", "abc", "test", time.content,
				time.calc_date, time.create_time, time.content_type, null,
				null, false, 11, 12, -1, true);
		try {
			NetworkUtilities.shareRecord(raw, name);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	private void confirmDone() {
		// save to db
		String content = edit_content.getText().toString();
		// Do nothing if content is empty
		if (content.length() > 0) {
			tr = new TimeRecord(content);
			if(fileName != null) {
				//tr = new TimeRecord("/sdcard/Demo/"+fileName);
				tr.setPhoto("/sdcard/Demo/"+fileName);
				tr.setContentType(TalkUtil.MEDIA_TYPE_PHOTO_TEXT);
			} else {
				tr.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
			}
			rMgr.addRecord(tr);
		}

		// share to friend
		// shareToFriend(tr, friend);
		if(friend != null) {
			new ShareRecordTask().execute();
		}
		// goto main activity
		/*
		 * Intent mIntent = new Intent(); mIntent.setClass(this,
		 * MainActivity.class); startActivity(mIntent);
		 */
		finish();
	}

	private class ShareRecordTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			return shareToFriend(tr, friend);
		}

		@Override
		protected void onPostExecute(final String authToken) {
		}

		@Override
		protected void onCancelled() {
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_confirm:
			confirmDone();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
   
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, TalkUtil.REQUEST_PHOTO_CROPPER);
    }
    
    
    private void createDirAndSaveFile(Bitmap imageToSave, String fileName) {
        File direct = new File(Environment.getExternalStorageDirectory() + "/Demo");
        
        if(!direct.exists()) {
            File fileDirectory = new File("/sdcard/Demo/");
            fileDirectory.mkdirs();
        }
        
        File file = new File(new File("/sdcard/Demo/"), fileName);
        
        if(file.exists())
            file.delete();
        
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private String getTimeAsFileName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss"); 
        Date date = new Date();
        return dateFormat.format(date);
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "got the return :" + requestCode + " :" + resultCode);
		switch (requestCode) {
		case GET_FRIEND:
			if (resultCode == RESULT_OK) {
				String name = data.getStringExtra("friend_name").toString();
				tv.setText("Choose Friends: " + name);
				friend = name;
			}
			break;
        case TalkUtil.REQUEST_SELECT_PICTURE:
            if (resultCode == RESULT_OK) {
            	if(data != null ) {
            		Uri selectedImageUri = data.getData();
            		startPhotoZoom(selectedImageUri);
            	}
            }
            break;
        case TalkUtil.REQUEST_PHOTO_CROPPER:// 返回的结果
        	if (resultCode == RESULT_OK) {
        		if(data != null ) {
		           	Bundle bundle = data.getExtras();
		            if (bundle != null) {
		                Bitmap photo = bundle.getParcelable("data");
		                
		               	fileName = getTimeAsFileName();
		                createDirAndSaveFile(photo, fileName);

		                content_bg.setImageBitmap(photo);
		                //apply blur
		                applyBlur();
		            }
        		}
        	}
            break;            
		}
	}
}
