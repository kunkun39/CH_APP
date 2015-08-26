package com.changhong.gdappstore.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseRelativeLayout;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.util.ImageLoadUtil;

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
		rl_post = findView(R.id.rl_postitem_bigpost);
		iv_post = findView(R.id.iv_post);
		tv_postname = findView(R.id.tv_postname);
		rl_app = findView(R.id.rl_postitem_apppost);
		iv_appicon = findView(R.id.iv_appicon);
		tv_appname = findView(R.id.tv_appname);
	}

	/**
	 * 栏目推荐位
	 * 
	 * @param category
	 * @param iconId
	 *            图片临时采用写死
	 */
	public void setCategoryData(Category category) {
		rl_app.setVisibility(VISIBLE);
		rl_post.setVisibility(INVISIBLE);
		if (category == null) {
			return;
		}
		if (!TextUtils.isEmpty(category.getIconFilePath())) {
			ImageLoadUtil.displayImgByMemoryDiscCache(category.getIconFilePath(), iv_appicon);
		}
		tv_appname.setText(TextUtils.isEmpty(category.getName()) ? "" : category.getName());

	}
	/**
	 * 显示本地图片
	 * @param ispost 是否是海报
	 * @param drawableid 图片id
	 */
	public void setDrawableIconPost(boolean ispost,int drawableid) {
		if (ispost) {
			iv_post.setImageResource(drawableid);
		}else {
			iv_appicon.setImageResource(drawableid);
		}
	}

	/**
	 * 页面推荐位
	 * 
	 * @param category
	 */
	public void setPageAppData(PageApp pageApp) {
		if (pageApp == null) {
			return;
		}
		if (pageApp.getPosition() > 3) {
			// 小海报图标
			rl_app.setVisibility(VISIBLE);
			rl_post.setVisibility(INVISIBLE);
			if (pageApp != null) {
				ImageLoadUtil.displayImgByMemoryDiscCache(pageApp.getPosterFilePath(), iv_appicon);
				tv_appname.setText(TextUtils.isEmpty(pageApp.getAppname()) ? "" : pageApp.getAppname());
			}
		} else {
			// 大海报图片
			rl_app.setVisibility(INVISIBLE);
			rl_post.setVisibility(VISIBLE);
			rl_post.setBackgroundColor(Color.TRANSPARENT);
			if (pageApp != null) {
				ImageLoadUtil.displayImgByMemoryDiscCache(pageApp.getPosterFilePath(), iv_post);
				tv_postname.setText(TextUtils.isEmpty(pageApp.getAppname()) ? "" : pageApp.getAppname());
			}
		}
	}

	/**
	 * 页面推荐位
	 * 
	 * @param category
	 */
	public void setAppData(App pageApp) {
		if (pageApp == null) {
			return;
		}
		// 小海报图标
		rl_app.setVisibility(VISIBLE);
		rl_post.setVisibility(INVISIBLE);
		if (pageApp != null) {
			ImageLoadUtil.displayImgByMemoryDiscCache(pageApp.getPosterFilePath(), iv_appicon);
			tv_appname.setText(TextUtils.isEmpty(pageApp.getAppname()) ? "" : pageApp.getAppname());
		}
	}

	public void setSelected(boolean selected) {
		if (tv_appname.getVisibility() == VISIBLE) {
			tv_appname.setSelected(selected);
		}
		if (tv_postname.getVisibility() == VISIBLE) {
			tv_postname.setSelected(selected);
		}
	}
}
