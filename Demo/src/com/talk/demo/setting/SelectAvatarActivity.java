package com.talk.demo.setting;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.talk.demo.R;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.TalkUtil;

import java.io.File;

public class SelectAvatarActivity extends Activity implements OnClickListener{

	private static String TAG = "SelectAvatarActivity";
	private String account_name = null;
	private File tempFile = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_avatar);
        Bundle bundle = getIntent().getExtras();
        account_name = bundle.getString("account");
        tempFile = new File(Environment.getExternalStorageDirectory(),
    			account_name);
        
        Button photoBtn = (Button)findViewById(R.id.change_dialog_take_photo); 
        photoBtn.setOnClickListener(this);          
        Button galleryBtn = (Button)findViewById(R.id.change_dialog_take_gallery); 
        galleryBtn.setOnClickListener(this);
        Button cancelBtn = (Button)findViewById(R.id.change_dialog_take_cancel);
        
        cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
        });

	}
	
	
	public void onClick(View v) {

		switch (v.getId()) {
        case R.id.change_dialog_take_photo:
            Log.d(TAG, "send to photo");
	        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
	        	startActivityForResult(takePictureIntent, TalkUtil.REQUEST_IMAGE_CAPTURE_CROP);
	        }
            break; 
        case R.id.change_dialog_take_gallery:
            Log.d(TAG, "send to gallery");
	        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
	        intent.setType("image/*");
	        startActivityForResult(Intent.createChooser(intent,
	                "Select Picture"), TalkUtil.REQUEST_SELECT_PICTURE);
            break;
        default: 
            break; 
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
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, TalkUtil.REQUEST_PHOTO_CROPPER);
    }
    
	private void uploadAvatarServer(String path) {
		NetworkUtilities.addAvatar(path, account_name);
	}
	
	private class syncAvatarTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... params) {
			uploadAvatarServer(params[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer e) {
		}

		@Override
		protected void onCancelled() {
		}
	}
	
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "got the return :"+requestCode+" :"+resultCode);
        switch(requestCode) {
            case TalkUtil.REQUEST_IMAGE_CAPTURE_CROP:
                if (resultCode == RESULT_OK) {
                	/*
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    String fileName = account_name;
                    TalkUtil.createDirAndSaveFile(imageBitmap, fileName);
                    //TODO: upload to server
                    AvatarDrawableFactory avatarDrawableFactory = new AvatarDrawableFactory(getResources());
                    Drawable roundedAvatarDrawable = avatarDrawableFactory.getRoundedAvatarDrawable(imageBitmap);
                    user_avatar.setImageDrawable(roundedAvatarDrawable);
                    */
                	if(null == tempFile) {
                		tempFile = new File(Environment.getExternalStorageDirectory(),
                				account_name);
                	}
                	startPhotoZoom(Uri.fromFile(tempFile));
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
    		            	Bitmap imageBitmap = bundle.getParcelable("data");
    	                    String fileName = account_name;
    	                    String resultPath = TalkUtil.createDirAndSaveFile(imageBitmap, fileName);
    	                    
    	                    new syncAvatarTask().execute(resultPath);
    	                    
    	                    Intent resIntent = new Intent();
    	                    setResult(RESULT_OK, resIntent);
    	                    finish();
    		            }
            		}
            	}
                break;                
        }
    }
}