package com.changhong.gdappstore.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.widget.Toast;

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
	/** 网络情况改变回调监听器 */
	public static List<NetChangeListener> listeners = new ArrayList<NetChangeReceiver.NetChangeListener>();

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
		L.d("网络情况发生改变，isconnect= " + isconnect + " 当前class " + curClassName);
		Toast.makeText(context, "网络" + (isconnect ? "连接成功" : "断开链接"), Toast.LENGTH_SHORT).show();
		if (listeners != null && listeners.size() > 0) {
			for (int i = 0; i < listeners.size(); i++) {
				listeners.get(i).onNetChange(isconnect);
			}
		}
	}

}
