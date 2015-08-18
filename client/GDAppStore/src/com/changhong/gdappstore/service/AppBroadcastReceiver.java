package com.changhong.gdappstore.service;

import java.io.File;

import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;

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
			//TODO 删除安装包
			File file=new File(UpdateService.baseUpdatePath);
			if (file!=null&& file.isDirectory()&& file.listFiles().length>0) {
				for (int i = 0; i < file.listFiles().length; i++) {
					Util.deleteFile(file.listFiles()[i].getAbsolutePath());
				}
			}
		}
		// 接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			L.d("AppBroadcastReceiver--uninstalled app "+packageName);

		}
	}
}
