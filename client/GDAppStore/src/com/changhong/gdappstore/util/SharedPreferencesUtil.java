package com.changhong.gdappstore.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences文件工具类
 * 
 * @author wangxiufeng
 * 
 */
public class SharedPreferencesUtil {
	/** jsoncache存放文件 */
	public static final String JSON_CACHEFILE = "jsoncache";

	/**
	 * 获取SharedPreferences文件对象，类型为私有
	 * 
	 * @param context
	 * @param name
	 *            文件名字
	 * @return
	 */
	public static SharedPreferences getPrivateShareFile(Context context, String name) {
		return context == null ? null : context.getSharedPreferences(name, Activity.MODE_PRIVATE);
	}

	public static void putSharedString(Context context, String filename, String key, String value) {
		SharedPreferences sharedPreferences = getPrivateShareFile(context, filename);
		if (sharedPreferences == null) {
			L.e("sharedPreferences is null when putSharedString");
			return;
		}
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getSharedString(Context context, String filename, String key, String defaultValue) {
		SharedPreferences sharedPreferences = getPrivateShareFile(context, filename);
		if (sharedPreferences == null) {
			L.e("sharedPreferences is null when getSharedString");
			return defaultValue;
		}
		String json = sharedPreferences.getString(key, defaultValue);
		return json;
	}

	/**
	 * 存放json字符串私用
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putJsonCache(Context context, String key, String value) {
		putSharedString(context, JSON_CACHEFILE, key, value);
	}

	/**
	 * 获取json字符串私用
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getJsonCache(Context context, String key) {
		return getSharedString(context, JSON_CACHEFILE, key, "");
	}
}
