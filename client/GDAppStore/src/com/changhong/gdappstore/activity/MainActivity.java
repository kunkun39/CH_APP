package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.MainViewPagerAdapter;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.base.BasePageView;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.view.JingpinView;
import com.changhong.gdappstore.view.TitleView;
import com.changhong.gdappstore.view.ZhuanTiView;
import com.changhong.gdappstore.view.YouXiView;
import com.changhong.gdappstore.view.YuLeView;

/**
 * homepage
 * 
 * There are four tabs in this activity,so there are four item pages add to
 * ViewPager in this activity. Change the tab focus the item will be changed by
 * animation.
 * 
 * @author wangxiufeng
 * 
 */
public class MainActivity extends BaseActivity {
	/** 标签按钮view **/
	private TitleView titleView;
	/***/
	private ViewPager viewPager;
	/** viewpager适配器 */
	private MainViewPagerAdapter viewPagerAdapter;
	/** 每页view */
	private List<BasePageView> pageViews = new ArrayList<BasePageView>();
	/** 精品view */
	private JingpinView view_jingpin;
	/** 娱乐view */
	private YuLeView view_yule;
	/** 应用view */
	private ZhuanTiView view_yingyong;
	/** 游戏view */
	private YouXiView view_youxi;
	/** viewpager 翻页动画 */
	private Animation anim_rightin, anim_leftin;
	/** viewpager 当前选中标签页 */
	private int currIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	private void initView() {
		// init title view
		titleView = findView(R.id.titleview);
		titleView.setOnFocusChangeListener(titleFocusChangeListener);

		// init page views
		view_jingpin = new JingpinView(context);
		view_yingyong = new ZhuanTiView(context);
		view_youxi = new YouXiView(context);
		view_yule = new YuLeView(context);
		pageViews.add(view_jingpin);
		pageViews.add(view_yule);
		pageViews.add(view_yingyong);
		pageViews.add(view_youxi);
		// init view pager
		viewPager = findView(R.id.viewpager);
		viewPagerAdapter = new MainViewPagerAdapter(pageViews);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setAnimationCacheEnabled(true);
		viewPager.setOnPageChangeListener(pageChangeListener);
		initPageChangeAnimtion();

		titleView.getBtn_jingpin().requestFocus();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		// 目前是初始化默认数据
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

	private OnFocusChangeListener titleFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				if (v.getId() == R.id.bt_title_jingpin) {
					viewPager.setCurrentItem(0);
				} else if (v.getId() == R.id.bt_title_yule) {
					viewPager.setCurrentItem(1);
				} else if (v.getId() == R.id.bt_title_zhuanti) {
					viewPager.setCurrentItem(2);
				} else if (v.getId() == R.id.bt_title_youxi) {
					viewPager.setCurrentItem(3);
				}
			}
		}
	};

	/**
	 * 初始化动画
	 */
	private void initPageChangeAnimtion() {
		int halfscreen = screenWidth / 2;
		int duration = 300;
		anim_leftin = new TranslateAnimation(-halfscreen, 0, 0, 0);
		anim_rightin = new TranslateAnimation(halfscreen, 0, 0, 0);
		anim_leftin.setFillAfter(false);
		anim_rightin.setFillAfter(false);
		anim_leftin.setDuration(duration);
		anim_rightin.setDuration(duration);
	}

	/**
	 * 页卡切换监听
	 */
	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 < 0 || arg0 > pageViews.size()
					|| pageViews.get(arg0) == null) {
				return;
			}
			BasePageView curPageView = pageViews.get(arg0);
			switch (arg0) {
			case 0:// 选中精品标签
				titleView.setSelectedItemById(R.id.bt_title_jingpin);
				break;
			case 1:// 选中娱乐标签
				titleView.setSelectedItemById(R.id.bt_title_yule);
				break;
			case 2:// 选中应用标签
				titleView.setSelectedItemById(R.id.bt_title_zhuanti);
				break;
			case 3:// 选中游戏标签
				titleView.setSelectedItemById(R.id.bt_title_youxi);
				break;
			}
			if (!titleView.hasChildFocesed()) {// 非标签上面切换情况下，处理默认交代呢
				if (currIndex == arg0 - 1) {// 从左往右翻页
					if (pageViews.get(currIndex).currentFocuesId == R.id.jingping_item10) {
						curPageView.setFocuesItemByPosition(3);// 最底层一排翻页让第下一页最低层第一个获取焦点
					} else {
						curPageView.setFocuesItemByPosition(0);// 其它情况让第一个获取焦点
					}
				} else if (currIndex == arg0 + 1) {// 从右往左翻页
					if (pageViews.get(currIndex).currentFocuesId == R.id.jingping_item4) {
						curPageView.setFocuesItemByPosition(9);// 最底层一排翻页让第上一页最低层最后一个获取焦点
					} else {
						curPageView.setFocuesItemByPosition(12);// 其它情况让最后一列最上面一个获取焦点
					}
				}
			}
			if (currIndex == arg0 - 1) {
				curPageView.startAnimation(anim_rightin);// 从左往右翻页动画
			} else if (currIndex == arg0 + 1) {
				curPageView.startAnimation(anim_leftin);// 从右往左翻页动画
			}
			currIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
}
