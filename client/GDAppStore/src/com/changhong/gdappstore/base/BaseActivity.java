package com.changhong.gdappstore.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class BaseActivity extends Activity {

	protected Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
	}
	/**
	 * instead of findViewById(id)，减少每次都要去强制转换的麻烦
	 * @param id
	 * @return 
	 */
	protected <T>T findView(int id) {
		return (T)findViewById(id);
	}

}
