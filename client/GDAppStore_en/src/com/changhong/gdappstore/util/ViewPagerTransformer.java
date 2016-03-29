package com.changhong.gdappstore.util;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.changhong.gdappstore.R;

/**
 * Created by Yves Yang on 2016/3/28.
 */
public class ViewPagerTransformer {

    final static float MAX_SCALE = 1.4f;
    final static float BASE_SCALE = 1f;
    final static float SCALE_FACTOR = 1;
    final static float SCALE_BEGIN = 0f;
    final static float SCALE_END = 1f;

    public static void scaleView(Context context,View page){
        if (page == null ||
                page.getParent() == null)
            return;

        Rect rect = new Rect();
        Rect windoowRect = new Rect();
        page.getGlobalVisibleRect(rect);
        int x = rect.centerX();
        ((ViewGroup)page.getParent()).getLocalVisibleRect(windoowRect);
        int winWidth = windoowRect.width();
        int winHalfWidth = winWidth >> 1;
        int winQuterWidth = winHalfWidth >> 1;
        float factor = 0;
        if (x > winHalfWidth
                && x < winHalfWidth + winQuterWidth){
            factor = ( winWidth - x - winQuterWidth) / (float)winQuterWidth;
        }else if (x > winQuterWidth && x <= winHalfWidth){
            factor = (x - winQuterWidth) / (float)winQuterWidth;
        }

        float scaleFactor =  1;

        if ( x >  winQuterWidth
                && x < (winWidth - winQuterWidth)){
            scaleFactor = BASE_SCALE + (factor * ((MAX_SCALE - BASE_SCALE) * SCALE_FACTOR));
            page.setScaleY(scaleFactor);
            page.bringToFront();
            ((ImageView)page).clearColorFilter();
        }else{
            scaleFactor = BASE_SCALE;
            page.setScaleY(0.8f);
            setFakeImage(context,(ImageView)page);
        }
        page.setScaleX(scaleFactor);


       L.d("POSITION:" + page.getTag().toString() + ":" + x + ":" + winWidth + ":" + scaleFactor);
    }

    static void setFakeImage(Context context,ImageView v){
        if (v == null)
            return;
        v.setColorFilter(context.getResources().getColor(R.color.black_half20), PorterDuff.Mode.DARKEN);
    }
}
