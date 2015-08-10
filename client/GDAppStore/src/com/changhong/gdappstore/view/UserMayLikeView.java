package com.changhong.gdappstore.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BasePageView;
import com.changhong.gdappstore.model.MainPostItemModel;
import com.changhong.gdappstore.util.L;

public class UserMayLikeView extends BasePageView implements OnFocusChangeListener, OnClickListener {
	/** 外部回调点击监听器 */
	private OnClickListener onClickListener;
	/** 外部回调焦点监听器 */
	private OnFocusChangeListener onFocusChangeListener;
	/** 焦点框view */
	private ImageView ivFocues;

	public UserMayLikeView(Context context) {
		super(context);
		initView();
	}

	public UserMayLikeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public UserMayLikeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	protected void initView() {
		itemCount = 8;
		itemIds = new int[itemCount];
		itemIds[0] = R.id.usermaylike_item0;
		itemIds[1] = R.id.usermaylike_item1;
		itemIds[2] = R.id.usermaylike_item2;
		itemIds[3] = R.id.usermaylike_item3;
		itemIds[4] = R.id.usermaylike_item4;
		itemIds[5] = R.id.usermaylike_item5;
		itemIds[6] = R.id.usermaylike_item6;
		itemIds[7] = R.id.usermaylike_item7;
		itemViews = new PostItemView[itemCount];
		View rootView = LayoutInflater.from(context).inflate(R.layout.view_usermaylike, this);
		ivFocues = findView(R.id.iv_detail_focues);
		ivFocues.setVisibility(INVISIBLE);
		for (int i = 0; i < itemCount; i++) {
			itemViews[i] = findView(itemIds[i]);
			itemViews[i].setFocusable(true);
			itemViews[i].setClickable(true);
			itemViews[i].setOnFocusChangeListener(this);
			itemViews[i].setOnClickListener(this);
		}
	}

	public void initData() {
		for (int i = 0; i < itemCount; i++) {
			itemViews[i].setData(new MainPostItemModel(true,
					R.drawable.img_post1+i%3, "应用名字"));
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
		if (onFocusChangeListener != null) {
			onFocusChangeListener.onFocusChange(v, hasFocus);
		}
		if (hasFocus) {
			L.d(TAG + "jingpinview onfocueschange " + v.getId());
			int viewId = v.getId();
			currentFocuesId = v.getId();
			RelativeLayout.LayoutParams mlayout = new RelativeLayout.LayoutParams(100, 100);
			// RelativeLayout.LayoutParams tmplayout = (LayoutParams) v
			// .getLayoutParams();
			RelativeLayout.LayoutParams tmplayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			tmplayout.leftMargin = v.getLeft();
			tmplayout.topMargin = v.getTop();
			tmplayout.width = v.getWidth();
			tmplayout.height = v.getHeight();
			mlayout.leftMargin = tmplayout.leftMargin - 15 - tmplayout.width / 20;
			mlayout.topMargin = tmplayout.topMargin - 15 - tmplayout.height / 20;
			mlayout.width = tmplayout.width + 25 + (tmplayout.width / 10);
			mlayout.height = tmplayout.height + 23 + (tmplayout.height / 10);
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
	}

}
