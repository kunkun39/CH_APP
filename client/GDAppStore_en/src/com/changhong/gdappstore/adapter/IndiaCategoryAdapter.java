package com.changhong.gdappstore.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.activity.DetailActivity;
import com.changhong.gdappstore.fragment.RecycleViewFragment;
import com.changhong.gdappstore.holder.IndiaAppItemHolder;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.NativeApp;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yves Yang on 2016/3/28.
 */
public class IndiaCategoryAdapter extends RecycleViewFragment.RecycleViewAdapter<IndiaAppItemHolder>{
    List<App> apps = new ArrayList<App>();
    Activity activity;

    public IndiaCategoryAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setData(List<App> apps){
        this.apps.clear();
        this.apps.addAll(apps);
        notifyDataSetChanged();
    }

    @Override
    public IndiaAppItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        return new IndiaAppItemHolder(activity, LayoutInflater.from(activity).inflate(R.layout.app_item,parent,false));
    }

    @Override
    public void onBindViewHolder(IndiaAppItemHolder holder, int position) {
        if (apps.size() == 0)
            return;
        int config = 0;
        final App pageApp = apps.get(position);
        if (pageApp.getAppname() != null){
            NativeApp nativeApp = Util.getNativeApp(activity, pageApp.getAppname());
            config |= (nativeApp != null) ? IndiaAppItemHolder.FLAG_INSTALLED : 0;
        }
        setText(holder.name,pageApp.getAppname());
        setText(holder.category,pageApp.getSubtitle());
        if(pageApp.getIconFilePath() != null){
            ImageLoadUtil.displayImgByMemoryDiscCache(pageApp.getIconFilePath(), holder.icon);
        }

        if (pageApp.getTime() != null){
            try {
                if(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(pageApp.getTime()).getTime()
                        - System.currentTimeMillis()
                        < Config.NEW_TIME) {
                    config |= IndiaAppItemHolder.FLAG_NEW;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        holder.setStatus(config);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailActivity.class);
                intent.putExtra(Config.KEY_APPID, pageApp.getAppid());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    void setText(TextView v,String string){
        if (string != null){
            v.setText(string);
        }

    }
}
