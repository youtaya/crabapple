package com.talk.demo.intimate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;
import com.talk.demo.util.TalkUtil;

import net.sectorsieteg.avatars.AvatarDrawableFactory;

public class EditIntimateActivity extends Activity {
	private static String TAG = "EditIntimateActivity";
	private int friend_id;
	private String friend_name;
	private TextView tvFriendName;
	private ImageView ivAvatar;
	private TextView tvLinkageTo, tvLinkageFrom;
	private TextView tvNumberTo, tvNumberFrom;
	private TextView tvPressAdd;
	private TextView tvChannel;
	private int numberTo;
	private int oneStep;

	private static final int ONE_STEP = 29;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_intimate);
        
        Bundle bundle = getIntent().getExtras();
        friend_id = bundle.getInt("id");
        friend_name = bundle.getString("friend");
        tvFriendName = (TextView) findViewById(R.id.friend_name);
        tvFriendName.setText(friend_name);

        ivAvatar = (ImageView) findViewById(R.id.friend_avatar);
        
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = false;
        Bitmap avatar = BitmapFactory.decodeResource(this.getResources(), R.drawable.avatar, options);

        AvatarDrawableFactory avatarDrawableFactory = new AvatarDrawableFactory(this.getResources());
        Drawable roundedAvatarDrawable = avatarDrawableFactory.getRoundedAvatarDrawable(avatar);
        ivAvatar.setImageDrawable(roundedAvatarDrawable);
        
        tvLinkageTo = (TextView) findViewById(R.id.linkage_from);
        tvNumberTo = (TextView) findViewById(R.id.number_to);
        numberTo = 61;
        tvNumberTo.setText(String.valueOf(numberTo));
        
        tvLinkageTo.setText("我对"+friend_name+"亲密度");
        tvLinkageFrom = (TextView) findViewById(R.id.linkage_to);
        tvLinkageFrom.setText(friend_name+"对我亲密度");
        tvNumberFrom = (TextView) findViewById(R.id.number_from);
        
        tvPressAdd = (TextView) findViewById(R.id.add_press);
        tvPressAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(EditIntimateActivity.this, AddPressActivity.class);
	            Bundle mBundle = new Bundle();
                mBundle.putInt("id", friend_id);
                newIntent.putExtras(mBundle);
				startActivityForResult(newIntent, ONE_STEP);
			}
        	
        });
        
        tvChannel = (TextView) findViewById(R.id.subject_wall);
        tvChannel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(EditIntimateActivity.this, IntimateChannelActivity.class));
			}
        	
        });
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "got the return :" + requestCode + " :" + resultCode);
		switch (requestCode) {
		case ONE_STEP:
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				oneStep = extras.getInt("STEP");
				
		        numberTo += oneStep;
		        tvNumberTo.setText(String.valueOf(numberTo));
			}
			break;
		}
	}
    
}
