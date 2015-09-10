package com.changhong.gdappstore.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.service.UpdateService;

public class Util {
	/**
	 * 获取当前显示activity
	 * 
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		String curClassName = info.topActivity.getClassName(); // 类名
		return curClassName;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dipTopx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int pxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static float getStarRandomInt() {
		int max = 5;
		int min = 3;
		Random random = new Random();
//		float s = random.nextInt(max) % (max - min + 1) + min;
		float s = random.nextInt(max - min+1) + min;
		return s;
	}
	/**
	 * 只删除子文件，不删除父文件
	 * @param path 父文件目录
	 */
	public static void deleteFileChildrens(String path) {
		File file = new File(path);
		if (file != null && file.isDirectory() && file.listFiles().length > 0) {
			for (int i = 0; i < file.listFiles().length; i++) {
				Util.deleteFile(file.listFiles()[i].getAbsolutePath());
			}
		}
	}

	/**
	 * 递归删除文件及其子文件
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
		if (packageName != null && packageName.equals("com.changhong.gdappstore")) {
			Toast.makeText(context, "该应用已打开", Toast.LENGTH_LONG).show();
			return false;
		}
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
				tmpInfo.appIcon = packageInfo.applicationInfo.loadIcon(context.getPackageManager());
				nativeApps.add(tmpInfo);
				tmpInfo.appid = -1;
			}
		}
		return nativeApps;
	}

	/**
	 * int转为str类型
	 * 
	 * @param int
	 * @return String
	 */
	public static String intToStr(int num) {
		if (num < 10000) {
			// 1w以下直接返回
			return num + "";
		} else if (num < 100000) {
			// 1w~10w以下格式:x0000+ x为数字
			return num / 10000 + "0000+";
		} else if (num < 1000000) {
			// 10w~100w以下格式:x0万+ x为数字
			return num / 100000 + "0万+";
		} else {
			return num / 1000000 + "00万+";
		}
	}
	
	/**
	 * 判断list是否为空
	 * 
	 * @param int
	 * @return String
	 */
	public static boolean listIsEmpty(List list) {
		if(list == null || list.isEmpty()) {
			return true;
		}
		return false;
	}
}
