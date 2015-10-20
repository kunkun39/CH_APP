package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
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
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.net.LoadListener;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
import com.changhong.gdappstore.service.UpdateService;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.DialogUtil.DialogBtnOnClickListener;
import com.changhong.gdappstore.util.DialogUtil.DialogMessage;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;
import com.changhong.gdappstore.view.JustifyTextView;
import com.changhong.gdappstore.view.MyProgressDialog;
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
	private ImageView bt_dowload, bt_update, bt_open,iv_recommend;
	/** 用户喜欢 */
	private UserMayLikeView view_usermaylike;
	/** 应用文本介绍信息 */
	private TextView tv_appname, tv_downloadcount, tv_size, tv_version, tv_updatetime;
	private JustifyTextView tv_introduce;
	private ImageView iv_post, iv_icon;

	private AppDetail appDetail;
	/** 数据更新提示对话框 */
	private ProgressDialog updateAppPDialog;
	/** 下载进度提示对话框 */
	private MyProgressDialog downloadPDialog;
	/** 下载进度提示下载功能 */
	private UpdateService updateService;

	private ScoreView scoreview;

	int appId = -1;
	/** 下载量是否需要加1？用于处理下载成功后手动添加下载量，不再请求服务器获取 */
	public static int detailLoadCount = 0;

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
		iv_recommend = findView(R.id.iv_recommend);
		downloadPDialog = new MyProgressDialog(context);
		downloadPDialog.setUpdateFileSizeName(true);
		downloadPDialog.dismiss();
		updateAppPDialog = DialogUtil.showCirculProDialog(context, context.getString(R.string.tishi),
				context.getString(R.string.dataloading), true);
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
		updateService = new UpdateService(context, null, downloadPDialog);
		if (NetworkUtils.ISNET_CONNECT) {
			updateAppPDialog.show();
		}
		DataCenter.getInstance().loadAppDetail(appId, new LoadObjectListener() {

			@Override
			public void onComplete(Object object) {
				appDetail = (AppDetail) object;
				updateAppPDialog.dismiss();
				if (appDetail != null) {
					scoreview.setScoreBy10Total(appDetail.getScores());
					tv_appname.setText(appDetail.getAppname());
					tv_downloadcount.setText(Util.intToStr(Integer.parseInt(appDetail.getDownload())));
					detailLoadCount = Integer.parseInt(appDetail.getDownload());
					tv_size.setText(TextUtils.isEmpty(appDetail.getApkSize()) ? "" : appDetail.getApkSize() + " M");
					tv_version.setText(appDetail.getVersion());
					tv_introduce.setText(appDetail.getDescription());
					tv_updatetime.setText(appDetail.getUpdateDate());
//					iv_recommend.setVisibility(appDetail.isRecommend()?VISIBLE:INVISIBLE);
					ImageLoadUtil.displayImgByNoCache(appDetail.getIconFilePath(), iv_icon);
					ImageLoadUtil.displayImgByNoCache(appDetail.getPosterFilePath(), iv_post);
					updateBtnState();
					L.d("appdetail appdetail=" + appDetail.toString());
					if (appDetail.getCategoryId() > 0) {
						initRecommendData();
					}
				}
			}
		}, context);

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
								if (((App) items.get(i)).getAppid() != appDetail.getAppid()) {
									apps.add(((App) items.get(i)));
								}
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
		tv_downloadcount.setText(Util.intToStr(detailLoadCount));
		// 是否已经安装
		NativeApp installed = Util.getNativeApp(context, appDetail.getPackageName());
		boolean isInstalled = installed != null;
		bt_open.setVisibility(isInstalled ? VISIBLE : GONE);
		bt_dowload.setVisibility(isInstalled ? GONE : VISIBLE);
		bt_update.setVisibility(isInstalled ? VISIBLE : GONE);
		if (view_usermaylike.getCurFocuesView() != null) {
			view_usermaylike.getCurFocuesView().requestFocus();
		} else {
			if (isInstalled) {
				bt_open.requestFocus();
			} else {
				bt_dowload.requestFocus();
			}
		}
		int appdetailVersion = appDetail.getVersionInt();
		// App databaseApp =
		// DBManager.getInstance(context).queryAppVersionById(appDetail.getAppid());
		// if (databaseApp != null) {// 数据库存在该应用（取消数据库作用）
		// int databaseAppVersion = databaseApp.getVersionInt();
		// bt_update.setVisibility((appdetailVersion > databaseAppVersion) ?
		// VISIBLE : GONE);
		// L.d("checkversion--appdetail is " + appdetailVersion +
		// " databaseAppVersion is " + databaseAppVersion);
		// } else if (isInstalled) {// 数据库不存在再比较已安装apk版本号
		if (isInstalled) {//比较本地已安装版本号
			int installedVersion = installed.getNativeVersionInt();
			bt_update.setVisibility((appdetailVersion > installedVersion) ? VISIBLE : GONE);
			L.d("checkversion--appdetail is " + appdetailVersion + " installedVersion is " + installedVersion);
		} else {
			// 没有安装
			L.d("checkversion--appdetail isnot in database and not installed" + appDetail.getAppid());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start:
			Util.openAppByPackageName(context, appDetail.getPackageName());
			break;
		case R.id.bt_download:
			showDownloadDialog(true);
			break;
		case R.id.bt_update:
			showDownloadDialog(false);
			break;

		default:
			break;
		}
	}

	private void showDownloadDialog(final boolean isDownload) {
		String content = "";
		if (isDownload) {
			content = "确认下载应用？";
		} else {
			content = "确认更新应用？";
		}
		DialogUtil.showMyAlertDialog(context, "提示：", content, "确  定", "取  消", new DialogBtnOnClickListener() {

			@Override
			public void onSubmit(DialogMessage dialogMessage) {
				downloadPDialog.setProgress(0);
				updateService.update(appDetail, isDownload);
				if (dialogMessage != null && dialogMessage.dialogInterface != null) {
					dialogMessage.dialogInterface.dismiss();
				}
			}

			@Override
			public void onCancel(DialogMessage dialogMessage) {
				if (dialogMessage != null && dialogMessage.dialogInterface != null) {
					dialogMessage.dialogInterface.dismiss();
				}
			}
		});
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}
}
