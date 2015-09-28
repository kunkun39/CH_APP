package com.changhong.gdappstore.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
	
	private ImageView iv_shandow1,iv_shandow2,iv_shandow3,iv_shandow4,iv_shandow5;

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
			categoryItemViews[i].setNextFocusLeftId(categroyItemIds[i]);// 最左一排不能再按左。只是首页
		}
		categoryItemViews[0].setBackgroundResource(R.drawable.img_maincategory_bg1);
		categoryItemViews[1].setBackgroundResource(R.drawable.img_maincategory_bg2);
		categoryItemViews[2].setBackgroundResource(R.drawable.img_maincategory_bg3);
		categoryItemViews[3].setBackgroundResource(R.drawable.img_maincategory_bg4);
		
		iv_shandow1=findView(R.id.iv_shandow1);
		iv_shandow2=findView(R.id.iv_shandow2);
		iv_shandow3=findView(R.id.iv_shandow3);
		iv_shandow4=findView(R.id.iv_shandow4);
		iv_shandow5=findView(R.id.iv_shandow5);
	}

	public void initNativeData() {
		// 首页的4个标签写死
		categoryItemViews[0].setCategoryData(new Category(-10, -10, "搜索"));
		categoryItemViews[1].setCategoryData(new Category(-10, -10, "排行榜"));
		categoryItemViews[2].setCategoryData(new Category(-10, -10, "本地应用"));
		categoryItemViews[3].setCategoryData(new Category(-10, -10, "装机必备"));
		categoryItemViews[0].setDrawableIconPost(false, R.drawable.icon_search);
		categoryItemViews[1].setDrawableIconPost(false, R.drawable.icon_ranklist);
		categoryItemViews[2].setDrawableIconPost(false, R.drawable.icon_nativeapp);
		categoryItemViews[3].setDrawableIconPost(false, R.drawable.icon_nessary);
	}

	public void initData(Category category) {
		if (category.getCategoryPageApps() == null) {
			return;
		}
		for (int i = 0; i < category.getCategoryPageApps().size(); i++) {
			final PageApp pageApp = category.getCategoryPageApps().get(i);
			int position = pageApp.getPosition();
			if (position <= postItemCount) {
				if (position==5||position==6||position==9||position==12) {
					postItemViews[(position - 1)].setPageAppData(pageApp,imageLoadingListener);
				}else {
					postItemViews[(position - 1)].setPageAppData(pageApp,null);
				}
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
		if (v.getId() == R.id.jingping_itema3) {
			context.startActivity(new Intent(context, NativeAppActivity.class));
			return;
		}
		if (NetworkUtils.ISNET_CONNECT) {
			if (v.getId() == R.id.jingping_itema1) {
				context.startActivity(new Intent(context, SearchActivity.class));
			} else if (v.getId() == R.id.jingping_itema2) {
				context.startActivity(new Intent(context, RankingListActivity.class));
			} else if (v.getId() == R.id.jingping_itema4) {
				DataCenter dataCenter = DataCenter.getInstance();
				if (dataCenter.getCategoryById(12) != null) {
					jumpToPostActivity(dataCenter.getCategoryById(12).getParentId(), dataCenter.getCategoryById(12)
							.getId());
				}
			} else {
				DialogUtil.showLongToast(context, context.getResources().getString(R.string.weipeizhi));
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
			if (viewId == R.id.jingping_item5 || viewId == R.id.jingping_item6 || viewId == R.id.jingping_item12) {
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
		((PageItemView) v).setItemSelected(hasFocus);
	}
	
	public void setShandows() {
		iv_shandow1.setImageBitmap(Util.createImages(context, Util.convertViewToBitmap(categoryItemViews[3])));
		iv_shandow2.setImageBitmap(Util.createImages(context, Util.convertViewToBitmap(postItemViews[4])));
		iv_shandow3.setImageBitmap(Util.createImages(context, Util.convertViewToBitmap(postItemViews[5])));
		iv_shandow4.setImageBitmap(Util.createImages(context, Util.convertViewToBitmap(postItemViews[8])));
		iv_shandow5.setImageBitmap(Util.createImages(context, Util.convertViewToBitmap(postItemViews[11])));
	}
	
	private ImageLoadingListener imageLoadingListener =new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String imageUri, View view) {
			
		}
		
		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			setShandows();
		}
		
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			setShandows();
		}
		
		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			// TODO Auto-generated method stub
			
		}
	};

	public boolean isRightItemFocused() {
		return isRightItemFocused;
	}

}
