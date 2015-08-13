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
		iv_post = findView(R.id.iv_detailpost);
	}

	private void initData() {
		view_usermaylike.initData();
		scoreView.setScoreBy5Total(5);
		//测试数据
		ImageLoadUtil.displayImgByNoCache("http://c.hiphotos.baidu.com/image/pic/item/8694a4c27d1ed21b949a2ed3a96eddc451da3fb0.jpg", iv_post);
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}
}
