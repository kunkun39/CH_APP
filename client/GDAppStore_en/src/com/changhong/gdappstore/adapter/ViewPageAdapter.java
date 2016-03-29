package com.changhong.gdappstore.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.changhong.gdappstore.base.BaseFragment;
import com.changhong.gdappstore.fragment.RecycleViewFragment;
import com.changhong.gdappstore.fragment.TabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yves Yang on 2016/2/24.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {
    List<RecycleViewFragment> list = new ArrayList<RecycleViewFragment>();

    public ViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        RecycleViewFragment fragment = list.get(position);
        if(fragment instanceof TabFragment ){
            return((TabFragment)fragment).getName();
        }
        return null;
    }

    public void setData(){

    }

    public ViewPageAdapter addItem(TabFragment fragment){
        list.add(fragment);
        notifyDataSetChanged();
        return this;
    }
}
