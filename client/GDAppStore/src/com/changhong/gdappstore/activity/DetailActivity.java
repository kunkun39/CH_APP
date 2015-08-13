package com.changhong.gdappstore.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.view.PostItemView;
import com.changhong.gdappstore.view.ScoreView;
import com.changhong.gdappstore.view.UserMayLikeView;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 应用详情页面
 * 
 * @author wangxiufeng
 * 
 */
public class DetailActivity extends BaseActivity implements OnFocusChangeListener, OnClickListener {
	/** 下载按钮 */
	private Button bt_dowload;
	/** 用户喜欢 */
	private UserMayLikeView view_usermaylike;
	/** 评分 */
	private ScoreView scoreView;
	/** 应用文本介绍信息 */
	private TextView tv_appname, tv_downloadcount, tv_size, tv_version, tv_updatetime, tv_controltool, tv_tv_introduce;

	private ImageView iv_post;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		initView();
		initData();
	}

	private void initView() {
		view_usermaylike = findView(R.id.view_usermaylike);
		bt_dowload = findView(R.id.bt_download);
		bt_dowload.setOnFocusChangeListener(this);
		scoreView = findView(R.id.scoreview);
		iv_post = findView(R.id.iv_post);
	}

	private void initData() {
		view_usermaylike.initData();
		scoreView.setScoreBy5Total(5);
//		ImageLoadUtil.loadImgByNoCache("http://img4.imgtn.bdimg.com/it/u=695405215,1736088129&fm=21&gp=0.jpg", iv_post,
//				new ImageLoadingListener() {
//
//					@Override
//					public void onLoadingStarted(String paramString, View paramView) {
//						L.d("onLoadingStarted");
//					}
//
//					@Override
//					public void onLoadingFailed(String paramString, View paramView, FailReason paramFailReason) {
//						L.d("onLoadingFailed "+paramFailReason);
//					}
//
//					@Override
//					public void onLoadingComplete(String paramString, View paramView, Bitmap paramBitmap) {
//						L.d("onLoadingComplete");
//					}
//
//					@Override
//					public void onLoadingCancelled(String paramString, View paramView) {
//						L.d("onLoadingCancelled");
//					}
//				});
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}
}
