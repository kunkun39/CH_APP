package com.changhong.gdappstore.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
import com.changhong.gdappstore.util.InstallUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SilentInstallService extends Service {
	private final String TAG = "SilentInstallService";

	int silentInstallPos = 0;
	HttpUtils http = new HttpUtils(Config.CONNECTION_TIMEOUT);
	/** 静默安装和卸载列表 */
	List<List<Object>> lists = new ArrayList<List<Object>>();
	/** 静默安装列表 */
	List<Object> installApps = new ArrayList<Object>();
	/** 静默卸载列表 */
	List<Object> uninstallApps = new ArrayList<Object>();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		L.d(TAG + " oncreate");
		new Thread(new Runnable() {

			@Override
			public void run() {
				checkSilentUpdate();
			}
		}).start();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/** 下载回调函数 */
	RequestCallBack<File> requestCallBack = new RequestCallBack<File>() {

		@Override
		public void onStart() {
			L.d(TAG + " onstart load");
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {
			L.d(TAG + " onloading " + current + "  " + total);
		}

		@Override
		public void onSuccess(final ResponseInfo<File> responseInfo) {
			L.d(TAG + "---responseinfo  " + responseInfo.result.getPath());
			Util.chrome0777File(UpdateService.baseUpdatePath);
			Util.chrome0777File(responseInfo.result.getPath());
			new Thread(new Runnable() {

				@Override
				public void run() {
					int success = InstallUtil.installAppByCommond(responseInfo.result.getPath());
					L.d(TAG + " installsuccess is  " + success);
				}
			}).start();
			try {
				long t1 = System.currentTimeMillis();
				Thread.sleep(5000);
				silentInstallPos++;
				if (silentInstallPos >= installApps.size()) {
					stopSelf();
					return;
				}
				L.d(TAG + " time==" + (System.currentTimeMillis() - t1));
				App app = (App) installApps.get(silentInstallPos);
				String apkLoadUrl = app.getPosterFilePath();// 临时使用海报图片路径变量代替apk下载路径变量
				String apkname = apkLoadUrl.substring(apkLoadUrl.lastIndexOf("/") + 1, apkLoadUrl.length()).trim();
				http.download(apkLoadUrl, UpdateService.baseUpdatePath + "/" + apkname, true, true, requestCallBack);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			L.d(TAG + "---load onFailure  " + msg);
		}
	};

	private void checkSilentUpdate() {
		Util.deleteFileChildrens(UpdateService.baseUpdatePath);
		DataCenter.getInstance().loadSilentInstallData(getApplicationContext(), new LoadObjectListener() {

			@Override
			public void onComplete(Object object) {
				lists = (List<List<Object>>) object;
				installApps = lists.get(0);// 静默安装
				uninstallApps = lists.get(1);// 静默卸载

				for (int j = 0; j < uninstallApps.size(); j++) {
					if (uninstallApps.get(j) != null) {// 卸载
						InstallUtil.uninstallAppByCommond(((App) uninstallApps.get(j)).getPackageName());
						try {
							Thread.sleep(1000);// 避免卡顿
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

				if (installApps.size() <= 0) {
					L.d(TAG + " installApps size = 0");
					return;
				}
				for (int i = 0; i < installApps.size(); i++) {
					if (installApps.get(i) == null) {
						installApps.remove(i);// 排空
						i--;
					}
					// L.d(TAG+" install App i "+i+" "+installApps.get(i).toString());
				}
				silentInstallPos = 0;
				App app = (App) installApps.get(0);
				String apkLoadUrl = app.getPosterFilePath();// TODO
															// 临时使用海报图片路径变量代替apk下载路径变量
				String apkname = apkLoadUrl.substring(apkLoadUrl.lastIndexOf("/") + 1, apkLoadUrl.length()).trim();
				HttpHandler handler = http.download(apkLoadUrl, UpdateService.baseUpdatePath + "/" + apkname, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
						true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
						requestCallBack);

			}
		});

	}
}
