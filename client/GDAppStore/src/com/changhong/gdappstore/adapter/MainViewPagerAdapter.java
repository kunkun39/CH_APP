package com.changhong.gdappstore.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.changhong.gdappstore.base.BasePageView;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.view.HomePageView;
/**
 * 首页viewpageradapter
 * @author wangxiufeng
 *
 */
public class MainViewPagerAdapter extends PagerAdapter {

	private List<BasePageView> pageViews = new ArrayList<BasePageView>();

	public MainViewPagerAdapter() {
	}

	public MainViewPagerAdapter(List<BasePageView> pageViews) {
		setPageViews(pageViews);
	}

	public void updateList(List<BasePageView> pageViews) {
		setPageViews(pageViews);
	}
	
	private void setPageViews(List<BasePageView> pageViews) {
		if (pageViews==null || pageViews.size()==0) {
			this.pageViews=pageViews;
			notifyDataSetChanged();
			return;
		}
		this.pageViews=new ArrayList<BasePageView>(pageViews);
		for (int i = 0; i < this.pageViews.size(); i++) {
			if (this.pageViews.get(i)==null) {
				L.d("MainViewPagerAdapter there is a null pageView pos is "+i+" size is "+pageViews.size() );
				this.pageViews.remove(i);
				i--;
			}
		}
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
