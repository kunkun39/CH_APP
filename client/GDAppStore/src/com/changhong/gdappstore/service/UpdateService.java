package com.changhong.gdappstore.service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.DialogUtil.DialogBtnOnClickListener;
import com.changhong.gdappstore.util.DialogUtil.DialogMessage;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

public class UpdateService {

	/** 系统上下文 */
	private Context context;

	/** ACTIVITY传过来的消息处理 */
	private Handler handler;

	/** handler需要处理的MESSAGE的编号 */
	private int MESSAGE_SERVER_FILEERROR = 11;
	private int MESSAGE_DOWNLOADOVER = 12;
	private int MESSAGE_DOWNEXCEPTION = 13;

	/** ACITIVY传过来更新进度条 */
	private ProgressDialog m_pDialog;

	public static boolean downloading = false;
	public static boolean THREAD_ONE_FINISHED = true;
	public static boolean THREAD_TWO_FINISHED = true;
	public static boolean THREAD_DOWNLOAD_EXCEPTION = false;

	public static final String baseUpdatePath = "/data/data/com.changhong.tvhelper/";
	public static File updateFile;
	public static AppDetail appDetail;

	public UpdateService(Context context, Handler handler, ProgressDialog m_pDialog) {
		this.context = context;
		this.handler = handler;
		this.m_pDialog = m_pDialog;
	}

	public void update(AppDetail appDetail) {
		if (appDetail == null || TextUtils.isEmpty(appDetail.getApkFilePath())) {
			return;
		}
		if (downloading) {
			Toast.makeText(context, "当前正在下载更新，请耐心等待", Toast.LENGTH_SHORT).show();
			return;
		}

		/**
		 * 检查上次下载是否有异常
		 */
		// UpdateLogService preferenceService = new UpdateLogService(context);
		// boolean downingException =
		// preferenceService.isDownloadingException();
		this.appDetail = appDetail;
		String apkname = appDetail.getApkFilePath().substring(appDetail.getApkFilePath().lastIndexOf("/"),
				appDetail.getApkFilePath().length());
		updateFile = new File(baseUpdatePath + apkname);
		/**
		 * 根据下载的文件和上次的异常来判断走升级那个流程
		 */
		if (updateFile.exists()) {
			/**
			 * 本地APK已存在流程
			 */
			fileExistFlow();
		} else {
			/**
			 * 本地文件不存在，从服务器获得更新流程
			 */
			fileNotExistFlow();
		}

	}

	private void fileExistFlow() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// 先比较本地下载APK和服务端最新的版本
					PackageManager pm = context.getPackageManager();
					PackageInfo filePMInfo = pm.getPackageArchiveInfo(updateFile.getAbsolutePath().toString(),
							PackageManager.GET_ACTIVITIES);
					final String newVersion = appDetail.getVersion();// versonname，不是versioncode
					if (!TextUtils.isEmpty(newVersion)) {
						// 先比较本地程序和服务器的版本
						if (filePMInfo != null) {
							// int installVersionCode = filePMInfo.versionCode;
							float installVersionName = Float.parseFloat(filePMInfo.versionName);
							float newVersionName = Float.parseFloat(newVersion);
							if (newVersionName > installVersionName) {
								// 有更新 弹框提示下载更新
								updateFile.delete();
								fileNotExistFlow();
								return;
							}
						} else {
							// 文件包存在，但是又得不到信息，证明下载的文件又问题，重新下载
							updateFile.delete();
							fileNotExistFlow();
							return;
						}
					}

					// 弹框提示安装
					installApp();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	private void fileNotExistFlow() {
		// 本地版本小于等于服务器版本,有更新 弹框提示下载更新
		downloadApp();
	}

