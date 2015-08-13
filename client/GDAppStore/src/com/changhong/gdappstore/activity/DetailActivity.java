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

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
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
	/** 应用文本介绍信息 */
	private TextView tv_appname, tv_downloadcount, tv_size, tv_version, tv_updatetime, tv_controltool, tv_introduce;

	private ImageView iv_post, iv_icon;

	private AppDetail appDetail;

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
		iv_post = findView(R.id.iv_detailpost);
		iv_icon = findView(R.id.iv_detailicon);
		tv_appname = findView(R.id.tv_appname);
		tv_downloadcount = findView(R.id.tv_downloadcount);
		tv_size = findView(R.id.tv_appsize);
		tv_version = findView(R.id.tv_version);
		tv_updatetime = findView(R.id.tv_updatetime);
		tv_controltool = findView(R.id.tv_controltool);
		tv_introduce = findView(R.id.tv_introduce);
	}

	private void initData() {
		view_usermaylike.initData();
		int appId = -1;
		if (getIntent() != null) {
			appId = getIntent().getIntExtra(Config.KEY_APPID, -1);
		}
		DataCenter.getInstance().loadAppDetail(appId, new LoadObjectListener() {

			@Override
			public void onComplete(Object object) {
				appDetail = (AppDetail) object;
				if (appDetail != null) {
					tv_appname.setText(appDetail.getAppname());
					tv_downloadcount.setText(appDetail.getDownload());
					tv_size.setText(appDetail.getApkSize());
					tv_version.setText(appDetail.getVersion());
					tv_introduce.setText(appDetail.getDescription());
					ImageLoadUtil.displayImgByNoCache(appDetail.getIconFilePath(), iv_icon);
					ImageLoadUtil.displayImgByNoCache(appDetail.getPosterFilePath(), iv_post);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}
}
