package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
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
import com.changhong.gdappstore.view.UserMayLikeView;

/**
 * 应用详情页面
 * 
 * @author wangxiufeng
 * 
 */
public class DetailActivity extends BaseActivity implements OnFocusChangeListener, OnClickListener {
	/** 下载按钮 */
	private Button bt_dowload, bt_update, bt_open;
	/** 用户喜欢 */
	private UserMayLikeView view_usermaylike;
	/** 应用文本介绍信息 */
	private TextView tv_appname, tv_downloadcount, tv_size, tv_version, tv_updatetime, tv_controltool, tv_introduce;

	private ImageView iv_post, iv_icon;

	private AppDetail appDetail;

	private ProgressDialog progressDialog;

	private UpdateService updateService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initView();
		initData();
		L.d("detail oncreate");
	}

	private void initView() {
		view_usermaylike = findView(R.id.view_usermaylike);
		bt_dowload = findView(R.id.bt_download);
		bt_dowload.setOnFocusChangeListener(this);
		bt_dowload.setOnClickListener(this);
		bt_update = findView(R.id.bt_update);
		bt_update.setOnFocusChangeListener(this);
		bt_update.setOnClickListener(this);
		bt_open = findView(R.id.bt_open);
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
		tv_controltool = findView(R.id.tv_controltool);
		tv_introduce = findView(R.id.tv_introduce);
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle("当前下载进度...");
		progressDialog.setCancelable(false);
		progressDialog.setButton("后台下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (dialog != null) {
					dialog.dismiss();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		updateBtnState();
	}

	private void initData() {
		int appId = -1;
		if (getIntent() != null) {
			appId = getIntent().getIntExtra(Config.KEY_APPID, -1);
		}
		updateService = new UpdateService(context, null, progressDialog);
		DataCenter.getInstance().loadAppDetail(appId, new LoadObjectListener() {

			@Override
			public void onComplete(Object object) {
				appDetail = (AppDetail) object;
				if (appDetail != null) {
					tv_appname.setText(appDetail.getAppname());
					tv_downloadcount.setText(appDetail.getDownload());
					tv_size.setText(appDetail.getApkSize());
					tv_version.setText(appDetail.getVersion());
					tv_introduce.setText(appDetail.getDescription());
					ImageLoadUtil.displayImgByNoCache(appDetail.getIconFilePath(), iv_icon);
					ImageLoadUtil.displayImgByNoCache(appDetail.getPosterFilePath(), iv_post);
					updateBtnState();
					if (appDetail.getCategoryId() > 0) {
						initRecommendData();
					}
				}
			}
		});

	}

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
		App nativeApp = DBManager.getInstance(context).queryAppVersionById(appDetail.getAppid());
		if (nativeApp != null) {
			// 存在该应用
			bt_open.setVisibility(VISIBLE);
			bt_dowload.setVisibility(GONE);
			try {// 因为不能保证所有应用的versionname都能强制转行为float类型
				float appdetailVersion = Float.parseFloat(appDetail.getVersion());
				float nativeVersion = Float.parseFloat(nativeApp.getVersion());
				L.d("checkversion--appdetail is " + appDetail.getVersion() + " nativeapp is " + nativeApp.getVersion());
				bt_update.setVisibility((appdetailVersion > nativeVersion) ? VISIBLE : GONE);
			} catch (Exception e) {
				L.e("checkversion--error appdetail is " + appDetail.getVersion() + " nativeapp is "
						+ nativeApp.getVersion());
				bt_update.setVisibility(GONE);// TODO 如果版本号转换异常就不能更新
				e.printStackTrace();
			}
		} else {
			bt_open.setVisibility(GONE);
			bt_update.setVisibility(GONE);
			bt_dowload.setVisibility(VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_open:
			Util.openAppByPackageName(context, appDetail.getPackageName());
			break;
		case R.id.bt_download:
			progressDialog.setProgress(0);
			updateService.update(appDetail, true);
			break;
		case R.id.bt_update:
			progressDialog.setProgress(0);
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
