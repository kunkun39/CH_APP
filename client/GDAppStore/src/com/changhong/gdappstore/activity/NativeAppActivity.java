package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.NativeAppGridAdapter;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.net.LoadListener.LoadListListener;
import com.changhong.gdappstore.service.AppBroadcastReceiver;
import com.changhong.gdappstore.service.AppBroadcastReceiver.AppChangeListener;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.DialogUtil.DialogBtnOnClickListener;
import com.changhong.gdappstore.util.DialogUtil.DialogMessage;
import com.changhong.gdappstore.util.InstallUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;

/**
 * 本地应用页面,基础海报页面
 * 
 * @author wangxiufeng
 * 
 */
public class NativeAppActivity extends BaseActivity implements OnClickListener, OnKeyListener {
	/** 本地应用 */
	private List<NativeApp> nativeApps;
	/** 数据处理中心 */
	protected DataCenter dataCenter;
	/** 父栏目，根据首页传过来的id确定 */
	protected Category parentCategory = null;
	/** 栏目名字 */
	protected TextView tv_name, tv_page;
	/** 搜索按钮 */
	protected ImageView iv_search;
	/** 当前显示类别id **/
	protected int curCategoryId = 0;

	

	private static final int UNINSTALL_REQCODE = 11;

	private static final String DOBATCH = "卸载应用";
	private static final String SUBMIT_BACKUP = "确认卸载";
	private GridView gridView;
	private NativeAppGridAdapter adapter;
	private Button bt_batch;
	private ImageView iv_batch_icon;
	private TextView tv_batch_suggest, tv_num_checked, tv_ge;
	/** 批量操作时候选择的item个数 */
	private int curCheckedItem = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nativeapp);
		initView();
		initData();
	}

	private void initView() {
		tv_name = findView(R.id.tv_pagename);
		tv_name.setText("本地应用");
		tv_page = findView(R.id.tv_page);
		iv_search = findView(R.id.iv_search);
		iv_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(NativeAppActivity.this, SearchActivity.class));
			}
		});
		gridView = findView(R.id.gridview);

		adapter = new NativeAppGridAdapter(context, null);
		gridView.setOnItemClickListener(onItemClickListener);
		gridView.setOnKeyListener(this);
		gridView.setAdapter(adapter);

		bt_batch = findView(R.id.bt_batch);
		bt_batch.setOnKeyListener(this);
		bt_batch.setOnClickListener(this);
		bt_batch.requestFocus();

		tv_batch_suggest = findView(R.id.tv_batch_suggest);
		tv_num_checked = findView(R.id.tv_num_checked);
		iv_batch_icon = findView(R.id.iv_batch_icon);
		tv_ge = findView(R.id.tv_ge);
		showLoadingDialog();
	}


	private void initData() {
		nativeApps = Util.getApp(context);
		if (nativeApps != null) {
			adapter.updateList(nativeApps);
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
					adapter.updateList(nativeApps);
					dismissLoadingDialog();
				}
			}, context);
		}
		AppBroadcastReceiver.listeners.put(context.getClass().getName(), appChangeListener);
	}

	private AppChangeListener appChangeListener = new AppChangeListener() {

		@Override
		public void onAppChange(Intent intent) {
			L.d("onappchange---intent.getAction() " + intent.getAction() + " TopActivity "
					+ Util.getTopActivity(context) + "  " + context.getClass().getName());
			if (intent != null && intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")
					&& nativeApps != null && nativeApps.size() > 0) {
				String packageName = intent.getDataString();
				if (packageName != null && packageName.startsWith("package:")) {
					packageName = packageName.substring(packageName.indexOf(":") + 1, packageName.length());
				}
				for (int i = 0; i < nativeApps.size(); i++) {
					L.d("onappchange---packageName " + packageName + "  " + ((NativeApp) nativeApps.get(i)).appPackage);
					if (nativeApps.get(i).appPackage.equals(packageName)) {
						nativeApps.remove(i);
						break;
					}
				}
				adapter.updateList(nativeApps);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_batch:
			doBatchOnClick();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			doBatchOnClick();
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 批量操作按钮点击事件响应
	 */
	private void doBatchOnClick() {
		if (adapter.getCount() == 0) {
			DialogUtil.showShortToast(context, "没有应用存在，无法执行批量操作！");
			return;
		}
		if (bt_batch.getText().toString().equals(SUBMIT_BACKUP)) {
			// 已经是批量操作了，执行批量提交
			List<String> packages = new ArrayList<String>();
			for (int i = 0; i < adapter.getCount(); i++) {
				NativeApp app = (NativeApp) adapter.getItem(i);
				if (app.isChecked()) {
					packages.add(app.appPackage);
					app.setChecked(false);
				}
			}
			if (packages != null && packages.size() > 0) {
				unInstallApps(packages);
			}
			bt_batch.setText(DOBATCH);
			tv_batch_suggest.setText("");
			iv_batch_icon.setVisibility(VISIBLE);
			tv_num_checked.setVisibility(INVISIBLE);
			tv_ge.setVisibility(INVISIBLE);
			adapter.setBatch(false);
		} else {
			// 从正常操作转向批量操作
			bt_batch.setText(SUBMIT_BACKUP);
			tv_batch_suggest.setText("已经选择");
			iv_batch_icon.setVisibility(INVISIBLE);
			refreshCheckedItemCount();
			refreshCheckedItemText();
			adapter.setBatch(true);
		}
	}
	/**
	 * 批量卸载应用
	 * @param packages
	 */
	private void unInstallApps(final List<String> packages) {
		DialogUtil.showMyAlertDialog(context, "提示：", "确认卸载选中应用？", "确认", "取消", new DialogBtnOnClickListener() {

			@Override
			public void onSubmit(DialogMessage dialogMessage) {
				DialogUtil.showLongToast(context, "应用正在后台卸载中...");
				if (dialogMessage != null && dialogMessage.dialogInterface != null) {
					dialogMessage.dialogInterface.dismiss();
				}
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						for (int i = 0; i < packages.size(); i++) {
							InstallUtil.uninstallAppByCommond(packages.get(i));
						}
					}
				}).start();
			}

			@Override
			public void onCancel(DialogMessage dialogMessage) {
				if (dialogMessage != null && dialogMessage.dialogInterface != null) {
					dialogMessage.dialogInterface.dismiss();
				}
			}
		});

	}

	/**
	 * 批量操作时候，刷新选中的个数
	 */
	private void refreshCheckedItemText() {
		tv_num_checked.setVisibility(VISIBLE);
		tv_ge.setVisibility(VISIBLE);
		tv_num_checked.setText(curCheckedItem + "");
	}

	/**
	 * 刷新批量操作时候的选择个数
	 */
	private void refreshCheckedItemCount() {
		curCheckedItem = 0;
		List<NativeApp> items = adapter.getItems();
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).isChecked()) {
					curCheckedItem++;
				}
			}
		}
		if (tv_num_checked.getVisibility() == VISIBLE) {
			refreshCheckedItemText();
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		switch (v.getId()) {
		case R.id.bt_batch:
			// 批量操作按钮按又选中上次选中位置
			if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				gridView.requestFocus();// 选中上次选中的位置
				return true;
			}
			break;
		case R.id.gridview:
			if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				// 处理最后一排不能按下，不然焦点会跑到批量操作上面去
				int itemCount = adapter.getCount();
				int curSelectPos = gridView.getSelectedItemPosition();
				if (itemCount > 6) {
					int lastRowCount = itemCount % 3;
					if (curSelectPos >= (itemCount - lastRowCount)) {
						return true;
					}
				}
			}
			break;

		default:
			break;
		}
		return false;
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			NativeApp nativeApp = (NativeApp) adapter.getItem(position);
			if (adapter.isBatch()) {
				// 批量操作
				if (nativeApp.appPackage.equals("com.changhong.gdappstore")) {
					Toast.makeText(context, "不能卸载自己", Toast.LENGTH_SHORT).show();
				}else {
					curCheckedItem = nativeApp.isChecked() ? curCheckedItem - 1 : curCheckedItem + 1;
					refreshCheckedItemText();
					nativeApp.setChecked(!nativeApp.isChecked());
					adapter.notifyDataSetChanged();
				}
			} else {
				// 普通操作
				doItemClick(nativeApp);
			}
		}
	};

	private void doItemClick(NativeApp tmpInfo) {
		if (tmpInfo.getAppid() > 0 && NetworkUtils.ISNET_CONNECT && tmpInfo.ServerVersionInt > tmpInfo.nativeVersionInt) {// 如果该应用来自于应用商城，进入详情
			Intent intent = new Intent(NativeAppActivity.this, DetailActivity.class);
			intent.putExtra(Config.KEY_APPID, tmpInfo.getAppid());
			startActivity(intent);
		} else {// 启动该应用
			Util.openAppByPackageName(context, tmpInfo.getAppPackage());
		}
	}
}
