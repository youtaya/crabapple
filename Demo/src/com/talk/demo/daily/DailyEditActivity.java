package com.talk.demo.daily;

import android.accounts.Account;
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
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.FloatingActionButton;
import com.talk.demo.R;
import com.talk.demo.core.RecordManager;
import com.talk.demo.persistence.DBManager;
import com.talk.demo.persistence.DialogRecord;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.prewrite.PreWrite;
import com.talk.demo.time.DateInfo;
import com.talk.demo.types.PrvDialog;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.AlarmManagerUtil;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.TalkUtil;

import org.apache.http.ParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DailyEditActivity extends Activity {
	private static String TAG = "DailyEditActivity";
	private EditText edit_content;
	private String pre_content;
	private TextView tv, head;
	private ImageView content_bg;
	private ImageView add_photo;
	private FloatingActionButton btn_accept;
	
	private String fileName = null;
	private DBManager mgr;
	private RecordManager rMgr;
	private String friend = null;
	private TimeRecord tr = null;
	private DialogRecord dr = null;
	
	private String ownUser;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*set it to be no title*/ 
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		/*set it to be full screen*/ 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.activity_dailyedit);
		Bundle bundle = getIntent().getExtras();
		if(bundle != null)
			pre_content = bundle.getString("precontent");
		edit_content = (EditText) findViewById(R.id.edit_content);
		
		tv = (TextView) findViewById(R.id.daily_title);
		head = (TextView) findViewById(R.id.daily_head);
		PreWrite pw = new PreWrite(this);
		// when need to change week day, am/pm
		String when = pw.getWhen();
		DateInfo dateInfo = new DateInfo(when);
		dateInfo.parseCreateTime();
		String current = dateInfo.getTimeHead();
		String where = pw.getWhere();
		
		head.setText(current+"\n"+where);
		if(pre_content != null) {
			tv.setVisibility(View.VISIBLE);
			tv.setText(pre_content);
		}
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
		
		btn_accept = (FloatingActionButton) findViewById(R.id.btn_accept);
		btn_accept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				send_dialog();
			}
			
		});

		mgr = new DBManager(this);
		rMgr = new RecordManager(mgr, this);
		
        Account accout = AccountUtils.getPasswordAccessibleAccount(this);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        	ownUser = accout.name;
        }
		/*
		new Thread(new Runnable() {

			@Override
			public void run() {
				applyBlur();
			}
		}).start();
		*/
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

	private String shareToFriend(DialogRecord dialog, String name) {
		String result = "ok";

        //TODO 
		PrvDialog raw = PrvDialog.create(ownUser, ownUser, friend, friend, dialog.getPrvDialog().getContent(),
				dialog.getPrvDialog().getCreateDate(), dialog.getPrvDialog().getCreateTime(), dialog.getPrvDialog().getContentType(), null,
				null, 0, 11, 12, -1, 1);
		try {
			NetworkUtilities.shareRecord(raw, ownUser, name);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	private void uploadPhotoServer(String file) {
		NetworkUtilities.uploadPhoto(file);
	}
	
	private class SyncPhotoTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			uploadPhotoServer("/sdcard/Demo/"+fileName);
			return 0;
		}

		@Override
		protected void onPostExecute(Integer e) {
		}

		@Override
		protected void onCancelled() {
		}
	}
	
	
	private void confirmDone() {
		// save to db
		String content = edit_content.getText().toString();
		// Do nothing if content is empty
		if (content.length() > 0) {
			tr = new TimeRecord(content);
			if(fileName != null) {
				//tr = new TimeRecord("/sdcard/Demo/"+fileName);
				tr.getTimeRecord().setPhoto(fileName);
				new SyncPhotoTask().execute();
				tr.getTimeRecord().setContentType(TalkUtil.MEDIA_TYPE_PHOTO_TEXT);
			} else {
				tr.getTimeRecord().setContentType(TalkUtil.MEDIA_TYPE_TEXT);
			}
			
			if(pre_content != null) {
				tr.getTimeRecord().setTitle(pre_content);
			}
			tr.getTimeRecord().setHandle(ownUser);
			tr.getTimeRecord().setDeleted(0);
			tr.getTimeRecord().setDirty(1);
			rMgr.addRecord(tr);
		}

	}
	
	private void confirmDone(String target) {
		friend = target;
		Log.d(TAG, "friend is : "+friend);
		//TODO: get time according to friend intimate
		// for test: set 10 as default wait time
		int wait_x_time = 20;
		
	    // save to db
        String content = edit_content.getText().toString();
        // Do nothing if content is empty
        if (content.length() > 0) {
            dr = new DialogRecord(content);
            
            if(fileName != null) {
                //tr = new TimeRecord("/sdcard/Demo/"+fileName);
                dr.getPrvDialog().setPhoto(fileName);
                new SyncPhotoTask().execute();
                dr.getPrvDialog().setContentType(TalkUtil.MEDIA_TYPE_PHOTO_TEXT);
            } else {
                dr.getPrvDialog().setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            }
            		
            //TODO: add msg_interval_time and msg_done_time
 
            dr.getPrvDialog().setSendInterval(wait_x_time);
            //current+interval
            Calendar calendar = Calendar.getInstance();

            int doneSeconds = calendar.get(Calendar.SECOND) + wait_x_time;
            calendar.set(Calendar.SECOND, doneSeconds);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date date = new Date();
            String done_time = DateFormat.format("yyyyMMddHHmmss", calendar.getTime()).toString();
            String test_time = formatter.format(date);
            Log.d(TAG , "done time "+done_time+" test time "+test_time);
            dr.getPrvDialog().setSendDoneTime(done_time);

            // add sender object
            dr.getPrvDialog().setSender(ownUser);
            // save link object
            dr.getPrvDialog().setLink(target);
            rMgr.addDialog(dr);
        }
		//TODO: start Alarm Manager to send message after wait time
        AlarmManagerUtil.sendUpdateBroadcast(this, wait_x_time*1000);
        
		if(friend != null) {
			new ShareRecordTask().execute();
		}

	}
	

	
    private void confirmToTag(String tag) {
        Log.d(TAG, "tag is : "+tag);
        
        // save to db
        String content = edit_content.getText().toString();
        // Do nothing if content is empty
        if (content.length() > 0) {
            tr = new TimeRecord(content);

            if (fileName != null) {
                // tr = new TimeRecord("/sdcard/Demo/"+fileName);
                tr.getTimeRecord().setPhoto(fileName);
                new SyncPhotoTask().execute();
                tr.getTimeRecord().setContentType(TalkUtil.MEDIA_TYPE_PHOTO_TEXT);
            } else {
                tr.getTimeRecord().setContentType(TalkUtil.MEDIA_TYPE_TEXT);
            }
            
			if(pre_content != null) {
				tr.getTimeRecord().setTitle(pre_content);
			}
			
            // save tag object
            tr.getTimeRecord().setTag(tag);
			tr.getTimeRecord().setDeleted(0);
			tr.getTimeRecord().setDirty(1);
            rMgr.addRecord(tr);
        }
        //startActivity(new Intent(this, TagActivity.class));
        //finish();
    }
	
	private void send_dialog() {
		startActivityForResult(new Intent(this,SelectPopupActivity.class),TalkUtil.REQUEST_SEND_TO_WHAT);
	}
	  
	@Override
	public void finish() {  
		super.finish();  
		//关闭窗体动画显示
		this.overridePendingTransition(0, R.anim.out_to_bottom);  
	}
	
	private class ShareRecordTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			return shareToFriend(dr, friend);
		}

		@Override
		protected void onPostExecute(final String authToken) {
		}

		@Override
		protected void onCancelled() {
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
		                //applyBlur();
		            }
        		}
        	}
            break;
        case TalkUtil.REQUEST_SEND_TO_WHAT:
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                int to_what = extras.getInt("TO_WHAT");
                Log.d(TAG, "result to what : "+to_what);
                switch (to_what) {
                    case 1:
                        confirmDone();
                        break;
                    case 2:
                    case 3:
                    	String target = extras.getString("TARGET");
                    	Log.d(TAG, "target is : "+target);
                    	confirmDone(target);
                        break;
                    case 4:
                        String tag = extras.getString("TAG");
                        Log.d(TAG, "tag is : "+tag);
                        confirmToTag(tag);
                        break;                        
                }
                
                finish();
                
            }
            break;            
		}
	}
}
