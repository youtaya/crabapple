package com.talk.demo.setting;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.util.AccountUtils;
import com.talk.demo.util.NetworkUtilities;
import com.talk.demo.util.TalkUtil;

import net.sectorsieteg.avatars.AvatarDrawableFactory;

public class UserActivity extends Activity {
	private static String TAG = "UserActivity";
	private LinearLayout whole, collect, preview, setting;
    private TextView user_name;
    private String account_name;
    private ImageView user_avatar;
    private Uri uri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        user_name = (TextView)findViewById(R.id.my_name);
        
        whole = (LinearLayout)findViewById(R.id.whole_daily);
        whole.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(UserActivity.this, StoreActivity.class));
			}
        	
        });
        collect = (LinearLayout)findViewById(R.id.collect);
        preview = (LinearLayout)findViewById(R.id.preview_daily);
        preview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(UserActivity.this, PreviewActivity.class));
			}
        	
        });
        setting = (LinearLayout)findViewById(R.id.setting);
        setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(UserActivity.this, SettingActivity.class));
			}
        	
        });

        Account accout = AccountUtils.getPasswordAccessibleAccount(this);
        if (accout != null && !TextUtils.isEmpty(accout.name)) {
        	Log.d(TAG,"ccount name: "+accout.name);
        	account_name  = accout.name;
        	user_name.setText(account_name);
        }
        
        user_avatar = (ImageView)findViewById(R.id.user_avatar);
        
        // get avatar from uri
        uri = Uri.parse("file://"+"/sdcard/Demo/"+account_name);
        Bitmap account_avatar = getBitmapFromURI(uri);
        if(null != account_avatar) {
            AvatarDrawableFactory avatarFactory = new AvatarDrawableFactory(getResources());
            Drawable AvatarDrawable = avatarFactory.getRoundedAvatarDrawable(account_avatar);
            user_avatar.setImageDrawable(AvatarDrawable);
        }

        user_avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
		        Intent mIntent = new Intent(UserActivity.this,SelectAvatarActivity.class);
		        Bundle mBundle = new Bundle();
		        mBundle.putString("account", account_name);
		        mIntent.putExtras(mBundle);
				startActivityForResult(mIntent, TalkUtil.REQUEST_SELECT_AVATAR);
			}
        	
        });
    }
    
    private Bitmap getBitmapFromURI(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
   
   
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "got the return :"+requestCode+" :"+resultCode);
        switch(requestCode) {
            case TalkUtil.REQUEST_SELECT_AVATAR:
                if (resultCode == RESULT_OK) {
                	Bitmap imageBitmap = getBitmapFromURI(uri);
                    //TODO: upload to server
                    AvatarDrawableFactory avatarDrawableFactory = new AvatarDrawableFactory(getResources());
                    Drawable roundedAvatarDrawable = avatarDrawableFactory.getRoundedAvatarDrawable(imageBitmap);
                    user_avatar.setImageDrawable(roundedAvatarDrawable);
                }
                break;
                
        }
    }
}