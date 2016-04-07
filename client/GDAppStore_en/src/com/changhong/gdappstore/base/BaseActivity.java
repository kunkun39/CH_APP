package com.changhong.gdappstore.base;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.net.LoadListener;
import com.changhong.gdappstore.service.NetChangeReceiver;
import com.changhong.gdappstore.service.SilentInstallService;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 基类activity
 * 
 * @author wangxiufeng
 * 
 */
public class BaseActivity extends AppCompatActivity {

	protected Context context;
	/** 频幕宽高 */
	protected int screenWidth, screenHeight;
	/** View.VISIBLE */
	protected static final int VISIBLE = View.VISIBLE;
	/** View.INVISIBLE */
	protected static final int INVISIBLE = View.INVISIBLE;
	/** View.GONE */
	protected static final int GONE = View.GONE;

	/** 静默安装Server是否已经启动 */
	private static boolean isSilentInstallServiceStart = false;

	protected ProgressDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		init();
	}

	private void init() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;// 获取分辨率宽度
		screenHeight = dm.heightPixels;

	}

	/**
	 * instead of findViewById(id)，减少每次都要去强制转换的麻烦
	 * 
	 * @param id
	 * @return
	 */
	protected <T> T findView(int id) {
		return (T) findViewById(id);
	}

	protected void showLoadingDialog() {
		if (loadingDialog == null) {
			loadingDialog = DialogUtil.showCirculProDialog(context, context.getString(R.string.tishi),
					context.getString(R.string.dataloading), false);
		}
		loadingDialog.show();
	}

	protected void dismissLoadingDialog() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}

	/**
	 * 静默安装
	 */
	protected void startSilentInstallService() {
		// 启动静默安装
		if (!isSilentInstallServiceStart) {
			startService(new Intent(BaseActivity.this, SilentInstallService.class));
			isSilentInstallServiceStart = true;
		}
	}

}
