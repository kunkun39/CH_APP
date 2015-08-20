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
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.util.L;
/**
 * 应用页面view
 * @author wangxiufeng
 *
 */
public class ZhuanTiView extends BasePageView implements
OnFocusChangeListener, OnClickListener {
	/** 外部回调点击监听器 */
	private OnClickListener onClickListener;
	/** 外部回调焦点监听器 */
	private OnFocusChangeListener onFocusChangeListener;
	/** 焦点框view */
	private ImageView ivFocues;
	/**是否第一列或者最后一列获取焦点*/
	public boolean isLeftFocues = false, isRightFocues = false;

	public ZhuanTiView(Context context) {
		super(context);
		initView();
	}

	public ZhuanTiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public ZhuanTiView(Context context, AttributeSet attrs, int defStyle) {
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
	}
	public void initData(final Category category) {
		if (category.getCategoyChildren() != null) {
			// 初始化左边4个子栏目数据，最多4个
			int size = category.getCategoyChildren().size();
			for (int i = 0; i < (size <= 4 ? size : 4); i++) {
				final Category childCategory = category.getCategoyChildren().get(i);
				itemViews[9 + i].setCategoryData(childCategory);
				itemViews[9 + i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (onClickListener != null) {
							onClickListener.onClick(v);
						}
						jumpToPostActivity(category.getId(), childCategory.getId());
					}
				});
			}
		}
		if (category.getCategoryPageApps() == null) {
			return;
		}
		for (int i = 0; i < category.getCategoryPageApps().size(); i++) {
			final PageApp pageApp = category.getCategoryPageApps().get(i);
			int position = pageApp.getPosition();
			if (position <= 9) {
				itemViews[(position - 1)].setPageAppData(pageApp);
				itemViews[(position - 1)].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (onClickListener != null) {
							onClickListener.onClick(v);
						}
						jumpToDetailActivity(pageApp.getAppid());
					}
				});
			}
		}
	}

	/**
	 * 最上一排海报往上按默认焦点
	 * 
	 * @param id
	 */
	public void setNextFocuesUpId(int id) {
		itemViews[0].setNextFocusUpId(id);
		itemViews[1].setNextFocusUpId(id);
		itemViews[2].setNextFocusUpId(id);
		itemViews[9].setNextFocusUpId(id);
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
			currentFocuesId = v.getId();
			if (viewId == R.id.jingping_item1 || viewId == R.id.jingping_item2
					|| viewId == R.id.jingping_item3
					|| viewId == R.id.jingping_item4) {
				isLeftFocues = true;
			} else {
				isLeftFocues = false;
			}
			if (viewId == R.id.jingping_item9
					|| viewId == R.id.jingping_item3) {
				isRightFocues = true;
			} else {
				isRightFocues = false;
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
			if (viewId == R.id.jingping_item1
					|| viewId == R.id.jingping_item2
					|| viewId == R.id.jingping_item3) {
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

}
