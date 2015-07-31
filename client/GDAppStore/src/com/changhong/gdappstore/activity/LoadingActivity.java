package com.changhong.gdappstore.activity;

import android.content.Intent;
import android.os.Bundle;

import com.changhong.gdappstore.base.BaseActivity;

public class LoadingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		jumpToMain();
	}
	
	private void initView() {
		
	}
	
	private void jumpToMain() {
		startActivity(new Intent(context, MainActivity.class));
		finish();
	}
}
