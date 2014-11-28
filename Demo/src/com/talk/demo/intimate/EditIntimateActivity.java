package com.talk.demo.intimate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.talk.demo.R;

import net.sectorsieteg.avatars.AvatarDrawableFactory;

public class EditIntimateActivity extends Activity {
	private String friend_name;
	private TextView tvFriendName;
	private ImageView ivAvatar;
	private TextView tvLinkageTo, tvLinkageFrom;
	private TextView tvNumberTo, tvNumberFrom;
	private TextView tvPressAdd;
	private TextView tvChannel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_intimate);
        
        Bundle bundle = getIntent().getExtras();
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
        tvNumberTo = (TextView) findViewById(R.id.number_from);
        tvLinkageTo.setText("我对"+friend_name+"亲密度");
        tvLinkageFrom = (TextView) findViewById(R.id.linkage_to);
        tvLinkageFrom.setText(friend_name+"对我亲密度");
        tvNumberFrom = (TextView) findViewById(R.id.number_to);
        
        tvPressAdd = (TextView) findViewById(R.id.add_press);
        tvPressAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent newIntent = new Intent(EditIntimateActivity.this, AddPressActivity.class);
				startActivityForResult(newIntent, 0);
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
}
