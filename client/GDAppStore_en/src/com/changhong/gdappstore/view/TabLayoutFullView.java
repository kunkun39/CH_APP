package com.changhong.gdappstore.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Yves Yang on 2016/3/30.
 */
public class TabLayoutFullView extends TabLayout{
    LinearLayout mTabStrip;
    Paint mPaint;
    int mSplitLineWidth = 0;
    public TabLayoutFullView(Context context) {
        this(context, null);
    }

    public TabLayoutFullView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayoutFullView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float percent = getMeasuredWidth() / (float)mTabStrip.getMeasuredWidth();
        for (int i = 0 ; i < getTabCount(); i ++){
            Tab tab = getTabAt(i);
            Class<?> tabClass = tab.getClass();
            Method method = null;
            TextView v = null;
            try {
                method = tabClass.getDeclaredMethod("getCustomView",null);
                method.setAccessible(true);
                if (method != null){
                    v = (TextView)method.invoke(tab);
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (v == null)
                return;
            int paddingLeft = (int)(v.getPaddingLeft() * percent);
            int paddingRight = (int)(v.getPaddingRight() * percent);
            int textSize = (int)(v.getTextSize() * percent);
            v.setTextSize(textSize);
            v.setPadding(paddingLeft,v.getPaddingTop(),paddingRight,v.getPaddingBottom());
        }
    }

    public static Method getMethod(Class clazz, String methodName,
                                   final Class[] classes) throws Exception {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, classes);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName,
                            classes);
                }
            }
        }
        return method;
    }
    void init(){
        mTabStrip = (LinearLayout)getChildAt(0);
        mTabStrip.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        mPaint = new Paint();
        mPaint.setColor(getContext().getResources().getColor(R.color.Grey_500));
        mSplitLineWidth = 1;//Util.dipTopx(getContext(),1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = mTabStrip.getChildCount();
        int leftPadding = ((View)mTabStrip.getParent()).getLeft() + getScrollX();

        for (int i = 0; i < (count - 1) ; i ++){
            Rect rect = new Rect();
            Point point = new Point();
            mTabStrip.getChildAt(i).getGlobalVisibleRect(rect);
            getChildVisibleRect(mTabStrip.getChildAt(i), rect, point);
            if (rect.right - leftPadding > 0
                    && rect.right - leftPadding < canvas.getClipBounds().right){
                canvas.drawRect(new Rect(rect.right - mSplitLineWidth - leftPadding,4,rect.right - leftPadding,rect.height() - 4),mPaint);
            }

        }
    }
}
