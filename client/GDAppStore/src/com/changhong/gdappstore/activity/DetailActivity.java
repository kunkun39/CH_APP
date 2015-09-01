package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.database.DBManager;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.net.LoadListener;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
import com.changhong.gdappstore.service.UpdateService;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;
import com.changhong.gdappstore.view.ScoreView;
import com.changhong.gdappstore.view.UserMayLikeView;

/**
 * 应用详情页面
 * 
 * @author wangxiufeng
 * 
 */
public class DetailActivity extends BaseActivity implements OnFocusChangeListener, OnClickListener {
	/** 下载按钮 */
	private ImageView bt_dowload, bt_update, bt_open;
	/** 用户喜欢 */
	private UserMayLikeView view_usermaylike;
	/** 应用文本介绍信息 */
	private TextView tv_appname, tv_downloadcount, tv_size, tv_version, tv_updatetime, tv_introduce;

	private ImageView iv_post, iv_icon;

	private AppDetail appDetail;

	private ProgressDialog downloadPDialog, updateAppPDialog;

	private UpdateService updateService;

	private ScoreView scoreview;

	int appId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		if (getIntent() != null) {
			appId = getIntent().getIntExtra(Config.KEY_APPID, -1);
		}
		initView();
		initData();
	}

	private void initView() {
		view_usermaylike = findView(R.id.view_usermaylike);
		bt_dowload = findView(R.id.bt_download);
		bt_dowload.setOnFocusChangeListener(this);
		bt_dowload.setOnClickListener(this);
		bt_update = findView(R.id.bt_update);
		bt_update.setOnFocusChangeListener(this);
		bt_update.setOnClickListener(this);
		bt_open = findView(R.id.bt_start);
		bt_open.setOnFocusChangeListener(this);
		bt_open.setOnClickListener(this);
		bt_dowload.setVisibility(GONE);
		bt_open.setVisibility(GONE);
		bt_update.setVisibility(GONE);
		iv_post = findView(R.id.iv_detailpost);
		iv_icon = findView(R.id.iv_detailicon);
		tv_appname = findView(R.id.tv_appname);
		tv_downloadcount = findView(R.id.tv_downloadcount);
		tv_size = findView(R.id.tv_appsize);
		tv_version = findView(R.id.tv_version);
		tv_updatetime = findView(R.id.tv_updatetime);
		tv_introduce = findView(R.id.tv_introduce);
		scoreview = findView(R.id.scoreview_detail);
		downloadPDialog = new ProgressDialog(context);
		downloadPDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		downloadPDialog.setTitle("当前下载进度...");
		downloadPDialog.setCancelable(false);
		downloadPDialog.setButton("后台下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
		updateAppPDialog = new ProgressDialog(context);
		updateAppPDialog.setCancelable(false);
		updateAppPDialog.dismiss();
		view_usermaylike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getTag() != null) {
					App app = (App) v.getTag();
					appId = app.getAppid();
					initData();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (downloadPDialog != null) {
			downloadPDialog.dismiss();
		}
		if (updateAppPDialog != null) {
			updateAppPDialog.dismiss();
		}
		updateBtnState();
	}

	private void initData() {
		scoreview.setScoreBy5Total(Util.getStarRandomInt());
		updateService = new UpdateService(context, null, downloadPDialog);
		updateAppPDialog.show();
		DataCenter.getInstance().loadAppDetail(appId, new LoadObjectListener() {

			@Override
			public void onComplete(Object object) {
				appDetail = (AppDetail) object;
				updateAppPDialog.dismiss();
				if (appDetail != null) {
					tv_appname.setText(appDetail.getAppname());
					tv_downloadcount.setText(appDetail.getDownload());
					tv_size.setText(TextUtils.isEmpty(appDetail.getApkSize())?"":appDetail.getApkSize()+" M");
					tv_version.setText(appDetail.getVersion());
					tv_introduce.setText(appDetail.getDescription());
					tv_updatetime.setText(appDetail.getUpdateDate());
					ImageLoadUtil.displayImgByNoCache(appDetail.getIconFilePath(), iv_icon);
					ImageLoadUtil.displayImgByNoCache(appDetail.getPosterFilePath(), iv_post);
					updateBtnState();
					L.d("appdetail appdetail=" + appDetail.toString());
					if (appDetail.getCategoryId() > 0) {
						initRecommendData();
					}
				}
			}
		});

	}

	/**
	 * 获取猜你喜欢数据
	 */
	private void initRecommendData() {
		DataCenter.getInstance().loadRecommendData(context, appDetail.getCategoryId(),
				new LoadListener.LoadListListener() {

					@Override
					public void onComplete(List<Object> items) {
						if (items != null && items.size() > 0) {
							List<App> apps = new ArrayList<App>();
							for (int i = 0; i < items.size(); i++) {
								apps.add(((App) items.get(i)));
							}
							view_usermaylike.initData(apps);
						} else {
							view_usermaylike.initData(null);
						}
					}
				});
	}

	/**
	 * 更改按钮状态
	 */
	private void updateBtnState() {
		if (appDetail == null) {
			return;
		}
		// 是否已经安装
		boolean isInstalled = Util.getNativeApp(context, appDetail.getPackageName()) != null;
		bt_open.setVisibility(isInstalled ? VISIBLE : GONE);
		bt_dowload.setVisibility(isInstalled ? GONE : VISIBLE);
		bt_update.setVisibility(isInstalled ? VISIBLE : GONE);
		if (isInstalled) {
			bt_open.requestFocus();
		}else {
			bt_dowload.requestFocus();
		}
		App nativeApp = DBManager.getInstance(context).queryAppVersionById(appDetail.getAppid());
		if (nativeApp != null) {
			// 存在该应用
			try {// 因为不能保证所有应用的versionname都能强制转行为float类型
				int appdetailVersion = appDetail.getVersionInt();
				int nativeVersion = nativeApp.getVersionInt();
				L.d("checkversion--appdetail is " + appDetail.getVersionInt() + " nativeapp is " + nativeApp.getVersionInt());
				bt_update.setVisibility((appdetailVersion > nativeVersion) ? VISIBLE : GONE);
			} catch (Exception e) {
				L.e("checkversion--error appdetail is " + appDetail.getVersionInt() + " nativeapp is "
						+ nativeApp.getVersionInt());
				e.printStackTrace();
			}
		} else {
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start:
			Util.openAppByPackageName(context, appDetail.getPackageName());
			break;
		case R.id.bt_download:
			downloadPDialog.setProgress(0);
			updateService.update(appDetail, true);
			break;
		case R.id.bt_update:
			downloadPDialog.setProgress(0);
			updateService.update(appDetail, false);
			break;

		default:
			break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}
}
