package com.changhong.gdappstore.service;

import java.io.File;

import com.changhong.gdappstore.database.DBManager;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class AppBroadcastReceiver extends BroadcastReceiver {
	public static AppDetail curAppDetail;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收安装广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
			L.d("AppBroadcastReceiver--installed packageName=" + packageName+" "+curAppDetail);
			// TODO 删除安装包
			File file = new File(UpdateService.baseUpdatePath);
			if (file != null && file.isDirectory() && file.listFiles().length > 0) {
				for (int i = 0; i < file.listFiles().length; i++) {
					Util.deleteFile(file.listFiles()[i].getAbsolutePath());
				}
			}
			if (packageName!=null&&packageName.startsWith("package:")) {
				packageName=packageName.substring(packageName.indexOf(":")+1, packageName.length());
			}
			//将新安装的应用版本好存入数据库
			if (curAppDetail != null && !TextUtils.isEmpty(curAppDetail.getPackageName())
					&& curAppDetail.getPackageName().equals(packageName)) {
				DBManager.getInstance(context).insertOrUpdateVersions(curAppDetail);
			}
		}
		// 接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			L.d("AppBroadcastReceiver--uninstalled app " + packageName);

		}
	}
}
