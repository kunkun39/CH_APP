package com.changhong.gdappstore.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.service.UpdateService;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SynchronousActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronous);
		initView();
		initData();
	}

	private void initView() {
		findViewById(R.id.rl_backup).setOnClickListener(this);
		findViewById(R.id.rl_recover).setOnClickListener(this);
		findViewById(R.id.rl_manage).setOnClickListener(this);
		findViewById(R.id.rl_backup).requestFocus();
	}
	
	private void initData() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_backup:
			startActivity(new Intent(this,SynchBackUpActivity.class));
			break;
		case R.id.rl_recover:
			startActivity(new Intent(this,SynchRecoverActivity.class));
			break;
		case R.id.rl_manage:
			startActivity(new Intent(this,SynchManageActivity.class));
			break;

		default:
			break;
		}
	}

}
