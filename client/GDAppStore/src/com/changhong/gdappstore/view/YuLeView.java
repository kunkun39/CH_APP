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
 * 娱乐view
 * 
 * @author wangxiufeng
 * 
 */
public class YuLeView extends BasePageView implements OnFocusChangeListener, OnClickListener {
	/** 外部回调点击监听器 */
	private OnClickListener onClickListener;
	/** 外部回调焦点监听器 */
	private OnFocusChangeListener onFocusChangeListener;
	/** 焦点框view */
	private ImageView ivFocues;
	/** 是否第一列或者最后一列获取焦点 */
	public boolean isLeftFocues = false, isRightFocues = false;

	public YuLeView(Context context) {
		super(context);
		initView();
	}

	public YuLeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public YuLeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	protected void initView() {
		View rootView = LayoutInflater.from(context).inflate(R.layout.view_homepage, this);
		ivFocues = findView(R.id.iv_jingpin_focues);
		ivFocues.setVisibility(INVISIBLE);
		for (int i = 0; i < postItemCount; i++) {
			postItemViews[i] = findView(postItemIds[i]);
			postItemViews[i].setFocusable(true);
			postItemViews[i].setClickable(true);
			postItemViews[i].setOnClickListener(this);
			postItemViews[i].setOnFocusChangeListener(this);
		}
		for (int i = 0; i < 4; i++) {
			categoryItemViews[i] = findView(categroyItemIds[i]);
			categoryItemViews[i].setFocusable(true);
			categoryItemViews[i].setClickable(true);
			categoryItemViews[i].setOnClickListener(this);
			categoryItemViews[i].setOnFocusChangeListener(this);
		}
		categoryItemViews[0].setBackgroundResource(R.drawable.img_maincategory_bg1);
		categoryItemViews[1].setBackgroundResource(R.drawable.img_maincategory_bg2);
		categoryItemViews[2].setBackgroundResource(R.drawable.img_maincategory_bg3);
		categoryItemViews[3].setBackgroundResource(R.drawable.img_maincategory_bg4);
	}

	public void initData(final Category category) {
		if (category.getCategoyChildren() != null) {
			// 初始化左边4个子栏目数据，最多4个
			int size = category.getCategoyChildren().size();
			for (int i = 0; i < (size <= 4 ? size : 4); i++) {
				final Category childCategory = category.getCategoyChildren().get(i);
				categoryItemViews[i].setCategoryData(childCategory);
				categoryItemViews[i].setOnClickListener(new OnClickListener() {

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
			if (position <= postItemCount) {
				postItemViews[(position - 1)].setPageAppData(pageApp);
				postItemViews[(position - 1)].setOnClickListener(new OnClickListener() {

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
		categoryItemViews[0].setNextFocusUpId(id);
		postItemViews[0].setNextFocusUpId(id);
		postItemViews[1].setNextFocusUpId(id);
		postItemViews[6].setNextFocusUpId(id);
		postItemViews[9].setNextFocusUpId(id);
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
			L.d(TAG + "yuleview onfocueschange " + v.getId());
			int viewId = v.getId();
			currentFocuesId = v.getId();
			if (viewId == R.id.jingping_item1 || viewId == R.id.jingping_item2 || viewId == R.id.jingping_item3
					|| viewId == R.id.jingping_item4) {
				isLeftFocues = true;
			} else {
				isLeftFocues = false;
			}
			if (viewId == R.id.jingping_item9 || viewId == R.id.jingping_item3) {
				isRightFocues = true;
			} else {
				isRightFocues = false;
			}
			RelativeLayout.LayoutParams mlayout = new RelativeLayout.LayoutParams(100, 100);
			// RelativeLayout.LayoutParams tmplayout = (LayoutParams) v
			// .getLayoutParams();
			RelativeLayout.LayoutParams tmplayout = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			tmplayout.leftMargin = v.getLeft();
			tmplayout.topMargin = v.getTop();
			tmplayout.width = v.getWidth();
			tmplayout.height = v.getHeight();
			if (viewId == R.id.jingping_item1 || viewId == R.id.jingping_item6) {
				// 大海报
				mlayout.leftMargin = (int) (tmplayout.leftMargin + bigLeftMar_add - tmplayout.width * 0.1);
				mlayout.topMargin = (int) (tmplayout.topMargin + bigTopMar_add - tmplayout.height * 0.1);
				mlayout.width = (int) (tmplayout.width + bigWidth_add + (tmplayout.width * 0.1));
				mlayout.height = (int) (tmplayout.height + bigHeight_add + (tmplayout.height * 0.1));
			} else if (viewId == R.id.jingping_itema1 || viewId == R.id.jingping_itema2
					|| viewId == R.id.jingping_itema3 || viewId == R.id.jingping_itema4) {
				mlayout.leftMargin = (int) (tmplayout.leftMargin + horLeftMar_add - tmplayout.width * 0.1);
				mlayout.topMargin = (int) (tmplayout.topMargin + horTopMar_add - tmplayout.height * 0.1);
				mlayout.width = (int) (tmplayout.width + horWidth_add + (tmplayout.width * 0.1));
				mlayout.height = (int) (tmplayout.height + horHeight_add + (tmplayout.height * 0.1));
			} else {
				mlayout.leftMargin = (int) (tmplayout.leftMargin + smallLeftMar_add - tmplayout.width * 0.1);
				mlayout.topMargin = (int) (tmplayout.topMargin + smallTopMar_add - tmplayout.height * 0.1);
				mlayout.width = (int) (tmplayout.width + smallWidth_add + (tmplayout.width * 0.1));
				mlayout.height = (int) (tmplayout.height + smallHeight_add + (tmplayout.width * 0.1));
			}
			ivFocues.setBackgroundResource(R.drawable.img_focues_mainpost);
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
		((PostItemView) v).setItemSelected(hasFocus);
		if (onFocusChangeListener != null) {
			onFocusChangeListener.onFocusChange(v, hasFocus);
		}
	}

}
