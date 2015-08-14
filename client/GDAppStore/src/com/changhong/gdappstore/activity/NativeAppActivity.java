package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;

import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.post.PostSetting;
import com.post.view.base.BasePosterLayoutView;
import com.post.view.listener.Listener.IItemOnClickListener;

public class NativeAppActivity extends PostActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		List<Object> apps = getApp(context);
		postSetting.setPosttype(PostSetting.TYPE_NATIVEAPP);
		postSetting.setOnItemClickListener(nativeappPostItemOnclickListener);
		postSetting.setOnItemLongClickListener(null);
		if (apps != null) {
//			postView.initData(apps, apps.size());
			postView.refreshAllData(apps, postSetting, apps.size());
		}
	}

	public List<Object> getApp(Context context) {
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		if (context == null || context.getPackageManager() == null) {
			return null;
		}
		// http://blog.csdn.net/qinjuning/article/details/6867806
		List<Object> nativeApps = new ArrayList<Object>();
		List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if ((packageInfo.applicationInfo.flags & packageInfo.applicationInfo.FLAG_SYSTEM) <= 0) {
				// 非系统预装的应用程序

				NativeApp tmpInfo = new NativeApp();
				tmpInfo.appname = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
				tmpInfo.appPackage = packageInfo.packageName;
				tmpInfo.versionName = packageInfo.versionName;
				tmpInfo.versionCode = packageInfo.versionCode;
				tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(getPackageManager());
				nativeApps.add(tmpInfo);
			}
		}
		return nativeApps;
	}

	/** 海报墙点击监听 **/
	private IItemOnClickListener nativeappPostItemOnclickListener = new IItemOnClickListener() {

		@Override
		public void itemOnClick(BasePosterLayoutView arg0, View arg1, int arg2) {
			NativeApp tmpInfo = (NativeApp) arg1.getTag();
		}
	};
}
