package com.changhong.gdappstore.base;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.view.PostItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
/**
 * 基类pageView
 * @author wangxiufeng
 *
 */
public class BasePageView extends BaseRelativeLayout {

	/** 推荐位ID */
	protected int[] itemIds = { R.id.jingping_item1, R.id.jingping_item2,
			R.id.jingping_item3, R.id.jingping_item4, R.id.jingping_item5,
			R.id.jingping_item6, R.id.jingping_item7, R.id.jingping_item8,
			R.id.jingping_item9, R.id.jingping_item10, R.id.jingping_item11,
			R.id.jingping_item12, R.id.jingping_item13 };
	/** 推荐位个数 */
	protected  int itemCount = 13;
	/** 推荐位view */
	protected PostItemView[] itemViews = new PostItemView[itemCount];
	/** 焦点缩放动画 */
	protected Animation animationbig, animationsmall;
	/**当前焦点位置*/
	public int currentFocuesId;

	public BasePageView(Context context) {
		super(context);
		initBaseView();
	}

	public BasePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initBaseView();
	}

	public BasePageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initBaseView();
	}
	
	private void initBaseView() {
		animationbig = AnimationUtils.loadAnimation(context, R.anim.scale_big);
		animationsmall = AnimationUtils.loadAnimation(context,
				R.anim.scale_small);
	}
	/**
	 * set the item view in itemViews request focus by position .
	 * @param position
	 */
	public void setFocuesItemByPosition(int position) {
		if (itemViews != null && position >= 0 && position < itemViews.length
				&& itemViews[position] != null) {
			itemViews[position].requestFocus();
		}
	}

}
