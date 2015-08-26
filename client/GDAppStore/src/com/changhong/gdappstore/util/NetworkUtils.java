package com.changhong.gdappstore.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络工具类
 * 
 * @author wangxiufeng
 * 
 */
public class NetworkUtils {
	/** 网络是否链接 */
	public static boolean ISNET_CONNECT = true;

	public static boolean isConnectInternet(final Context pContext) {
		final ConnectivityManager conManager = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			ISNET_CONNECT = networkInfo.isAvailable();
			return ISNET_CONNECT;
		}
		ISNET_CONNECT = false;
		return ISNET_CONNECT;
	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}

		return false;
	}

}
