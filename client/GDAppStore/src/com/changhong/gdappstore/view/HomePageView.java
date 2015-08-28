package com.changhong.gdappstore.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.activity.NativeAppActivity;
import com.changhong.gdappstore.activity.RankingListActivity;
import com.changhong.gdappstore.activity.SearchActivity;
import com.changhong.gdappstore.base.BasePageView;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.NetworkUtils;

/**
 * 精品view
 * 
 * @author wangxiufeng
 * 
 */
public class HomePageView extends BasePageView implements OnFocusChangeListener, OnClickListener {

	/** 外部回调点击监听器 */
	private OnClickListener onClickListener;
	/** 外部回调焦点监听器 */
	private OnFocusChangeListener onFocusChangeListener;
	/** 焦点框view */
	private ImageView ivFocues;

	public boolean isRightItemFocused = false;

	public HomePageView(Context context) {
		super(context);
		initView();
	}

	public HomePageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public HomePageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	protected void initView() {
		View rootView = LayoutInflater.from(context).inflate(R.layout.view_homepage, this);
		ivFocues = findView(R.id.iv_jingpin_focues);
		ivFocues.setVisibility(INVISIBLE);
		for (int i = 0; i < itemCount; i++) {
			itemViews[i] = findView(itemIds[i]);
			itemViews[i].setFocusable(true);
			itemViews[i].setClickable(true);
			itemViews[i].setOnFocusChangeListener(this);
		}

		// 最左一排不能再按左。
		itemViews[9].setNextFocusLeftId(R.id.jingping_itema1);
		itemViews[10].setNextFocusLeftId(R.id.jingping_itema2);
		itemViews[11].setNextFocusLeftId(R.id.jingping_itema3);
		itemViews[12].setNextFocusLeftId(R.id.jingping_itema4);
		itemViews[9].setBackgroundResource(R.drawable.img_maincategory_bg1);
		itemViews[10].setBackgroundResource(R.drawable.img_maincategory_bg2);
		itemViews[11].setBackgroundResource(R.drawable.img_maincategory_bg3);
		itemViews[12].setBackgroundResource(R.drawable.img_maincategory_bg4);
	}

	public void initNativeData() {
		// 首页的4个标签写死
		itemViews[9].setCategoryData(new Category(-10, -10, "搜索"));
		itemViews[10].setCategoryData(new Category(-10, -10, "排行榜"));
		itemViews[11].setCategoryData(new Category(-10, -10, "本地应用"));
		itemViews[12].setCategoryData(new Category(-10, -10, "装机必备"));
		itemViews[9].setOnClickListener(this);
		itemViews[10].setOnClickListener(this);
		itemViews[11].setOnClickListener(this);
		itemViews[12].setOnClickListener(this);
		itemViews[9].setDrawableIconPost(false, R.drawable.icon_search);
		itemViews[10].setDrawableIconPost(false, R.drawable.icon_ranklist);
		itemViews[11].setDrawableIconPost(false, R.drawable.icon_nativeapp);
		itemViews[12].setDrawableIconPost(false, R.drawable.icon_nessary);
	}

	public void initData(Category category) {
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
		if (NetworkUtils.ISNET_CONNECT) {
			if (v.getId() == R.id.jingping_itema1) {
				context.startActivity(new Intent(context, SearchActivity.class));
			} else if (v.getId() == R.id.jingping_itema2) {
				context.startActivity(new Intent(context, RankingListActivity.class));
			} else if (v.getId() == R.id.jingping_itema3) {
				context.startActivity(new Intent(context, NativeAppActivity.class));
			} else if (v.getId() == R.id.jingping_itema4) {
				context.startActivity(new Intent(context, NativeAppActivity.class));
			}
		} else {
			DialogUtil.showLongToast(context, context.getResources().getString(R.string.net_notconnected));
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (onFocusChangeListener != null) {
			onFocusChangeListener.onFocusChange(v, hasFocus);
		}
		if (hasFocus) {
			int viewId = v.getId();
			currentFocuesId = v.getId();
			if (viewId == R.id.jingping_item1 || viewId == R.id.jingping_item9) {
				isRightItemFocused = true;
			} else {
				isRightItemFocused = false;
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
			if (viewId == R.id.jingping_item1 || viewId == R.id.jingping_item2 || viewId == R.id.jingping_item3) {
				// 大海报
				mlayout.leftMargin = tmplayout.leftMargin + bigLeftMar_add - tmplayout.width / 20;
				mlayout.topMargin = tmplayout.topMargin + bigTopMar_add - tmplayout.height / 20;
				mlayout.width = tmplayout.width + bigWidth_add + (tmplayout.width / 10);
				mlayout.height = tmplayout.height + bigHeight_add + (tmplayout.height / 10);
			} else {
				mlayout.leftMargin = tmplayout.leftMargin + smallLeftMar_add - tmplayout.width / 20;
				mlayout.topMargin = tmplayout.topMargin + smallTopMar_add - tmplayout.height / 20;
				mlayout.width = tmplayout.width + smallWidth_add + (tmplayout.width / 10);
				mlayout.height = tmplayout.height + smallHeight_add + (tmplayout.height / 10);
			}
			ivFocues.setBackgroundResource(R.drawable.focues_post);
			ivFocues.setLayoutParams(mlayout);
			ivFocues.setVisibility(View.VISIBLE);

			v.startAnimation(animationbig);
			ivFocues.startAnimation(animationbig);
			v.bringToFront();
			ivFocues.bringToFront();
			((PostItemView) v).setSelected(true);
		} else {
			v.startAnimation(animationsmall);
			ivFocues.startAnimation(animationsmall);
			v.clearAnimation();
			ivFocues.clearAnimation();
			ivFocues.setVisibility(View.INVISIBLE);
			((PostItemView) v).setSelected(false);
		}
	}

	public boolean isRightItemFocused() {
		return isRightItemFocused;
	}

}