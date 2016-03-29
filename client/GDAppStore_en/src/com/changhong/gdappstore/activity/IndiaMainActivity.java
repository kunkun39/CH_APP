package com.changhong.gdappstore.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.DrawableUtils;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.MyApplication;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.IndiaAppListPageAdapter;
import com.changhong.gdappstore.adapter.IndiaBannerPageAdapter;
import com.changhong.gdappstore.adapter.IndiaCategoryAdapter;
import com.changhong.gdappstore.adapter.MainViewPagerAdapter;
import com.changhong.gdappstore.adapter.ViewHolder;
import com.changhong.gdappstore.adapter.ViewPageAdapter;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.fragment.RecycleViewFragment;
import com.changhong.gdappstore.fragment.TabFragment;
import com.changhong.gdappstore.holder.IndiaAppItemHolder;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.HomePagePoster;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.net.LoadListener;
import com.changhong.gdappstore.util.DateUtils;
import com.changhong.gdappstore.util.Executor;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.Util;
import com.changhong.gdappstore.util.ViewPagerTransformer;
import com.changhong.gdappstore.view.FrontViewViewPager;
import com.changhong.gdappstore.view.ViewPagerFoucusChildPrority;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Yves Yang on 2016/3/17.
 */
public class IndiaMainActivity  extends AppCompatActivity implements RecycleViewFragment.OnFragmentInteractionListener {
    final static int BANNER_CHANGE_DELAY = 3000;
    public static final int BANNER_PIC_COUNT = 3;

    FrontViewViewPager banner;
    ViewPager viewPager;
    TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_india);
        initView();
        initData();
    }

    void initView(){
        banner = findView(R.id.banner);
        banner.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                ViewPagerTransformer.scaleView(IndiaMainActivity.this,page);
            }
        });
        banner.setAdapter(new IndiaBannerPageAdapter(this));
        banner.setOffscreenPageLimit(BANNER_PIC_COUNT);
        banner.disableDrawingOrder();

        banner.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT: {
                        int index = banner.getCurrentItem() - 1;
                        if (index > 0) {
                            banner.setOrders(index);
                        }

                    }
                    break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT: {
                        int index = banner.getCurrentItem() + 1;
                        if (index < banner.getChildCount()) {
                            banner.setOrders(index);
                        }

                    }
                    break;
                }

                return false;
            }
        });

        banner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state){
                    case ViewPager.SCROLL_STATE_IDLE:{
                        int count = banner.getChildCount();
                        if ( count > BANNER_PIC_COUNT) { //多于1，才会循环跳转
                            if ( banner.getCurrentItem() >= (count - BANNER_PIC_COUNT)) { //末位之后，跳转到首位（1）
                                banner.setCurrentItem(0,false); //false:不显示跳转过程的动画
                            }
                        }
                    }break;
                }
            }

        });

        viewPager = findView(R.id.viewpager);
        IndiaAppListPageAdapter pagerAdapter = new IndiaAppListPageAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        tabs = findView(R.id.tabs);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        runBanner();
    }

    void runBanner(){
        Executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(BANNER_CHANGE_DELAY);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nextPoster();
                        }
                    });
                }
            }
        });
    }

    void nextPoster(){
        if (banner == null)
            return;

        int current = banner.getCurrentItem();
        current += 1;
        banner.setCurrentItem(current);
    }

    void initData(){
        Executor.execute(new Runnable() {
            @Override
            public void run() {
                DataCenter.getInstance().loadCategories(getBaseContext(), new LoadListener.LoadObjectListener() {
                    @Override
                    public void onComplete(Object object) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initViewData();
                            }
                        });
                    }
                });
                DataCenter.getInstance().loadHomePagePoster(getBaseContext(), new LoadListener.LoadObjectListener() {
                    @Override
                    public void onComplete(final Object object) {
                        if (object == null)
                            return;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initBannerData((List<HomePagePoster>) object);
                            }
                        });
                    }
                });
            }
        });
    }

    void initViewData(){
        List<Category> categories = DataCenter.getInstance().getCategories();
        List<ViewPager> viewPagers = new ArrayList<ViewPager>();
        for(int i = 0;i < 7; i ++){
            Category category = categories.get(i);

            // 线性布局
            LinearLayout linearLayout = new LinearLayout(getBaseContext());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            TextView textView = new TextView(getBaseContext());

            //左边为文本内容
            textView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
            textView.setText(category.getName());
            textView.setTextColor(getResources().getColor(R.color.Grey_500));
            textView.setGravity(Gravity.CENTER);
            linearLayout.addView(textView);

//            //除了右边最后一个，其他的都在右边加上竖线
//            if (i != (categories.size() - 1)){
//                View view = new View(getBaseContext());
//
//                RelativeLayout.MarginLayoutParams params = new RelativeLayout.MarginLayoutParams(Util.dipTopx(getBaseContext(), 1.0f), ViewGroup.LayoutParams.MATCH_PARENT);
//                params.setMargins(0,
//                        Util.dipTopx(getBaseContext(), 4.0f),
//                        0,
//                        Util.dipTopx(getBaseContext(), 16.0f)
//                );
//                view.setLayoutParams(params);
//                view.setBackgroundResource(R.color.Grey_300);
//                linearLayout.addView(view);
//            }

            // 设置好布局后，把布局添加进view
            tabs.addTab(tabs.newTab().setCustomView(linearLayout));
            ViewPagerFoucusChildPrority viewPager = new ViewPagerFoucusChildPrority(getBaseContext());
            viewPager.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            viewPager.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    ViewPager viewpager = (ViewPager)v;
                    View childView = viewpager.getChildAt(viewpager.getCurrentItem());
                    if (childView != null
                        && childView.onKeyDown(keyCode,event)){
                        return true;
                    }else {
                        return false;
                    }

                }
            });
            viewPager.setId(i);
            viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager()));
            viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            viewPagers.add(viewPager);
        }
        ((IndiaAppListPageAdapter)viewPager.getAdapter()).setData(viewPagers, categories);
    }

    void initBannerData(List<HomePagePoster> posters){
        if (posters == null)
            return;

        ((IndiaBannerPageAdapter)banner.getAdapter()).setData(posters);
    }

    <T>T findView(int id){
        return (T)super.findViewById(id);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        ;
    }
}


