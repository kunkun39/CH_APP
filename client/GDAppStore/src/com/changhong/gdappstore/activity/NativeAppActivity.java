package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.net.LoadListener.LoadListListener;
import com.changhong.gdappstore.post.PostSetting;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;
import com.post.view.base.BasePosterLayoutView;
import com.post.view.listener.Listener.IItemOnClickListener;

/**
 * 本地应用页面,基础海报页面
 * 
 * @author wangxiufeng
 * 
 */
public class NativeAppActivity extends PostActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String aString="1.1.1";
		String bString="1.10";
		int equal=aString.compareTo(bString);
		Toast.makeText(context, equal+"", Toast.LENGTH_SHORT).show();
		initData();
	}

	private void initData() {
		List<Object> apps = Util.getApp(context);
		postSetting.setPosttype(PostSetting.TYPE_NATIVEAPP);
		postSetting.setOnItemClickListener(nativeappPostItemOnclickListener);
		postSetting.setOnItemLongClickListener(null);
		if (apps != null) {
			// postView.initData(apps, apps.size());
			postView.refreshAllData(apps, postSetting, apps.size());
			List<String> packages = new ArrayList<String>();
			for (int i = 0; i < apps.size(); i++) {
				if (apps.get(i) != null && !TextUtils.isEmpty(((NativeApp) apps.get(i)).getAppPackage())) {
					packages.add(((NativeApp) apps.get(i)).getAppPackage());
				}
			}
			DataCenter.getInstance().loadAppsUpdateData(packages, new LoadListListener() {

				@Override
				public void onComplete(List<Object> items) {
					List<Object> apps=items;
					for (int i = 0; i < apps.size(); i++) {
						L.d("app----"+((App)apps.get(i)).toString());
					}
				}
			});
		}
	}

	/** 海报墙点击监听 **/
	private IItemOnClickListener nativeappPostItemOnclickListener = new IItemOnClickListener() {

		@Override
		public void itemOnClick(BasePosterLayoutView arg0, View arg1, int arg2) {
			NativeApp tmpInfo = (NativeApp) arg1.getTag();
			Util.openAppByPackageName(context, tmpInfo.getAppPackage());
		}
	};
}
