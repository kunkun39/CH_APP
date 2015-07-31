package com.changhong.gdappstore.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseRelativeLayout;
import com.changhong.gdappstore.model.PostItemModel;

/**
 * 推荐位海报
 * 
 * @author wangxiufeng
 * 
 */
public class PostItemView extends BaseRelativeLayout {

	private ImageView iv_post, iv_appicon;
	private TextView tv_postname, tv_appname;
	private RelativeLayout rl_post, rl_app;

	public PostItemView(Context context) {
		super(context);
		initView();
	}

	public PostItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public PostItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	protected void initView() {
		LayoutInflater.from(context).inflate(R.layout.item_post, this);
		rl_post=findView(R.id.rl_postitem_bigpost);
		iv_post = findView(R.id.iv_post);
		tv_postname = findView(R.id.tv_postname);
		rl_app=findView(R.id.rl_postitem_apppost);
		iv_appicon = findView(R.id.iv_appicon);
		tv_appname = findView(R.id.tv_appname);
	}

	public void setData(PostItemModel model) {
		if (model==null) {
			return;
		}
		if (model.isapp) {
			rl_app.setVisibility(VISIBLE);
			rl_post.setVisibility(INVISIBLE);
			if (model.imageId > 0) {
				iv_appicon.setImageResource(model.imageId);
			}
			tv_appname.setText(TextUtils.isEmpty(model.name)?"":model.name);
		}else {
			rl_app.setVisibility(VISIBLE);
			rl_post.setVisibility(VISIBLE);
			if (model.imageId > 0) {
				iv_post.setImageResource(model.imageId);
			}
			tv_postname.setText(TextUtils.isEmpty(model.name)?"":model.name);
		}
		
	}
}
