package com.talk.demo;

import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;

import com.talk.demo.intimate.FindDSourceFriendsActivity;
import com.talk.demo.setting.UserActivity;

public class PlusActionProvider extends ActionProvider {

	private Context context;

	public PlusActionProvider(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public View onCreateActionView() {
		return null;
	}

	@Override
	public void onPrepareSubMenu(SubMenu subMenu) {
		subMenu.clear();
		subMenu.add(context.getString(R.string.user))
				.setIcon(R.drawable.newbie)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
					    callOtherActivity(UserActivity.class);
						return true;
					}
				});
		subMenu.add(context.getString(R.string.add_intimate))
				.setIcon(R.drawable.ic_popup_reminder)
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
					    callOtherActivity(FindDSourceFriendsActivity.class);
						return false;
					}
				});
	}
	
    private void callOtherActivity(Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }
    

	@Override
	public boolean hasSubMenu() {
		return true;
	}

}