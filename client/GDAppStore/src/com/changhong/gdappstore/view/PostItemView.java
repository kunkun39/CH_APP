package com.changhong.gdappstore.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseRelativeLayout;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;

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
		if (TextUtils.isEmpty(category.getIconFilePath())) {
			L.d("111 "+""+"  "+category);
			// 没有配置图片时候写死配置
			if (category.getName().equals("搜索")) {
				iv_appicon.setImageResource(R.drawable.icon_jingpin_search);
			} else if (category.getName().equals("排行榜")) {
				iv_appicon.setImageResource(R.drawable.icon_jingpin_ranklist);
			} else if (category.getName().equals("本地应用")) {
				iv_appicon.setImageResource(R.drawable.icon_jingpin_subject);
			} else if (category.getName().equals("装机必备")) {
				iv_appicon.setImageResource(R.drawable.icon_jingpin_necessary);
			} else if (category.getName().equals("生活")) {
				iv_appicon.setImageResource(R.drawable.icon_yule_life);
			} else if (category.getName().equals("音乐")) {
				iv_appicon.setImageResource(R.drawable.icon_yule_child);
			} else if (category.getName().equals("健康")) {
				iv_appicon.setImageResource(R.drawable.icon_yule_health);
			} else if (category.getName().equals("其他")) {
				iv_appicon.setImageResource(R.drawable.icon_yule_more);
			} else if (category.getName().equals("休闲")) {
				iv_appicon.setImageResource(R.drawable.icon_youxi_relax);
			} else if (category.getName().equals("棋牌")) {
				iv_appicon.setImageResource(R.drawable.icon_youxi_card);
			} else if (category.getName().equals("动作")) {
				iv_appicon.setImageResource(R.drawable.icon_youxi_move);
			} else if (category.getName().equals("工具")) {
				iv_appicon.setImageResource(R.drawable.icon_zhuanti_tool);
			} else if (category.getName().equals("教育")) {
				iv_appicon.setImageResource(R.drawable.icon_zhuanti_education);
			} else if (category.getName().equals("咨询")) {
				iv_appicon.setImageResource(R.drawable.icon_zhuanti_parts);
			}
		}
		tv_appname.setText(TextUtils.isEmpty(category.getName()) ? "" : category.getName());

	}

	/**
	 * 栏目推荐为
	 * 
	 * @param category
	 */
	public void setAppData(PageApp pageApp) {
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
			if (pageApp != null) {
				ImageLoadUtil.displayImgByMemoryDiscCache(pageApp.getPosterFilePath(), iv_post);
				tv_postname.setText(TextUtils.isEmpty(pageApp.getAppname()) ? "" : pageApp.getAppname());
			}
		}
	}
}
