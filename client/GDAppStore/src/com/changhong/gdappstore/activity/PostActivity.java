package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Toast;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.model.PostTitleModel;
import com.changhong.gdappstore.post.PostModel;
import com.changhong.gdappstore.post.PostSetting;
import com.changhong.gdappstore.post.PosterLayoutView;
import com.changhong.gdappstore.view.PostTitleView;
import com.post.view.listener.IPosteDateListener;

/**
 * 海报墙页面
 * 
 * @author wangxiufeng
 * 
 */
public class PostActivity extends BaseActivity {
	private PosterLayoutView postView;
	private PostSetting postSetting;
	private PostTitleView titleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		initView();
		initPostView();
		initData();
	}

	private void initView() {
		postView = findView(R.id.postview);
		titleView=findView(R.id.posttitleview);
	}

	private void initPostView() {
		
		// 海报墙设置，监听器没有可以设为空，行列设为负数则使用默认值
		postSetting = new PostSetting(3, 3, R.drawable.selector_bg_postitem, iPosteDateListener, null, null, null,
				postOnKeyListener);
		postSetting.setVerticalScroll(false);
		postSetting.setVisibleClumn(1.14f);
		postSetting.setMargins(10, 10, 0, 0);
//		postSetting.setPagePadding(0, 0, 0, 0);
		postSetting.setFirstRowFocusUp(true);
		postSetting.setFristItemFocus(true);
		// 如果需要海报墙使用自己的设置，要先调用设置设置方法，在调用设置数据
		postView.init(postSetting);
		
	}

	private void initData() {
		List<PostTitleModel> titleModels=new ArrayList<PostTitleModel>();
		titleModels.add(new PostTitleModel("全部"));
		titleModels.add(new PostTitleModel("棋牌"));
		titleModels.add(new PostTitleModel("休闲"));
		titleModels.add(new PostTitleModel("动作"));
		titleModels.add(new PostTitleModel("体育"));
		titleModels.add(new PostTitleModel("角色"));
		titleModels.add(new PostTitleModel("设计"));
		titleModels.add(new PostTitleModel("体感"));
		titleView.initData(titleModels);
		
		
		List<Object> loadItems = new ArrayList<Object>();
		for (int i = 0; i < 50; i++) {
			loadItems.add(new PostModel(i, R.drawable.img_post1, "应用名字应用名字应用名字" + i, 1.5f, "这是第" + i + "个应用"));
		}
		postView.initData(loadItems, loadItems.size());
		
	}

	private IPosteDateListener iPosteDateListener = new IPosteDateListener() {

		@Override
		public void requestNextPageDate(int currentSize) {
			// List<Object> loadItems = new ArrayList<Object>();
			// for (int i = currentSize; i < currentSize + 20; i++) {
			// loadItems.add(new PostModel(i, ("标签 " + i), R.drawable.img_post_1
			// + i % 5));
			// }
			// view_post.initData(loadItems, totalnum);
		}

		@Override
		public void changePage(Boolean isnext, int curpage, int totalpage) {
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
			// int pos = ((PostItem) v).getPosition();
			return false;
		}
	};
}
