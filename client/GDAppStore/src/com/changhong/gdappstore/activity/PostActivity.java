package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.net.LoadListener.LoadListListener;
import com.changhong.gdappstore.post.PostItem;
import com.changhong.gdappstore.post.PostSetting;
import com.changhong.gdappstore.post.PosterLayoutView;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
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
	protected PosterLayoutView postView;
	/** 海报墙配置项 */
	protected PostSetting postSetting;
	/** 标题view */
	protected PostTitleView titleView;
	/** 数据处理中心 */
	protected DataCenter dataCenter;
	/** 父栏目，根据首页传过来的id确定 */
	protected Category parentCategory = null;
	/** 栏目名字 */
	protected TextView tv_name,tv_page;
	/**搜索按钮*/
	protected ImageView iv_search;
	/** 当前应用列表 */
	private List<Object> currentApps = new ArrayList<Object>();
	/**阴影图片*/
	protected ImageView iv_shandow1,iv_shandow2,iv_shandow3,iv_shandow4;

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
		tv_name = findView(R.id.tv_pagename);
		iv_shandow1=findView(R.id.iv_shandow1);
		iv_shandow2=findView(R.id.iv_shandow2);
		iv_shandow3=findView(R.id.iv_shandow3);
		iv_shandow4=findView(R.id.iv_shandow4);
		tv_page=findView(R.id.tv_page);
		iv_search=findView(R.id.iv_search);
		iv_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(PostActivity.this,SearchActivity.class));
			}
		});
		titleView.setTitleItemOnClickListener(new TitleItemOnClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				Category category = (Category) view.getTag();
				dataCenter.loadAppsByCategoryId(context, category.getId(), loadAppListListener);
			}
		});
		titleView.setTitleItemOnFocuesChangedListener(new TitleItemOnFocuesChangedListener() {

			@Override
			public void onItemFocuesChanged(View view, boolean hasFocues, int position) {

			}
		});
		initPostView();
	}

	private void initPostView() {

		// 海报墙设置，监听器没有可以设为空，行列设为负数则使用默认值
		postSetting = new PostSetting(3, 3, R.drawable.selector_bg_postitem, iPosteDateListener, null,
				postItemOnclickListener, null, postOnKeyListener);
		postSetting.setVerticalScroll(false);// 纵向滚动
		postSetting.setVisibleClumn(1.07f);// 显示的页数
		postSetting.setMargins(0, 0, 0, 0);// item的距离
		// postSetting.setPagePadding(0, 0, 0, 0);
		postSetting.setFirstRowFocusUp(true);// 第一排是否允许焦点再往上
		postSetting.setFirstClumnFocusLeft(false);
		postSetting.setFristItemFocus(false);
		postSetting.setPosttype(PostSetting.TYPE_NORMAL);
		// 如果需要海报墙使用自己的设置，要先调用设置设置方法，在调用设置数据
		postView.init(postSetting);

	}

	private void initData() {
		dataCenter = DataCenter.getInstance();
		Intent intent = getIntent();
		int parentCategoryId = intent.getIntExtra(Config.KEY_PARENT_CATEGORYID, -2);
		int currentCategoryId = intent.getIntExtra(Config.KEY_CURRENT_CATEGORYID, parentCategoryId);
		if (parentCategoryId < -1) {
			return;
		}
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
		dataCenter.loadAppsByCategoryId(context, currentCategoryId, loadAppListListener);
	}

	private LoadListListener loadAppListListener = new LoadListListener() {

		@Override
		public void onComplete(List<Object> items) {
			currentApps = items;
			L.d("loadapp complete " + ((items == null) ? "items is null" : items.size()));
			postView.refreshAllData(currentApps, postSetting, currentApps == null ? 0 : currentApps.size());
		}
	};

	/** 海报墙点击监听 **/
	private IItemOnClickListener postItemOnclickListener = new IItemOnClickListener() {

		@Override
		public void itemOnClick(BasePosterLayoutView arg0, View arg1, int arg2) {
			if (arg1 == null || arg1.getTag() == null) {
				return;
			}
			if (!NetworkUtils.ISNET_CONNECT) {
				DialogUtil.showShortToast(context, context.getString(R.string.net_notconnected));
				return;
			}
			App app = (App) arg1.getTag();
			Intent intent = new Intent(PostActivity.this, DetailActivity.class);
			intent.putExtra(Config.KEY_APPID, app.getAppid());
			startActivity(intent);
		}
	};

	private IPosteDateListener iPosteDateListener = new IPosteDateListener() {

		@Override
		public void requestNextPageDate(int currentSize) {
			// 请求新数据回调
		}

		@Override
		public void changePage(Boolean isnext, int curpage, int totalpage) {
			// 翻页回调
			tv_page.setText("("+(totalpage<=0?0:curpage)+"/"+totalpage+")");
			setShandowsVisible(curpage, totalpage);
		}

		@Override
		public void lastPageOnKeyDpadDown() {
			// Toast.makeText(PostActivity.this, "已经是最后一页了！",
			// Toast.LENGTH_SHORT).show();
		}

		@Override
		public void firstPageOnKeyDpadup() {
			// Toast.makeText(PostActivity.this, "已经是第一页了！",
			// Toast.LENGTH_SHORT).show();
		}
	};

	private void setShandowsVisible(int curPage, int totalPage) {
		if (currentApps==null ||curPage < 0 || totalPage < 0 || curPage > totalPage) {
			return;
		}
		int size=currentApps.size();
		if (curPage==totalPage) {
			iv_shandow4.setVisibility(INVISIBLE);
			int curItems=size-((curPage-1)*9);//本页有多少个item
			iv_shandow1.setVisibility(curItems>=7?VISIBLE:INVISIBLE);
			iv_shandow2.setVisibility(curItems>=8?VISIBLE:INVISIBLE);
			iv_shandow3.setVisibility(curItems==9?VISIBLE:INVISIBLE);
		}else {
			iv_shandow1.setVisibility(VISIBLE);
			iv_shandow2.setVisibility(VISIBLE);
			iv_shandow3.setVisibility(VISIBLE);
			int nextItems=size-(curPage*9);//本页有多少个item
			iv_shandow4.setVisibility(nextItems>=7?VISIBLE:INVISIBLE);
		}
	}

	private OnKeyListener postOnKeyListener = new OnKeyListener() {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			int pos = ((PostItem) v).getPosition();
			if (pos < postSetting.getPost_column() && keyCode == KeyEvent.KEYCODE_DPAD_UP
					&& titleView.getCurrentSelectedView() != null) {
				// 处理每次海报墙按上键时候选中当前标签
				titleView.getCurrentSelectedView().requestFocus();
			}
			return false;
		}
	};
}
