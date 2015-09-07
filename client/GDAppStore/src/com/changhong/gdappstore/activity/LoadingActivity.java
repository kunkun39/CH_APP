package com.changhong.gdappstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.net.LoadListener.LoadCompleteListener;
import com.changhong.gdappstore.util.L;

/**
 * 应用进入动画页面
 * 
 * @author wangxiufeng
 * 
 */
public class LoadingActivity extends BaseActivity {
	/** 动画执行时间 */
	private final int AnimDuration = 3000;
	private ImageView ivLoading;
	private AlphaAnimation alphaAnimation;
	/** 动画是否加载完成 */
	private boolean animationEnd = false;
	/** 数据是否加载完成 */
	private boolean dataCompleted = false;

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
				animationEnd = true;
				if (dataCompleted) {
					jumpToMain();
				}
			}
		});
		ivLoading.startAnimation(alphaAnimation);
	}

	private void loadData() {
		DataCenter dataCenter = DataCenter.getInstance();
		dataCenter.loadCategoryAndPageData(context, new LoadCompleteListener() {

			@Override
			public void onComplete() {
				dataCompleted = true;
				if (animationEnd) {
					jumpToMain();
				}
			}
		});
	}

	private void jumpToMain() {
		startActivity(new Intent(context, MainActivity.class));
		finish();
	}
}
