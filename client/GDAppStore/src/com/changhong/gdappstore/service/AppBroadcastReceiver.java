package com.changhong.gdappstore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;

public class AppBroadcastReceiver extends BroadcastReceiver {
//	public static AppDetail curAppDetail;

	/** 网络情况改变回调监听器 key为context.getClass().getName()以防重复添加 */
	public static Map<String, AppChangeListener> listeners=new HashMap<String, AppBroadcastReceiver.AppChangeListener>();
	/**
	 * 网络变化监听器
	 * 
	 * @author wangxiufeng
	 * 
	 */
	public interface AppChangeListener {
		void onAppChange(Intent intent);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (listeners != null && listeners.size() > 0) {
			for (AppChangeListener listener:listeners.values()) {
				listener.onAppChange(intent);
			}
		}
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
