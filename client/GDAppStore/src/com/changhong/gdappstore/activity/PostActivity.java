package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PostTitleModel;
import com.changhong.gdappstore.post.PostItem;
import com.changhong.gdappstore.post.PostModel;
import com.changhong.gdappstore.post.PostSetting;
import com.changhong.gdappstore.post.PosterLayoutView;
import com.changhong.gdappstore.view.PostTitleView;
import com.changhong.gdappstore.view.PostTitleView.TitleItemOnClickListener;
import com.changhong.gdappstore.view.PostTitleView.TitleItemOnFocuesChangedListener;
import com.post.view.base.BasePosterLayoutView;
import com.post.view.listener.IPosteDateListener;
import com.post.view.listener.Listener.IItemOnClickListener;

/**
 * 海报墙页面
 * 
 * @author wangxiufeng
 * 
 */
public class PostActivity extends BaseActivity {
	/** 海报墙view */
	private PosterLayoutView postView;
	/** 海报墙配置项 */
	private PostSetting postSetting;
	/** 标题view */
	private PostTitleView titleView;
	/** 数据处理中心 */
	private DataCenter dataCenter;
	/** 父栏目，根据首页传过来的id确定 */
	private Category parentCategory = null;
	/**栏目名字*/
	private TextView tv_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		initView();
		initData();
	}

	private void initView() {
		postView = findView(R.id.postview);
		titleView = findView(R.id.posttitleview);
		tv_name=findView(R.id.tv_pagename);
		titleView.setTitleItemOnClickListener(titleItemOnClickListener);
		titleView.setTitleItemOnFocuesChangedListener(titleItemOnFocuesChangedListener);
		initPostView();
	}

	private void initPostView() {

		// 海报墙设置，监听器没有可以设为空，行列设为负数则使用默认值
		postSetting = new PostSetting(3, 3, R.drawable.selector_bg_postitem, iPosteDateListener, null,
				postItemOnclickListener, null, postOnKeyListener);
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
		dataCenter = DataCenter.getInstance();
		Intent intent = getIntent();
		int parentCategoryId = intent.getIntExtra(Config.KEY_PARENT_CATEGORYID, 1);
		int currentCategoryId = intent.getIntExtra(Config.KEY_CURRENT_CATEGORYID, parentCategoryId);
		parentCategory = dataCenter.getCategoryById(parentCategoryId);// 获取父栏目
		if (parentCategory != null && parentCategory.getCategoyChildren() != null) {
			tv_name.setText(parentCategory.getName());
			titleView.initData(parentCategory, parentCategory.getCategoyChildren());
			for (int i = 0; i < parentCategory.getCategoyChildren().size(); i++) {
				if (parentCategory.getCategoyChildren().get(i).getId() == currentCategoryId) {
					titleView.setFocusItem(i + 1);// 选中当前item
				}
			}
		}

		List<Object> loadItems = new ArrayList<Object>();
		for (int i = 0; i < 50; i++) {
			loadItems.add(new PostModel(i, R.drawable.img_post1, "应用名字应用名字应用名字" + i, 4.5f, "这是第" + i + "个应用"));
		}
		postView.initData(loadItems, loadItems.size());

	}

	/**
	 * 标签点击监听器
	 */
	private TitleItemOnClickListener titleItemOnClickListener = new TitleItemOnClickListener() {

		@Override
		public void onItemClick(View view, int position) {

		}
	};
	/**
	 * 标签焦点监听器
	 */
	private TitleItemOnFocuesChangedListener titleItemOnFocuesChangedListener = new TitleItemOnFocuesChangedListener() {

		@Override
		public void onItemFocuesChanged(View view, boolean hasFocues, int position) {

		}
	};
	/** 海报墙点击监听 **/
	private IItemOnClickListener postItemOnclickListener = new IItemOnClickListener() {

		@Override
		public void itemOnClick(BasePosterLayoutView arg0, View arg1, int arg2) {
			startActivity(new Intent(PostActivity.this, DetailActivity.class));
		}
	};

	private IPosteDateListener iPosteDateListener = new IPosteDateListener() {

		@Override
		public void requestNextPageDate(int currentSize) {
			// 请求新数据回调
			// List<Object> loadItems = new ArrayList<Object>();
			// for (int i = currentSize; i < currentSize + 20; i++) {
			// loadItems.add(new PostModel(i, ("标签 " + i), R.drawable.img_post_1
			// + i % 5));
			// }
			// view_post.initData(loadItems, totalnum);
		}

		@Override
		public void changePage(Boolean isnext, int curpage, int totalpage) {
			// 翻页回调
		}

		@Override
		public void lastPageOnKeyDpadDown() {
			Toast.makeText(PostActivity.this, "已经是最后一页了！", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void firstPageOnKeyDpadup() {
			Toast.makeText(PostActivity.this, "已经是第一页了！", Toast.LENGTH_SHORT).show();
		}
	};

	private OnKeyListener postOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			int pos = ((PostItem) v).getPosition();
			if (pos < postSetting.getPost_column() && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
				// 处理每次海报墙按上键时候选中当前标签
				titleView.getCurrentSelectedView().requestFocus();
			}
			return false;
		}
	};
}
