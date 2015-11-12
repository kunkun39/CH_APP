package com.changhong.gdappstore.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.changhong.gdappstore.service.DownLoadManager;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.DialogUtil.DialogBtnOnClickListener;
import com.changhong.gdappstore.util.DialogUtil.DialogMessage;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.InstallUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;
import com.changhong.gdappstore.view.JustifyTextView;
import com.changhong.gdappstore.view.MyProgressDialog;
import com.changhong.gdappstore.view.ScoreView;
import com.changhong.gdappstore.view.UserMayLikeView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * 应用详情页面
 * 
 * @author wangxiufeng
 * 
 */
public class DetailActivity extends BaseActivity implements OnFocusChangeListener, OnClickListener {
	private static final String TAG = "SynchRecoverActivity";
	private static final int UPDATE_DIALOG_TITLE = 110;
	private static final int SHOW_INSTALL_SUCCESS = 111;
	private static final int SHOW_INSTALL_FAILED = 112;
	private static final int DISSMISS_PDIALOG = 113;
	/** 下载按钮 */
	private ImageView bt_dowload, bt_update, bt_open, iv_recommend;
	/** 用户喜欢 */
	private UserMayLikeView view_usermaylike;
	/** 应用文本介绍信息 */
	private TextView tv_appname, tv_downloadcount, tv_size, tv_version, tv_updatetime, tv_downshelf;
	private JustifyTextView tv_introduce;
	private ImageView iv_post, iv_icon;

	private AppDetail appDetail;
	/** 下载进度提示对话框 */
	private MyProgressDialog downloadPDialog;

	private ScoreView scoreview;