	private void downloadApp() {

		Dialog dialog = DialogUtil.showAlertDialog(context, "提示：", "软件下载中...", new DialogBtnOnClickListener() {

			@Override
			public void onSubmit(DialogMessage dialogMessage) {
				m_pDialog.show();

				new Thread(new Runnable() {
					@Override
					public void run() {
						if (!NetworkUtils.isConnectInternet(context)) {
							return;
						}
						try {
							downloading = true;

							// 先获得文件的大小
							int fileTotalSize = 0;
							HttpURLConnection connection = null;
							try {
								URL url = new URL(appDetail.getApkFilePath());
								connection = (HttpURLConnection) url.openConnection();
								connection.setUseCaches(false);
								connection.setConnectTimeout(20000);
								connection.setRequestMethod("GET");
								if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
									connection.connect();
									fileTotalSize = connection.getContentLength();
									m_pDialog.setMax(fileTotalSize);
								}
							} catch (Exception e) {
								downloading = false;
								e.printStackTrace();
							} finally {
								try {
									if (connection != null) {
										connection.disconnect();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							// 获得文件的大小为0，直接返回, 并通知用户
							if (fileTotalSize <= 0) {
								Message message = new Message();
								message.arg1 = MESSAGE_SERVER_FILEERROR;
								handler.sendMessage(message);
								downloading = false;
								return;
							}

							// 计算两个线程分别要下载的文件大小
							long firstThreadStart = 0;
							long firstThreadEnd = fileTotalSize / 2;
							long secondThreadStart = fileTotalSize / 2 + 1;
							long secondThreadEnd = fileTotalSize;

							// 第一个线程下载
							THREAD_ONE_FINISHED = false;
							int DOWNLOAD_THREAD_ONE = 1;
							UpdateFileDownloadThread firstThread = new UpdateFileDownloadThread(context,
									DOWNLOAD_THREAD_ONE, firstThreadStart, firstThreadEnd);
							firstThread.start();

							// 第二个线程下载
							THREAD_TWO_FINISHED = false;
							int DOWNLOAD_THREAD_TWO = 2;
							UpdateFileDownloadThread secondThread = new UpdateFileDownloadThread(context,
									DOWNLOAD_THREAD_TWO, secondThreadStart, secondThreadEnd);
							secondThread.start();

							// 不停的更新下载的状态
							UpdateLogService preferenceService = new UpdateLogService(context);
							while (!THREAD_ONE_FINISHED || !THREAD_TWO_FINISHED) {
								Thread.sleep(100);

								/**
								 * 检查是否有异常，如果有异常，向外抛出异常， 用handler通知用户
								 */
								if (THREAD_DOWNLOAD_EXCEPTION) {
									THREAD_DOWNLOAD_EXCEPTION = false;
									throw new RuntimeException("thread download fail");
								}

								/**
								 * 计算现在更新的进度
								 */
								int alreadyRead = (int) preferenceService.getTotalDownlaodDataSize();
								m_pDialog.setProgress(alreadyRead);
							}

							// 下载完成，重置下载的进度
							preferenceService.saveThreadDownloadDataSize(DOWNLOAD_THREAD_ONE, 0);
							preferenceService.saveThreadDownloadDataSize(DOWNLOAD_THREAD_TWO, 0);

							// 下载完成 安装
							installApp();

							// 现在完成，设置DONWLOADING标志
							downloading = false;
							Message message = new Message();
							message.arg1 = MESSAGE_DOWNLOADOVER;
							handler.sendMessage(message);
							m_pDialog.setProgress(0);

						} catch (Exception e) {
							// 异常捕获
							e.printStackTrace();
							downloading = false;
							Message message = new Message();
							message.arg1 = MESSAGE_DOWNEXCEPTION;
							handler.sendMessage(message);
						}
					}
				}).start();
				if (dialogMessage.dialogInterface != null) {
					dialogMessage.dialogInterface.cancel();
				}
			}

			@Override
			public void onCancel(DialogMessage dialogMessage) {
				if (dialogMessage.dialogInterface != null) {
					dialogMessage.dialogInterface.cancel();
				}
			}
		});
	}

	private void installApp() {

		// 安装最新的apk文件
		if (!updateFile.exists()) {
			return;
		}

		try {
			Runtime.getRuntime().exec("chmod 0777  " + updateFile.getAbsolutePath().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 安装新的APK， 下载后用户直接安装
		Uri uri = Uri.fromFile(updateFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

}
