package com.changhong.gdappstore.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.service.AppBroadcastReceiver.AppChangeListener;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;

/**
 * 网络变化监听器
 * 
 * @author wangxiufeng
 * 
 */
public class NetChangeReceiver extends BroadcastReceiver {
	/** 网络情况改变回调监听器 key为context.getClass().getName()以防重复添加 */
	public static Map<String, NetChangeListener> listeners = new HashMap<String, NetChangeReceiver.NetChangeListener>();

	/**
	 * 网络变化监听器
	 * 
	 * @author wangxiufeng
	 * 
	 */
	public interface NetChangeListener {
		void onNetChange(boolean isconnect);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isconnect = NetworkUtils.isConnectInternet(context);
		String curClassName = Util.getTopActivity(context);
		L.d("网络情况发生改变，isconnect= " + isconnect + " 当前class " + curClassName + " ipaddress is "
				+ NetworkUtils.getIpAddress(context));
		// String ipaddress=" ip地址是："+NetworkUtils.getIpAddress(context);

		Toast.makeText(context,
				(isconnect ? context.getString(R.string.net_connected) : context.getString(R.string.net_disconnect)),
				Toast.LENGTH_LONG).show();
		if (listeners != null && listeners.size() > 0) {
			for (NetChangeListener listener : listeners.values()) {
				listener.onNetChange(isconnect);
			}
		}
	}

}
