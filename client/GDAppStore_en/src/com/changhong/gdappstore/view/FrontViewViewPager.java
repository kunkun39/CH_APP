package com.changhong.gdappstore.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Yves Yang on 2016/3/24.
 */
public class FrontViewViewPager extends ViewPager {
    Map<Integer,Integer> orders = new HashMap<Integer,Integer>();
    PagerAdapter adapter;

    public FrontViewViewPager(Context context) {
        super(context);
    }

    public FrontViewViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setViewToFront(View view){

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;

        adapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        //return super.getChildDrawingOrder(childCount,i);
        Log.d("Index",":" + i + orders.get(i));
        return orders.get(i) == null ? i:orders.get(i);
    }

    public void setOrders(int index){
        if(index <=0 && getChildCount() <= index){
            return;
        }

        orders.clear();
        Map<Integer,Integer> pair = new HashMap<Integer,Integer>();
        for(int i = 0; i < getChildCount(); i ++ ){
            pair.put(i,Integer.parseInt((String)getChildAt(i).getTag()));
        }
        int i = 0;
        if (index - 1 >= 0){
            orders.put(pair.get(i++),index - 1);
        }
        if (index + 1 < getChildCount()){
            orders.put(pair.get(i ++),index + 1);
        }

        for(int j = 0;i < (adapter.getCount() - 1); j ++){

            if (j == index
                    ||  j  == (index - 1)
                    || j == (index + 1)){
                continue;
            }
            orders.put(pair.get(i ++),j);
        }
        orders.put(pair.get(adapter.getCount() - 1),index);
        Log.d("MAP:",orders.toString());
    }

    @Override
    protected void setChildrenDrawingOrderEnabled(boolean enabled) {
        super.setChildrenDrawingOrderEnabled(false);
    }

    public void disableDrawingOrder(){
        setChildrenDrawingOrderEnabled(false);
    }

    DataSetObserver dataSetObserver = new DataSetObserver(){
        @Override
        public void onChanged() {
            super.onChanged();
            setOrders(1);
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            setOrders(1);
        }
    };
}
