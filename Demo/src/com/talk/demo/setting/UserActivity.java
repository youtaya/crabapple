package com.talk.demo.setting;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.persistence.TimeRecord;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.TalkUtil;

import net.sectorsieteg.avatars.AvatarDrawableFactory;

public class UserActivity extends Activity {
	private static String TAG = "UserActivity";
	private TextView tv_rich, user_name, tv_luck;
    // Get rich values
    private RichPresent rp;
    
    private String account_name;
    private ImageView user_avatar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        
        tv_rich = (TextView)findViewById(R.id.rich_number);
        user_name = (TextView)findViewById(R.id.my_name);
        tv_luck = (TextView)findViewById(R.id.my_luck_day);
        // Get values
        rp = RichPresent.getInstance(this);
        tv_rich.setText(String.valueOf(rp.getRich()));
        Account accout = AccountUtils.getPasswordAccessibleAccount(this);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        	account_name  = accout.name;
        	user_name.setText(account_name);
        }
        
        user_avatar = (ImageView)findViewById(R.id.user_avatar);
        
        // get avatar from uri
        Uri uri = Uri.parse("file://"+"/sdcard/Demo/"+account_name);
        Bitmap account_avatar = getBitmapFromURI(uri);
        if(null != account_avatar) {
            AvatarDrawableFactory avatarFactory = new AvatarDrawableFactory(getResources());
            Drawable AvatarDrawable = avatarFactory.getRoundedAvatarDrawable(account_avatar);
            user_avatar.setImageDrawable(AvatarDrawable);
        }

        user_avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
		        	startActivityForResult(takePictureIntent, TalkUtil.REQUEST_IMAGE_CAPTURE);
		        }
			}
        	
        });
    }
    
    private Bitmap getBitmapFromURI(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        tv_rich.setText(String.valueOf(rp.getRich()));
        // Get luck day
        SharedPreferences sPreferences = getSharedPreferences("luck_day", Context.MODE_PRIVATE);
        int sMonth = sPreferences.getInt("Month", 0);
        int sDay = sPreferences.getInt("Day", 0);
        tv_luck.setText(sMonth+" 月 "+sDay+" 日 ");
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "got the return :"+requestCode+" :"+resultCode);
        switch(requestCode) {
            case TalkUtil.REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    String fileName = account_name;
                    TalkUtil.createDirAndSaveFile(imageBitmap, fileName);
                    
                    AvatarDrawableFactory avatarDrawableFactory = new AvatarDrawableFactory(getResources());
                    Drawable roundedAvatarDrawable = avatarDrawableFactory.getRoundedAvatarDrawable(imageBitmap);
                    user_avatar.setImageDrawable(roundedAvatarDrawable);
                }
                break;
        }
    }
}