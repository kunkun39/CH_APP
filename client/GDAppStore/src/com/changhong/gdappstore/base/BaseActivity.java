package com.changhong.gdappstore.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class BaseActivity extends Activity {

	protected Context context;

	protected int screenWidth, screenHeight;

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

}
