package com.talk.demo.setting;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.talk.demo.R;
import com.talk.demo.account.AccountConstants;
import com.talk.demo.account.AppEnterActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity {
	private static String TAG = "SettingActivity";
	private UITableView tableView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        
        tableView = (UITableView) findViewById(R.id.tableView);        
        createList();        
        Log.d(TAG, "total items: " + tableView.getCount());        
        tableView.commit();
    }
    
    private void removeAccount() {
    	AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType(AccountConstants.ACCOUNT_TYPE);
        for (int index = 0; index < accounts.length; index++) {
        	manager.removeAccount(accounts[index], null, null);
        }
    }
    
    private void createList() {
    	CustomClickListener listener = new CustomClickListener();
    	tableView.setClickListener(listener);
    	tableView.addBasicItem("分享", "邀请朋友");
    	tableView.addBasicItem("关于西窗话", "版本信息");
    	tableView.addBasicItem("注销");
    }
    
	private void mySetOnClick(View v, AppInfo info, String argu ) {
		try{                
		    ComponentName toActivity = new ComponentName(info.getAppPkgName(),info.getAppLauncherClassName());

		    Intent intent = new Intent();
		    intent.setComponent(toActivity);
		    intent.setAction("android.intent.action.SEND");
		    intent.putExtra(Intent.EXTRA_TEXT, argu);
		    SettingActivity.this.startActivity(intent);
		}catch(Exception e){
		    Log.v(TAG,"------>"+e.toString());
		}

	}
	
	/**
	 * 分享对话框
	 * 
	 * 显示4个排序好的分享app，提示是否安装
	 * 
	 * 需要分享的节目名称
	 * 
	 * @param shareContent
	 */
    Dialog dialog;
	public void showShare_5(final String shareContent) {
		dialog = new Dialog(this, R.style.Dialog);
		dialog.setContentView(R.layout.dialog_factory_share);
		final TextView tv_sina_weibo = (TextView) dialog.findViewById(R.id.tv_sina);
		final TextView tv_tecent_weibo = (TextView) dialog.findViewById(R.id.tv_QQ_weibo);
		final TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
		final TextView tv_weixin = (TextView) dialog.findViewById(R.id.tv_weixin);

		final List<AppInfo> shareAppInfos = getShareAppList();

		for (int i = 0; i < shareAppInfos.size(); i++) {
			if ("com.sina.weibo".equals(shareAppInfos.get(i).getAppPkgName())) {
				Drawable add = this.getResources().getDrawable(R.drawable.weibo);
				add.setBounds(0, 0, add.getMinimumWidth(), add.getMinimumHeight());
				tv_sina_weibo.setCompoundDrawables(null, add, null, null);// 设置Top图标
				tv_sina_weibo.setTag(i);
			}
			if ("com.tencent.WBlog".equals(shareAppInfos.get(i).getAppPkgName())) {
				Drawable add = this.getResources().getDrawable(R.drawable.pyq);
				add.setBounds(0, 0, add.getMinimumWidth(), add.getMinimumHeight());
				tv_tecent_weibo.setCompoundDrawables(null, add, null, null);// 设置Top图标
				tv_tecent_weibo.setTag(i);
			}
			if ("com.android.mms".equals(shareAppInfos.get(i).getAppPkgName())) {
				Drawable add = this.getResources().getDrawable(R.drawable.twitter);
				add.setBounds(0, 0, add.getMinimumWidth(), add.getMinimumHeight());
				tv_message.setCompoundDrawables(null, add, null, null);// 设置Top图标
				tv_message.setTag(i);
			}
			if ("com.tencent.mm".equals(shareAppInfos.get(i).getAppPkgName())) {
				Drawable add = this.getResources().getDrawable(R.drawable.wx);
				add.setBounds(0, 0, add.getMinimumWidth(), add.getMinimumHeight());
				tv_weixin.setCompoundDrawables(null, add, null, null);// 设置Top图标
				tv_weixin.setTag(i);
			}
		}
		// 直接写死4个分享的应用信息
		final AppInfo app_sina = new AppInfo("com.sina.weibo", "com.sina.weibo.EditActivity");
		final AppInfo app_tx_weibo = new AppInfo("com.tencent.WBlog",
				"com.tencent.WBlog.intentproxy.TencentWeiboIntent");
		final AppInfo app_message = new AppInfo("com.android.mms", "com.android.mms.ui.ComposeMessageActivity");
		final AppInfo app_weixin = new AppInfo("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");

		/*
		final String share = this.getString(R.string.dialog_share1) + shareContent
				+ this.getString(R.string.dialog_share2);
		*/
			
		final String share = shareContent;
		
		tv_sina_weibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mySetOnClick(tv_sina_weibo, app_sina, share);
			}
		});
		tv_tecent_weibo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mySetOnClick(tv_tecent_weibo, app_tx_weibo, share);
			}
		});
		tv_message.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mySetOnClick(tv_message, app_message, share);
			}
		});
		tv_weixin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mySetOnClick(tv_weixin, app_weixin, share);
			}
		});
		
		dialog.show();
	}
	

    /**
     * 查询所有支持分享的应用信息
     * 
     * @param context
     * @return
     */
    private List<ResolveInfo> getShareApps(Context context) {
    	List<ResolveInfo> mApps = new ArrayList<ResolveInfo>();
    	Intent intent = new Intent(Intent.ACTION_SEND, null);
    	intent.addCategory(Intent.CATEGORY_DEFAULT);
    	intent.setType("text/plain");
    	PackageManager pManager = context.getPackageManager();
    	mApps = pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
    	return mApps;
    }

    /**
     * 得到应用列表
     * 
     * @return
     */
    private List<AppInfo> getShareAppList() {
    	List<AppInfo> shareAppInfos = new ArrayList<AppInfo>();
    	PackageManager packageManager = this.getPackageManager();
    	List<ResolveInfo> resolveInfos = getShareApps(this);
    	if (null == resolveInfos) {
    		return null;
    	}
    	else {
    		for (ResolveInfo resolveInfo : resolveInfos) {
    			AppInfo appInfo = new AppInfo();
    			appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);
    			appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
    			appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
    			appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
    			shareAppInfos.add(appInfo);
    		}
    	}
    	return shareAppInfos;
    }
    
    private void callOtherActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
    
    private class CustomClickListener implements ClickListener {

        @Override
        public void onClick(int index) {
            Log.d(TAG, "item clicked: " + index);
            switch(index) {
            case 0:
                showShare_5("I like");
                break;
            case 1:
                callOtherActivity(AboutActivity.class);
                break;
            case 2:
            	removeAccount();
            	callOtherActivity(AppEnterActivity.class);
            	break;
            }
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
    }
}