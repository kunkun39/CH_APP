package com.changhong.gdappstore.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.changhong.gdappstore.view.HomePageView;
/**
 * 首页viewpageradapter
 * @author wangxiufeng
 *
 */
public class MainViewPagerAdapter extends PagerAdapter {

	private List<HomePageView> pageViews = new ArrayList<HomePageView>();

	public MainViewPagerAdapter() {
	}

	public MainViewPagerAdapter(List<HomePageView> pageViews) {
		this.pageViews = pageViews;
	}

	public void updateList(List<HomePageView> pageViews) {
		this.pageViews = pageViews;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(pageViews.get(arg1));
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		ViewGroup v = (ViewGroup) pageViews.get(arg1).getParent();
        if (v != null) {
            v.removeView(pageViews.get(arg1));
        } 
		((ViewPager) arg0).addView(pageViews.get(arg1), 0);
		return pageViews.get(arg1);
	}

}
