package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ViewFlipper;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.MainViewPagerAdapter;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.view.JingpinView;
import com.changhong.gdappstore.view.TitleView;
import com.changhong.gdappstore.view.YingYongView;
import com.changhong.gdappstore.view.YouXiView;
import com.changhong.gdappstore.view.YuLeView;

/**
 * 主页面
 * 
 * @author wangxiufeng
 * 
 */
public class MainActivity extends BaseActivity {
	/** 标签按钮view **/
	private TitleView titleView;
	/***/
	private ViewPager viewflipper;
	/**viewpager适配器*/
	private MainViewPagerAdapter viewPagerAdapter;
	/**每页view*/
	private List<View> pageViews=new ArrayList<View>();
	/** 精品view */
	private JingpinView view_jingpin;
	/** 娱乐view */
	private YuLeView view_yule;
	/** 应用view */
	private YingYongView view_yingyong;
	/** 游戏view */
	private YouXiView view_youxi;
	/*** Animation is running? */
	boolean b_AnimationIsRun = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	private void initView() {
		titleView = findView(R.id.titleview);
		viewflipper = findView(R.id.viewflipper);
		view_jingpin = new JingpinView(context);
		view_yingyong = new YingYongView(context);
		view_youxi = new YouXiView(context);
		view_yule = new YuLeView(context);
		pageViews.add(view_jingpin);
		pageViews.add(view_yule);
		pageViews.add(view_yingyong);
		pageViews.add(view_youxi);
		viewPagerAdapter=new MainViewPagerAdapter(pageViews);
		viewflipper.setAdapter(viewPagerAdapter);
		viewflipper.setCurrentItem(0);
	}

	private void initData() {
		view_jingpin.initData();
		view_yingyong.initData();
		view_youxi.initData();
		view_yule.initData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			break;
		case KeyEvent.KEYCODE_BACK:

			break;
		}
		return super.onKeyDown(keyCode, event);
	}


	private void slide_left() {

	}

	private void slide_right() {


	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
