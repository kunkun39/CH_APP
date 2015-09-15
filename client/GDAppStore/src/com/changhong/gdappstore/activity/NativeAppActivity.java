package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.database.DBManager;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.net.LoadListener.LoadListListener;
import com.changhong.gdappstore.post.PostSetting;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;
import com.post.view.base.BasePosterLayoutView;
import com.post.view.listener.IPosteDateListener;
import com.post.view.listener.Listener.IItemOnClickListener;

/**
 * 本地应用页面,基础海报页面
 * 
 * @author wangxiufeng
 * 
 */
public class NativeAppActivity extends PostActivity {
	/** 本地应用 */
	private List<Object> nativeApps;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initView() {
		tv_name.setText("本地应用");
		// 重新配置post设置
		postSetting.setPosttype(PostSetting.TYPE_NATIVEAPP);
		postSetting.setOnItemClickListener(nativeappPostItemOnclickListener);
		postSetting.setiPosteDateListener(iPosteDateListener);
		postSetting.setOnItemLongClickListener(null);
		postSetting.setFristItemFocus(true);
		postSetting.setPost_column(5);
		postSetting.setPost_row(3);
		postSetting.setShowShandow(false);
		postSetting.setVisibleClumn(1.1f);
		postSetting.setMargins(15, 15, 0, 0);
		postView.init(postSetting);
		if (loadDataProDialog!=null && loadDataProDialog.isShowing()) {
			loadDataProDialog.dismiss();
		}
	}

	private void initData() {
		nativeApps = Util.getApp(context);
		if (nativeApps != null) {
			if (loadDataProDialog!=null && !loadDataProDialog.isShowing()) {
				loadDataProDialog.show();
			}
			postView.refreshAllData(nativeApps, postSetting, nativeApps.size());
			// 获取包名，用于请求版本号
			List<String> packages = new ArrayList<String>();
			for (int i = 0; i < nativeApps.size(); i++) {
				if (nativeApps.get(i) != null && !TextUtils.isEmpty(((NativeApp) nativeApps.get(i)).getAppPackage())) {
					packages.add(((NativeApp) nativeApps.get(i)).getAppPackage());
				}
			}
			// 请求版本号
			DataCenter.getInstance().loadAppsUpdateData(packages, new LoadListListener() {

				@Override
				public void onComplete(List<Object> items) {
					List<Object> versionApps = items;
					if (versionApps != null && versionApps.size() > 0) {
						// 设置服务端id和服务端配置版本号
						for (int i = 0; i < nativeApps.size(); i++) {
							for (int j = 0; j < versionApps.size(); j++) {
								App versionApp = (App) versionApps.get(j);
								if (versionApp != null
										&& nativeApps.get(i) != null
										&& ((NativeApp) nativeApps.get(i)).getAppPackage().equals(
												versionApp.getPackageName())) {
									((NativeApp) nativeApps.get(i)).setAppid(versionApp.getAppid());
									((NativeApp) nativeApps.get(i)).setServerVersionInt(versionApp.getVersionInt());
								}
							}
						}
					}
					// 设置本地数据库存储版本号
					List<App> dataApps = DBManager.getInstance(context).queryAppVersions();
					if (dataApps != null && dataApps.size() > 0) {
						for (int i = 0; i < nativeApps.size(); i++) {
							for (int j = 0; j < dataApps.size(); j++) {
								App dataApp = (App) dataApps.get(j);
								if (dataApp != null && nativeApps.get(i) != null
										&& ((NativeApp) nativeApps.get(i)).getAppid() == dataApp.getAppid()) {
									((NativeApp) nativeApps.get(i)).setNativeVersionInt(dataApp.getVersionInt());
								}
							}
						}
					}
					postView.refreshAllData(nativeApps, postSetting, nativeApps.size());
					if (loadDataProDialog!=null && loadDataProDialog.isShowing()) {
						loadDataProDialog.dismiss();
					}
				}
			},context);
		}
	}

	/** 海报墙点击监听 **/
	private IItemOnClickListener nativeappPostItemOnclickListener = new IItemOnClickListener() {

		@Override
		public void itemOnClick(BasePosterLayoutView arg0, View arg1, int arg2) {
			NativeApp tmpInfo = (NativeApp) arg1.getTag();
			if (tmpInfo.getAppid() > 0) {// 如果该应用来自于应用商城，进入详情
				if (!NetworkUtils.ISNET_CONNECT) {
					DialogUtil.showShortToast(context, context.getString(R.string.net_notconnected));
					return;
				}
				Intent intent = new Intent(NativeAppActivity.this, DetailActivity.class);
				intent.putExtra(Config.KEY_APPID, tmpInfo.getAppid());
				startActivity(intent);
			} else {// 启动该应用
				Util.openAppByPackageName(context, tmpInfo.getAppPackage());
			}
		}
	};
	private IPosteDateListener iPosteDateListener = new IPosteDateListener() {

		@Override
		public void requestNextPageDate(int currentSize) {
			// 请求新数据回调
		}

		@Override
		public void changePage(Boolean isnext, int curpage, int totalpage) {
			// 翻页回调
			tv_page.setText("当前显示第"+(totalpage <= 0 ? 0 : curpage)+"页;共"+totalpage+"页");
		}

		@Override
		public void lastPageOnKeyDpadDown() {
		}

		@Override
		public void firstPageOnKeyDpadup() {
		}
	};
}
