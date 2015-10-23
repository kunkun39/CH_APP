package com.changhong.gdappstore.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.SharedPreferencesUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 应用进入动画页面
 * 
 * @author wangxiufeng
 * 
 */
public class LoadingActivity extends BaseActivity {
	/** 动画执行时间 */
	private final int AnimDuration = 2000;
	private ImageView ivLoading;
	private AlphaAnimation alphaAnimation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		initView();
		loadData();
	}

	private void initView() {
		L.d("widthpx==" + context.getResources().getDisplayMetrics().density);
		L.d("widthpx==" + screenWidth + " " + screenHeight);
		ivLoading = findView(R.id.iv_loading);
		alphaAnimation = new AlphaAnimation(0.4f, 1f);
		alphaAnimation.setDuration(AnimDuration);
		alphaAnimation.setFillAfter(true);
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
					jumpToMain();
			}
		});
		
	}

	private void loadData() {
		String imgurl=SharedPreferencesUtil.getJsonCache(context, Config.KEY_BOOTADIMG);
		L.d("imgurl==="+imgurl+" ");
		if (TextUtils.isEmpty(imgurl)) {
			ivLoading.startAnimation(alphaAnimation);
		}else {
			ImageLoadUtil.displayImgByonlyDiscCache(imgurl, ivLoading, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					
				}
				
				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					
				}
				
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					ivLoading.startAnimation(alphaAnimation);
				}
				
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					
				}
			});
		}
		DataCenter.getInstance().loadBootADData(context, null);
	}

	private void jumpToMain() {
		startActivity(new Intent(context, MainActivity.class));
		finish();
	}
}
