package com.changhong.gdappstore.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;

public class AppBroadcastReceiver extends BroadcastReceiver {
//	public static AppDetail curAppDetail;

	@Override
	public void onReceive(Context context, Intent intent) {
		// 接收安装广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
			String packageName = intent.getDataString();
			L.d("AppBroadcastReceiver--installed packageName=" + packageName+" ");
			//  删除安装包
			Util.deleteFileChildrens(UpdateService.baseUpdatePath);
			
			if (packageName!=null&&packageName.startsWith("package:")) {
				packageName=packageName.substring(packageName.indexOf(":")+1, packageName.length());
			}
//			//将新安装的应用版本好存入数据库//TODO 取消数据库保存应用数据信息
//			if (curAppDetail != null && !TextUtils.isEmpty(curAppDetail.getPackageName())
//					&& curAppDetail.getPackageName().equals(packageName)) {
//				DBManager.getInstance(context).insertOrUpdateVersions(curAppDetail);
//			}
		}
		// 接收卸载广播
		if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
			String packageName = intent.getDataString();
			L.d("AppBroadcastReceiver--uninstalled app " + packageName);

		}
	}
}
