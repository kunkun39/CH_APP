package com.changhong.gdappstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseRelativeLayout;
import com.changhong.gdappstore.model.PostItemModel;
import com.changhong.gdappstore.util.L;

/**
 * 精品view
 * 
 * @author wangxiufeng
 * 
 */
public class JingpinView extends BaseRelativeLayout implements
		OnFocusChangeListener, OnClickListener {
	/** 推荐位ID */
	private int[] itemIds = { R.id.jingping_item1, R.id.jingping_item2,
			R.id.jingping_item3, R.id.jingping_item4, R.id.jingping_item5,
			R.id.jingping_item6, R.id.jingping_item7, R.id.jingping_item8,
			R.id.jingping_item9, R.id.jingping_item10, R.id.jingping_item11,
			R.id.jingping_item12, R.id.jingping_item13 };
	/** 推荐位个数 */
	private final int itemCount = 13;
	/** 推荐位view */
	private PostItemView[] itemViews = new PostItemView[itemCount];
	/** 外部回调点击监听器 */
	private OnClickListener onClickListener;
	/** 外部回调焦点监听器 */
	private OnFocusChangeListener onFocusChangeListener;
	/** 焦点框view */
	private ImageView ivFocues;

	private Animation animationbig, animationsmall;

	public boolean isRightItemFocused = false;

	public JingpinView(Context context) {
		super(context);
		initView();
	}

	public JingpinView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public JingpinView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	protected void initView() {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.view_jingpin, this);
		ivFocues = findView(R.id.iv_jingpin_focues);
		ivFocues.setVisibility(INVISIBLE);
		for (int i = 0; i < itemCount; i++) {
			itemViews[i] = findView(itemIds[i]);
			itemViews[i].setFocusable(true);
			itemViews[i].setClickable(true);
			itemViews[i].setOnFocusChangeListener(this);
			itemViews[i].setOnClickListener(this);
		}
		setNextFocuesUpId(R.id.bt_title_jingpin);
		animationbig = AnimationUtils.loadAnimation(context, R.anim.scale_big);
		animationsmall = AnimationUtils.loadAnimation(context,
				R.anim.scale_small);
		//最左一排不能再按左。
		itemViews[0].setNextFocusLeftId(R.id.jingping_item1);
		itemViews[1].setNextFocusLeftId(R.id.jingping_item2);
		itemViews[2].setNextFocusLeftId(R.id.jingping_item3);
		itemViews[3].setNextFocusLeftId(R.id.jingping_item4);
	}

	public void initData() {
		itemViews[0].setData(new PostItemModel(true,
				R.drawable.icon_jingpin_search, "搜索"));
		itemViews[1].setData(new PostItemModel(true,
				R.drawable.icon_jingpin_ranklist, "排行榜"));
		itemViews[2].setData(new PostItemModel(true,
				R.drawable.icon_jingpin_subject, "热门专题"));
		itemViews[3].setData(new PostItemModel(true,
				R.drawable.icon_jingpin_necessary, "装机必备"));
		itemViews[11].setData(new PostItemModel(false, R.drawable.img_post2, "海报2名字"));
		itemViews[12].setData(new PostItemModel(false, R.drawable.img_post3, "海报3名字"));
		for (int i = 4; i < 10; i++) {
			itemViews[i].setData(new PostItemModel(true,
					R.drawable.img_post1+i%3, "应用名字"));
		}
	}

	/**
	 * 最上一排海报往上按默认焦点
	 * 
	 * @param id
	 */
	public void setNextFocuesUpId(int id) {
		itemViews[0].setNextFocusUpId(id);
		itemViews[10].setNextFocusUpId(id);
		itemViews[11].setNextFocusUpId(id);
		itemViews[12].setNextFocusUpId(id);
	}

	public void setFocuesItem(int position) {
		if (itemViews != null && position >= 0 && position < itemViews.length
				&& itemViews[position] != null) {
			itemViews[position].requestFocus();
		}
	}

	@Override
	public void onClick(View v) {

		if (onClickListener != null) {
			onClickListener.onClick(v);
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			int viewId = v.getId();
			if (viewId == R.id.jingping_item13
					|| viewId == R.id.jingping_item10) {
				isRightItemFocused = true;
			} else {
				isRightItemFocused = false;
			}
			RelativeLayout.LayoutParams mlayout = new RelativeLayout.LayoutParams(
					100, 100);
			// RelativeLayout.LayoutParams tmplayout = (LayoutParams) v
			// .getLayoutParams();
			RelativeLayout.LayoutParams tmplayout = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			tmplayout.leftMargin = v.getLeft();
			tmplayout.topMargin = v.getTop();
			tmplayout.width = v.getWidth();
			tmplayout.height = v.getHeight();
			if (viewId == R.id.jingping_item11
					|| viewId == R.id.jingping_item12
					|| viewId == R.id.jingping_item13) {
				// 大海报
				mlayout.leftMargin = tmplayout.leftMargin - 9 - tmplayout.width
						/ 20;
				mlayout.topMargin = tmplayout.topMargin - 7 - tmplayout.height
						/ 20;
				mlayout.width = tmplayout.width + 14 + (tmplayout.width / 10);
				mlayout.height = tmplayout.height + 8 + (tmplayout.height / 10);
			} else {
				mlayout.leftMargin = tmplayout.leftMargin - 18
						- tmplayout.width / 20;
				mlayout.topMargin = tmplayout.topMargin - 18 - tmplayout.height
						/ 20;
				mlayout.width = tmplayout.width + 30 + (tmplayout.width / 10);
				mlayout.height = tmplayout.height + 30
						+ (tmplayout.height / 10);
			}
			ivFocues.setBackgroundResource(R.drawable.focues_post);
			ivFocues.setLayoutParams(mlayout);
			ivFocues.setVisibility(View.VISIBLE);

			v.startAnimation(animationbig);
			ivFocues.startAnimation(animationbig);
			v.bringToFront();
			ivFocues.bringToFront();

		} else {
			v.startAnimation(animationsmall);
			ivFocues.startAnimation(animationsmall);
			v.clearAnimation();
			ivFocues.clearAnimation();
			ivFocues.setVisibility(View.INVISIBLE);
		}
		if (onFocusChangeListener != null) {
			onFocusChangeListener.onFocusChange(v, hasFocus);
		}
	}

	public boolean isRightItemFocused() {
		return isRightItemFocused;
	}

}
