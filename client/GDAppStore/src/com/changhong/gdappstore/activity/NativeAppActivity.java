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
import com.changhong.gdappstore.util.Util;
import com.post.view.base.BasePosterLayoutView;
import com.post.view.listener.Listener.IItemOnClickListener;

public class NativeAppActivity extends PostActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
	}

	private void initData() {
		List<Object> apps = Util.getApp(context);
		postSetting.setPosttype(PostSetting.TYPE_NATIVEAPP);
		postSetting.setOnItemClickListener(nativeappPostItemOnclickListener);
		postSetting.setOnItemLongClickListener(null);
		if (apps != null) {
			// postView.initData(apps, apps.size());
			postView.refreshAllData(apps, postSetting, apps.size());
		}
	}

	/** 海报墙点击监听 **/
	private IItemOnClickListener nativeappPostItemOnclickListener = new IItemOnClickListener() {

		@Override
		public void itemOnClick(BasePosterLayoutView arg0, View arg1, int arg2) {
			NativeApp tmpInfo = (NativeApp) arg1.getTag();
			Util.openAppByPackageName(context, tmpInfo.getAppPackage());
		}
	};
}
