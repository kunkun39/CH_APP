package com.changhong.gdappstore.activity;

import android.content.Intent;
import android.os.Bundle;

import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.net.LoadListener.LoadCompleteListener;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.L;
/**
 * 应用进入动画页面
 * @author wangxiufeng
 *
 */
public class LoadingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		loadData();
	}
	
	private void initView() {
		L.d("widthpx=="+context.getResources().getDisplayMetrics().density);
		L.d("widthpx=="+screenWidth+" "+screenHeight);
	}
	
	private void loadData() {
		DataCenter dataCenter=DataCenter.getInstance();
		dataCenter.loadCategoryAndPageData(context,new LoadCompleteListener() {
			
			@Override
			public void onComplete() {
				jumpToMain();
			}
		});
	}
	
	private void jumpToMain() {
		startActivity(new Intent(context, MainActivity.class));
		finish();
	}
}
