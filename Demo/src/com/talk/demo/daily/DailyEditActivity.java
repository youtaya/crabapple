package com.talk.demo.daily;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.talk.demo.share.FriendsActivity;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.RawRecord;
import com.talk.demo.util.TalkUtil;

import org.apache.http.ParseException;
import org.json.JSONException;

import java.io.IOException;

public class DailyEditActivity extends Activity {
	private static String TAG = "DailyEditActivity";
	private EditText edit_content;
	private String pre_content;
	private TextView tv;
	private ImageView content_bg;
	//private ImageView content_bg_dec;
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
		//content_bg_dec = (ImageView) findViewById(R.id.content_bg_dec);

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
		Intent mIntent = new Intent(this, FriendsActivity.class);
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
			tr.setContentType(TalkUtil.MEDIA_TYPE_TEXT);
			rMgr.addRecord(tr);
			;
		}

		// share to friend
		// shareToFriend(tr, friend);
		new ShareRecordTask().execute();
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
		}
	}
}
