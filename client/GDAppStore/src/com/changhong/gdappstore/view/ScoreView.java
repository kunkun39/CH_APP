package com.changhong.gdappstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseRelativeLayout;
import com.changhong.gdappstore.util.L;
/**
 * 频分星星显示view，可根据自己需要扩展。
 * @author wangxiufeng
 *
 */
public class ScoreView extends BaseRelativeLayout {
	private LinearLayout ll_scrollview;

	public ScoreView(Context context) {
		super(context);
		initView();
	}

	public ScoreView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ScoreView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public void initView() {
		ll_scrollview = new LinearLayout(context);
		ll_scrollview.setOrientation(LinearLayout.HORIZONTAL);
		addView(ll_scrollview);
	}

	/**
	 * 按照5颗星5分来计算，1分为半颗星
	 * 
	 * @param score
	 */
	public void setStarsBy5Total(float score) {
		if (score < 0) {
			return;
		}
		int fullstar = (int) Math.floor(score);
		float decimal = score - fullstar;
		L.d("score---floor==" + Math.floor(score) + " " + decimal);
		ll_scrollview.removeAllViews();
		for (int i = 0; i < 5; i++) {
			if ((i + 1) <= fullstar) {
				ll_scrollview.addView(getImageView(2));
			} else if (decimal > 0 && i == fullstar) {
				ll_scrollview.addView(getImageView(1));
			} else {
				ll_scrollview.addView(getImageView(0));
			}

		}
		ll_scrollview.invalidate();
	}

	/**
	 * 获取imageView对象
	 * 
	 * @param state
	 *            状态1为半颗星，2为满颗星，3为空心星
	 * @return
	 */
	private ImageView getImageView(int state) {
		ImageView imageView = new ImageView(context);
		if (state == 1) {
			imageView.setImageResource(R.drawable.star_half);
		} else if (state == 2) {
			imageView.setImageResource(R.drawable.star_full);
		} else {
			imageView.setImageResource(R.drawable.star_empty);
		}
		return imageView;
	}

}
