package com.changhong.gdappstore.base;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.util.DialogUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 基类activity
 * 
 * @author wangxiufeng
 * 
 */
public class BaseActivity extends Activity {

	protected Context context;
	/** 频幕宽高 */
	protected int screenWidth, screenHeight;
	/** View.VISIBLE */
	protected static final int VISIBLE = View.VISIBLE;
	/** View.INVISIBLE */
	protected static final int INVISIBLE = View.INVISIBLE;
	/** View.GONE */
	protected static final int GONE = View.GONE;

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

}
