package com.changhong.gdappstore.base;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.activity.DetailActivity;
import com.changhong.gdappstore.activity.PostActivity;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.view.PostItemView;

/**
 * 基类pageView
 * 
 * @author wangxiufeng
 * 
 */
public class BasePageView extends BaseRelativeLayout {

	/** 应用推荐位ID */
	protected int[] postItemIds = { R.id.jingping_item1, R.id.jingping_item2, R.id.jingping_item3, R.id.jingping_item4,
			R.id.jingping_item5, R.id.jingping_item6, R.id.jingping_item7, R.id.jingping_item8, R.id.jingping_item9,
			R.id.jingping_item10, R.id.jingping_item11, R.id.jingping_item12 };
	/** 栏目推荐位ID */
	protected int[] categroyItemIds = { R.id.jingping_itema1, R.id.jingping_itema2, R.id.jingping_itema3,
			R.id.jingping_itema4 };
	/** 应用推荐位个数 */
	protected  int postItemCount = 12;
	/** 应用推荐位view */
	protected PostItemView[] postItemViews = new PostItemView[postItemCount];
	/** 栏目推荐位view */
	protected PostItemView[] categoryItemViews = new PostItemView[4];
	/** 焦点缩放动画 */
	protected Animation animationbig, animationsmall;
	/** 当前焦点位置 */
	public int currentFocuesId;

	protected final int bigLeftMar_add = 3, bigTopMar_add = -6, bigWidth_add = 10, bigHeight_add = 18;
	protected final int horLeftMar_add = -15, horTopMar_add = -15, horWidth_add = 24, horHeight_add = 26;
	protected final int smallLeftMar_add = -15, smallTopMar_add = -15, smallWidth_add = 25, smallHeight_add = 27;

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
		animationsmall = AnimationUtils.loadAnimation(context, R.anim.scale_small);
	}

	/**
	 * set the item view in categoryItemViews request focus by position .
	 * 
	 * @param position
	 */
	public void setCategoryItemFocuesByPos(int position) {
		if (categoryItemViews != null && position >= 0 && position < categoryItemViews.length
				&& categoryItemViews[position] != null) {
			categoryItemViews[position].requestFocus();
		}
	}

	/**
	 * set the item view in postItemViews request focus by position .
	 * 
	 * @param position
	 */
	public void setPostItemFocuesByPos(int position) {
		if (postItemViews != null && position >= 0 && position < postItemViews.length
				&& postItemViews[position] != null) {
			postItemViews[position].requestFocus();
		}
	}

	/**
	 * 跳转到海报墙页面
	 * 
	 * @param parentCategoryId
	 *            父栏目id
	 * @param currentCategoryId
	 *            子栏目id
	 */
	protected void jumpToPostActivity(int parentCategoryId, int currentCategoryId) {
		if (currentCategoryId < 0) {
			DialogUtil.showShortToast(context, "当前分类未配置");
			return;
		}
		if (!NetworkUtils.ISNET_CONNECT) {
			DialogUtil.showShortToast(context, context.getString(R.string.net_notconnected));
			return;
		}
		Intent intent = new Intent(context, PostActivity.class);
		intent.putExtra(Config.KEY_PARENT_CATEGORYID, parentCategoryId);
		intent.putExtra(Config.KEY_CURRENT_CATEGORYID, currentCategoryId);
		context.startActivity(intent);
	}

	/**
	 * 跳转到应用详情页面
	 * 
	 * @param appid
	 */
	protected void jumpToDetailActivity(int appid) {
		if (appid < 0) {
			Toast.makeText(context, "当前应用未配置", Toast.LENGTH_SHORT).show();
			return;
		}
		if (!NetworkUtils.ISNET_CONNECT) {
			DialogUtil.showShortToast(context, context.getString(R.string.net_notconnected));
			return;
		}
		Intent intent = new Intent(context, DetailActivity.class);
		intent.putExtra(Config.KEY_APPID, appid);
		context.startActivity(intent);
	}

}
