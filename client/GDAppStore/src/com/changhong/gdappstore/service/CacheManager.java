package com.changhong.gdappstore.service;

import android.content.Context;

import com.changhong.gdappstore.util.SharedPreferencesUtil;

public class CacheManager {
	public static final String KEYJSON_CATEGORIES="keyjsoncategories";
	public static final String KEYJSON_PAGEAPPS="keyjsonpageapps";
	public static final String KEYJSON_CATEGORYAPPS="keyappbycategoryid";
	public static final String KEYJSON_RANKLIST="keyjsonranklist";
	public static final String KEYJSON_RECOMMENDAPPS="keyrecommendapps";
	
	/**
	 * 存放json文件缓存
	 * @param context
	 * @param key
	 * @param value
	 */
	public static final void putJsonFileCache(Context context,String key,String value) {
		SharedPreferencesUtil.putJsonCache(context, key, value);
	}
	/**
	 * 获取json文件缓存
	 * @param context
	 * @param key
	 * @return
	 */
	public static final String getJsonFileCache(Context context,String key) {
		return SharedPreferencesUtil.getJsonCache(context,key);
	}
}
