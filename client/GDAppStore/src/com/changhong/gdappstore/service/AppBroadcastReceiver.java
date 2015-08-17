package com.changhong.gdappstore.service;

import com.changhong.gdappstore.util.L;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收安装广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
			L.d("AppBroadcastReceiver--installed app "+packageName);
		}
		// 接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			L.d("AppBroadcastReceiver--uninstalled app "+packageName);

		}
	}
}
