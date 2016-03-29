package com.changhong.gdappstore.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.fragment.TabFragment;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.net.LoadListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yves Yang on 2016/3/28.
 */
public class IndiaAppListPageAdapter extends PagerAdapter {
    static final int APP_COL = 4;

    Activity activity;
    List<ViewPager> viewPagers = new ArrayList<ViewPager>();
    List<Category> categories = new ArrayList<Category>();

    public IndiaAppListPageAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        final ViewPager viewPager = viewPagers.get(position);
        if (viewPager.getParent() != null){
            ((ViewGroup)viewPager.getParent()).removeView(viewPager);
        }

        container.addView(viewPager);

        initAppPage(viewPager, categories.get(position));

        return viewPager;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewPagers.get(position));
    }

    public IndiaAppListPageAdapter setData(List<ViewPager> viewPagers,List<Category> categories){
        if(viewPagers == null
                || categories == null)
            return this;
        this.viewPagers = viewPagers;
        this.categories = categories;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public int getCount() {
        return viewPagers.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    void initAppPage(final ViewPager viewPager,Category category){

        DataCenter.getInstance().loadAppsByCategoryId(activity, category.getId(), new LoadListener.LoadListListener() {
            @Override
            public void onComplete(List<Object> items) {
                if (items == null
                        || items.size() < 1)
                    return;

                ViewPageAdapter pageAdapter = (ViewPageAdapter) viewPager.getAdapter();

                List<App> apps = new ArrayList<App>();
                for (int i = 0; i < items.size(); i++) {

                    apps.add((App) items.get(i));
                    if ((i + 1) % 8 == 0) {
                        IndiaCategoryAdapter adapter = new IndiaCategoryAdapter(activity);
                        TabFragment fragment = TabFragment.newInstance(adapter, String.valueOf((i + 1) % 8), APP_COL);
                        pageAdapter.addItem(fragment);
                        adapter.setData(apps);
                        apps.clear();
                    }
                }
                IndiaCategoryAdapter adapter = new IndiaCategoryAdapter(activity);
                TabFragment fragment = TabFragment.newInstance(adapter, String.valueOf("last"), APP_COL);
                pageAdapter.addItem(fragment);
                adapter.setData(apps);
            }
        });

    }
}