	int appId = -1;
	/** 下载量是否需要加1？用于处理下载成功后手动添加下载量，不再请求服务器获取 */
	private int detailLoadCount = 0;
	/** 是否有app正在下载中 **/
	private static boolean hasAppLoading = false;

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
		tv_downshelf = findView(R.id.tv_downshelf);
		scoreview = findView(R.id.scoreview_detail);
		iv_recommend = findView(R.id.iv_recommend);
		downloadPDialog = new MyProgressDialog(context);
		downloadPDialog.setUpdateFileSizeName(true);
		downloadPDialog.dismiss();
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
		updateBtnState();
	}

	private void initData() {
		// updateService = new UpdateService(context, null, downloadPDialog);
		if (NetworkUtils.ISNET_CONNECT) {
			showLoadingDialog();
		}
		DataCenter.getInstance().loadAppDetail(appId, new LoadObjectListener() {

			@Override
			public void onComplete(Object object) {
				appDetail = (AppDetail) object;
				if (appDetail != null) {
					scoreview.setScoreBy10Total(appDetail.getScores());
					tv_appname.setText(appDetail.getAppname());
					tv_appname.setSelected(true);
					tv_downloadcount.setText(Util.intToStr(Integer.parseInt(appDetail.getDownload())));
					detailLoadCount = Integer.parseInt(appDetail.getDownload());
					tv_size.setText(TextUtils.isEmpty(appDetail.getApkSize()) ? "" : appDetail.getApkSize() + " M");
					tv_version.setText(appDetail.getVersion());
					tv_introduce.setText(appDetail.getDescription());
					tv_updatetime.setText(appDetail.getUpdateDate());
					// iv_recommend.setVisibility(appDetail.isRecommend()?VISIBLE:INVISIBLE);
					ImageLoadUtil.displayImgByNoCache(appDetail.getIconFilePath(), iv_icon);
					ImageLoadUtil.displayImgByNoCache(appDetail.getPosterFilePath(), iv_post);
					updateBtnState();
					L.d("appdetail appdetail=" + appDetail.toString());
					if (appDetail.getCategoryId() > 0) {
						initRecommendData();
					}
				}
				dismissLoadingDialog();
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
		if (!appDetail.isOnShelf()) {
			// 应用已下架
			tv_downshelf.setVisibility(VISIBLE);
			bt_dowload.setVisibility(GONE);
			bt_update.setVisibility(GONE);
		} else {
			tv_downshelf.setVisibility(GONE);
			bt_dowload.setVisibility(isInstalled ? GONE : VISIBLE);
			bt_update.setVisibility(isInstalled ? VISIBLE : GONE);
			int appdetailVersion = appDetail.getVersionInt();
			if (isInstalled) {// 比较本地已安装版本号
				int installedVersion = installed.getNativeVersionInt();
				bt_update.setVisibility((appdetailVersion > installedVersion) ? VISIBLE : GONE);
			}
		}
		// 焦点控制
		if (view_usermaylike.getCurFocuesView() != null) {
			view_usermaylike.getCurFocuesView().requestFocus();
		} else {
			if (isInstalled || !appDetail.isOnShelf()) {
				bt_open.requestFocus();
			} else {
				bt_dowload.requestFocus();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_start:
			Util.openAppByPackageName(context, appDetail.getPackageName());
			break;
		case R.id.bt_download:
			if (hasAppLoading) {
				DialogUtil.showLongToast(context, context.getString(R.string.hasapp_isloading));
			} else {
				showDownloadDialog(true);
			}
			break;
		case R.id.bt_update:
			if (hasAppLoading) {
				DialogUtil.showLongToast(context, context.getString(R.string.hasapp_isloading));
			} else {
				showDownloadDialog(true);
			}
			break;

		default:
			break;
		}
	}

	private void showDownloadDialog(final boolean isDownload) {
		String content = "";
		if (isDownload) {
			content = context.getString(R.string.sure_download_app);
		} else {
			content = context.getString(R.string.sure_update_app);
		}
		DialogUtil.showMyAlertDialog(context, "", content, "", "", new DialogBtnOnClickListener() {

			@Override
			public void onSubmit(DialogMessage dialogMessage) {
				downloadPDialog.setProgress(0);
				// updateService.update(appDetail, isDownload);
				doDownload(appDetail, isDownload);
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

	/**
	 * apk下载操作
	 * 
	 * @param appDetail
	 * @param isdownload
	 *            true 下载 false更新
	 */
	private void doDownload(final AppDetail appDetail, final boolean isdownload) {
		downloadPDialog.show();
		downloadPDialog.setProgress(0);
		downloadPDialog.setMax(0);
		downloadPDialog.setMyTitle(context.getString(R.string.downloading) + "：" + appDetail.getAppname());

		String apkLoadUrl = appDetail.getApkFilePath();
		final String apkname = apkLoadUrl.substring(apkLoadUrl.lastIndexOf("/") + 1, apkLoadUrl.length()).trim();
		hasAppLoading = true;
		DownLoadManager.putFileDownLoad(apkLoadUrl, appDetail.getPackageName(), Config.baseXutilDownPath + "/"
				+ apkname, false, true, new RequestCallBack<File>() {
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				downloadPDialog.setMax((int) total);
				downloadPDialog.setProgress((int) current);
				L.d("download detail app onloading total is " + total + " current " + current);
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onSuccess(final ResponseInfo<File> responseInfo) {
				Util.chrome0777File(Config.baseXutilDownPath);
				Util.chrome0777File(responseInfo.result.getPath());
				if (isdownload) {// 下载提交下载量，更新不提交
					DataCenter.getInstance().submitAppDownloadOK(appDetail.getAppid() + "", context);
					detailLoadCount++;
				}
				new Thread(new Runnable() {

					@Override
					public void run() {// 安装
						if (Config.ISNORMAL_INSTALL) {
							InstallUtil.installApp(context, responseInfo.result.getPath());
							handler.sendEmptyMessage(DISSMISS_PDIALOG);
						} else {
							Message installingMsg = handler.obtainMessage(UPDATE_DIALOG_TITLE);
							installingMsg.obj = context.getString(R.string.downloadover_installing);
							handler.sendMessage(installingMsg);
							boolean success = InstallUtil.installAppByCommond(responseInfo.result.getPath());
							L.d("install success " + success);
							if (success) {
								Message msg = handler.obtainMessage(SHOW_INSTALL_SUCCESS);
								msg.obj = appDetail.getAppname() + "";
								handler.sendMessage(msg);
							} else {
								Message msg = handler.obtainMessage(SHOW_INSTALL_FAILED);
								msg.obj = appDetail.getAppname() + "";
								handler.sendMessage(msg);
							}
							handler.sendEmptyMessage(DISSMISS_PDIALOG);
						}
					}
				}).start();
			}

			@Override
			public void onFailure(HttpException paramHttpException, String msg) {
				String downloadfailed = context.getString(R.string.downloadfailed) + ",";
				if (!NetworkUtils.ISNET_CONNECT) {
					DialogUtil.showLongToast(context, downloadfailed + context.getString(R.string.error_net_notconnect));
				} else if (msg.contains("ConnectTimeoutException")) {
					DialogUtil.showLongToast(context,
							downloadfailed + context.getString(R.string.error_netconnect_timeout));
				} else {
					DialogUtil.showLongToast(context, downloadfailed + context.getString(R.string.please_checknet));
				}
				handler.sendEmptyMessage(DISSMISS_PDIALOG);
			}
		});

	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case DISSMISS_PDIALOG:
				hasAppLoading = false;
				if (downloadPDialog != null && downloadPDialog.isShowing()) {
					downloadPDialog.dismiss();
				}
				updateBtnState();
				break;
			case SHOW_INSTALL_SUCCESS:
				DialogUtil.showLongToast(context, (String) msg.obj + " " + context.getString(R.string.install_success));
				break;
			case SHOW_INSTALL_FAILED:
				DialogUtil.showLongToast(context, (String) msg.obj + " " + context.getString(R.string.install_failed));
				break;
			case UPDATE_DIALOG_TITLE:
				L.d("install settitle " + (String) msg.obj);
				downloadPDialog.setMyTitle((String) msg.obj);
				break;

			default:
				break;
			}
		}
	};
}
