package com.changhong.gdappstore.base;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.activity.DetailActivity;
import com.changhong.gdappstore.activity.PostActivity;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.view.PostItemView;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

/**
 * 基类pageView
 * 
 * @author wangxiufeng
 * 
 */
public class BasePageView extends BaseRelativeLayout {

	/** 推荐位ID （默认布局为：左边4个序号分别是9,10,11,12上面3个海报序号为0,1,2下面6个序号为3,4,5,6,7,8） */
	protected int[] itemIds = { R.id.jingping_item1, R.id.jingping_item2, R.id.jingping_item3, R.id.jingping_item4,
			R.id.jingping_item5, R.id.jingping_item6, R.id.jingping_item7, R.id.jingping_item8, R.id.jingping_item9,
			R.id.jingping_itema1, R.id.jingping_itema2, R.id.jingping_itema3, R.id.jingping_itema4 };
	/** 推荐位个数 */
	protected int itemCount = 13;
	/** 推荐位view （默认布局为：左边4个序号分别是9,10,11,12上面3个海报序号为0,1,2下面6个序号为3,4,5,6,7,8） */
	protected PostItemView[] itemViews = new PostItemView[itemCount];
	/** 焦点缩放动画 */
	protected Animation animationbig, animationsmall;
	/** 当前焦点位置 */
	public int currentFocuesId;
	
	protected final int bigLeftMar_add=-1,bigTopMar_add=-3,bigWidth_add=1,bigHeight_add=-5;
	protected final int smallLeftMar_add=-13,smallTopMar_add=-14,smallWidth_add=20,smallHeight_add=21;

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
	 * set the item view in itemViews request focus by position .
	 * 
	 * @param position
	 */
	public void setFocuesItemByPosition(int position) {
		if (itemViews != null && position >= 0 && position < itemViews.length && itemViews[position] != null) {
			itemViews[position].requestFocus();
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
