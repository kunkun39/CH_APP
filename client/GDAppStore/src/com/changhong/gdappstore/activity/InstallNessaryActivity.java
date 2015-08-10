package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.model.PostTitleModel;
import com.changhong.gdappstore.post.PostModel;
import com.changhong.gdappstore.post.PostSetting;
import com.changhong.gdappstore.post.PosterLayoutView;

/**
 * 装机必备页面
 * 
 * @author wangxiufeng
 * 
 */
public class InstallNessaryActivity extends BaseActivity {
	/** 海报墙view */
	private PosterLayoutView postView;
	/** 海报墙配置项 */
	private PostSetting postSetting;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_install_necessary);
		initView();
		initData();
	}
	private void initView() {
		postView = findView(R.id.postview_installnessary);
		initPostView();
	}
	
	private void initPostView() {

		// 海报墙设置，监听器没有可以设为空，行列设为负数则使用默认值
		postSetting = new PostSetting(2, 6, R.drawable.selector_bg_postitem, null, null, null, null,
				null);
		postSetting.setVerticalScroll(false);// 纵向滚动
		postSetting.setVisibleClumn(1.14f);// 显示的页数
		postSetting.setMargins(10, 10, 0, 0);// item的距离
		// postSetting.setPagePadding(0, 0, 0, 0);
		postSetting.setFirstRowFocusUp(true);// 第一排是否允许焦点再往上
		postSetting.setFirstClumnFocusLeft(false);
		postSetting.setFristItemFocus(false);
		// 如果需要海报墙使用自己的设置，要先调用设置设置方法，在调用设置数据
		postView.init(postSetting);

	}

	private void initData() {

		List<Object> loadItems = new ArrayList<Object>();
		for (int i = 0; i < 50; i++) {
			loadItems.add(new PostModel(i, R.drawable.img_post1, "应用名字应用名字应用名字" + i, 4.5f, "这是第" + i + "个应用"));
		}
		postView.initData(loadItems, loadItems.size());

	}
}
