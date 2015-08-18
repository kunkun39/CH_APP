package com.changhong.gdappstore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.changhong.gdappstore.model.NativeApp;

public class Util {
	/**
	 * 递归删除文件及其只文件
	 * 
	 * @param path
	 */
	public static void deleteFile(String path) {
		if (TextUtils.isEmpty(path)) {
			return;
		}
		File file = new File(path);
		if (file.isDirectory() && file.listFiles().length > 0) {
			for (int i = 0; i < file.listFiles().length; i++) {
				File childFile = file.listFiles()[i];
				if (childFile.isDirectory() && childFile.listFiles().length > 0) {
					deleteFile(childFile.getAbsolutePath());
				} else {
					childFile.delete();
				}
			}
		} else {
			file.delete();
		}
	}

	/**
	 * 通过包名启动应用
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean openAppByPackageName(Context context, String packageName) {
		boolean isOk = false;
		try {
			System.out.println("packagename--------->" + packageName);
			// 通过包名启动
			PackageManager packageManager = context.getPackageManager();
			Intent intent = packageManager.getLaunchIntentForPackage(packageName);
			if (intent != null) {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				isOk = true;
			} else {
				System.out.println("getLaunchIntentForPackage is null");
			}
		} catch (Exception e) {
			isOk = false;
		}
		if (!isOk) {
			Toast.makeText(context, "应用启动失败，请检查应用是否安装正常", Toast.LENGTH_LONG).show();
		}
		return isOk;
	}

	/**
	 * 获取本地安装某应用，未安装返回空
	 * 
	 * @param context
	 * @param packageName
	 *            应用包名
	 * @return
	 */
	public static NativeApp getNativeApp(Context context, String packageName) {
		List<Object> objects = getApp(context);
		if (objects == null || objects.size() == 0 || TextUtils.isEmpty(packageName)) {
			return null;
		}
		for (int i = 0; i < objects.size(); i++) {
			NativeApp nativeApp = (NativeApp) objects.get(i);
			if (nativeApp != null && nativeApp.getAppPackage() != null && nativeApp.getAppPackage().equals(packageName)) {
				return nativeApp;
			}
		}
		return null;
	}

	/**
	 * 获取本地安装应用列表
	 * 
	 * @param context
	 * @return
	 */
	public static List<Object> getApp(Context context) {
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
				tmpInfo.appname = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
				tmpInfo.appPackage = packageInfo.packageName;
				tmpInfo.versionName = packageInfo.versionName;
				tmpInfo.versionCode = packageInfo.versionCode;
				tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
				nativeApps.add(tmpInfo);
			}
		}
		return nativeApps;
	}
}
