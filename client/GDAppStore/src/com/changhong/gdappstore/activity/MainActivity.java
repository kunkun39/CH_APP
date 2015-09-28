package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.MyApplication;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.MainViewPagerAdapter;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.base.BasePageView;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.net.LoadListener.LoadCompleteListener;
import com.changhong.gdappstore.service.NetChangeReceiver;
import com.changhong.gdappstore.service.NetChangeReceiver.NetChangeListener;
import com.changhong.gdappstore.service.UpdateService;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.DialogUtil.DialogBtnOnClickListener;
import com.changhong.gdappstore.util.DialogUtil.DialogMessage;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;
import com.changhong.gdappstore.view.HomePageView;
import com.changhong.gdappstore.view.MyProgressDialog;
import com.changhong.gdappstore.view.PostTitleView;
import com.changhong.gdappstore.view.PostTitleView.TitleItemOnClickListener;
import com.changhong.gdappstore.view.PostTitleView.TitleItemOnFocuesChangedListener;
import com.changhong.gdappstore.view.YouXiView;
import com.changhong.gdappstore.view.YuLeView;
import com.changhong.gdappstore.view.ShenghuoView;

/**
 * homepage
 * 
 * There are four tabs in this activity,so there are four item pages add to
 * ViewPager in this activity. Change the tab focus the item will be changed by
 * animation.
 * 
 * @author wangxiufeng
 * 
 */
public class MainActivity extends BaseActivity {
	/** 标签按钮view **/
	private PostTitleView titleView;
	/***/
	private ViewPager viewPager;
	/** viewpager适配器 */
	private MainViewPagerAdapter viewPagerAdapter;
	/** 每页view */
	private List<BasePageView> pageViews = new ArrayList<BasePageView>();
	/** 首页view */
	private HomePageView view_homepage;
	/** 娱乐view */
	private YuLeView view_yule;
	/** 应用view */
	private ShenghuoView view_zhuanti;
	/** 游戏view */
	private YouXiView view_youxi;
	/** viewpager 翻页动画 */
	private Animation anim_rightin, anim_leftin;
	/** viewpager 当前选中标签页 */
	private int currIndex = 0;
	/** 栏目分类数据 */
	private List<Category> categories = null;
	/** 设置按钮 */
	private ImageView iv_setting;
	/** 下载进度条 */
	private MyProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.d("mainactivity on create ");
		setContentView(R.layout.activity_main);
		initView();
		initOnCreateData();
	}

	private void initView() {
		// init title view
		titleView = findView(R.id.titleview);
		titleView.setTitleItemOnClickListener(titleItemOnClickListener);
		titleView.setTitleItemOnFocuesChangedListener(titleItemOnFocuesChangedListener);
		// init page views
		view_homepage = new HomePageView(context);
		view_zhuanti = new ShenghuoView(context);
		view_youxi = new YouXiView(context);
		view_yule = new YuLeView(context);
		// init view pager
		viewPager = findView(R.id.viewpager);
		viewPagerAdapter = new MainViewPagerAdapter(pageViews);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setAnimationCacheEnabled(true);
		viewPager.setOffscreenPageLimit(6);
		viewPager.setOnPageChangeListener(pageChangeListener);
		NetChangeReceiver.listeners.put(context.getClass().getName(),new NetChangeListener() {

			@Override
			public void onNetChange(boolean isconnect) {
				L.d("MainActivity---onnetchanged--" + isconnect + " " + Util.getTopActivity(context) + " ");
				DataCenter.getInstance().loadCategoryAndPageData(context, new LoadCompleteListener() {

					@Override
					public void onComplete() {
						if (Util.getTopActivity(context).equals(context.getClass().getName())) {// 网络从断开到链接更新数据
							initData();
						}
					}
				}, true);
			}
		});
		initPageChangeAnimtion();
		iv_setting = findView(R.id.iv_setting);
		iv_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS); // 系统设置
				startActivity(intent);
			}
		});
		progressDialog = new MyProgressDialog(context);
		progressDialog.setUpdateFileSizeName(true);
		progressDialog.dismiss();
	}

	private void initOnCreateData() {
		DataCenter.getInstance().loadPageApps(context, new LoadCompleteListener() {

			@Override
			public void onComplete() {
				initData();
			}
		}, true);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		categories = DataCenter.getInstance().getCategories();
		titleView.setMargin(0, 10);
		if (!pageViews.contains(view_homepage)) {
			pageViews.add(view_homepage);
		}
		view_homepage.initNativeData();
		if (categories != null) {
			for (int i = 0; i < categories.size(); i++) {
				L.d("mainactivity initdata category is " + categories.get(i));
				if (i > 3) {
					categories.remove(i);// TODO 第一阶段只显示4个标签，
					i--;
				}
			}
			titleView.initData(categories);

			// 目前是初始化默认数据
			for (int i = 0; i < categories.size(); i++) {
				if (i == 0) {
					view_homepage.initData(categories.get(0));
					view_homepage.setNextFocuesUpId(titleView.getItemTextViewAt(0).getId());
				} else if (i == 1) {
					view_yule.initData(categories.get(1));
					view_yule.setNextFocuesUpId(titleView.getItemTextViewAt(1).getId());
					if (!pageViews.contains(view_yule)) {
						pageViews.add(view_yule);
					}
				} else if (i == 2) {
					view_youxi.initData(categories.get(2));
					view_youxi.setNextFocuesUpId(titleView.getItemTextViewAt(2).getId());
					if (!pageViews.contains(view_youxi)) {
						pageViews.add(view_youxi);
					}
				} else if (i == 3) {
					view_zhuanti.initData(categories.get(3));
					view_zhuanti.setNextFocuesUpId(titleView.getItemTextViewAt(3).getId());
					if (!pageViews.contains(view_zhuanti)) {
						pageViews.add(view_zhuanti);
					}
				}
			}
		} else {
			L.d("mainactivity initdata category is null");
		}
		viewPagerAdapter.updateList(pageViews);
		titleView.setFocusItem(0);
		checkUpdate();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			break;
		case KeyEvent.KEYCODE_BACK:
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				DialogUtil.showMyAlertDialog(context, "提示：", "确认退出应用商城？", "确  认", "取  消",
						new DialogBtnOnClickListener() {

							@Override
							public void onSubmit(DialogMessage dialogMessage) {
								System.exit(0);
							}

							@Override
							public void onCancel(DialogMessage dialogMessage) {
								if (dialogMessage.dialogInterface != null) {
									dialogMessage.dialogInterface.dismiss();
								}
							}
						});
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
//		view_homepage.setShandows();
	}

	/**
	 * 初始化动画
	 */
	private void initPageChangeAnimtion() {
		int halfscreen = screenWidth / 2;
		int duration = 300;
		anim_leftin = new TranslateAnimation(-halfscreen, 0, 0, 0);
		anim_rightin = new TranslateAnimation(halfscreen, 0, 0, 0);
		anim_leftin.setFillAfter(false);
		anim_rightin.setFillAfter(false);
		anim_leftin.setDuration(duration);
		anim_rightin.setDuration(duration);
	}

	private TitleItemOnFocuesChangedListener titleItemOnFocuesChangedListener = new TitleItemOnFocuesChangedListener() {

		@Override
		public void onItemFocuesChanged(View view, boolean hasFocues, int position) {
			viewPager.setCurrentItem(position);
		}
	};

	private TitleItemOnClickListener titleItemOnClickListener = new TitleItemOnClickListener() {

		@Override
		public void onItemClick(View view, int position) {
			if (categories.get(position).getName().equals("首页")) {
				return;
			}
			Intent intent = new Intent(context, PostActivity.class);
			intent.putExtra(Config.KEY_PARENT_CATEGORYID, categories.get(position).getId());
			intent.putExtra(Config.KEY_CURRENT_CATEGORYID, categories.get(position).getId());
			context.startActivity(intent);
//			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	};
	/**
	 * 页卡切换监听
	 */
	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 < 0 || arg0 > pageViews.size() || pageViews.get(arg0) == null) {
				return;
			}
			BasePageView curPageView = pageViews.get(arg0);
			titleView.setSelectedItem(arg0);
			if (!titleView.hasChildFocesed()) {// 非标签上面切换情况下，处理默认交代呢
				if (currIndex == arg0 - 1) {// 从左往右翻页
					if (pageViews.get(currIndex).currentFocuesId == R.id.jingping_item12) {
						curPageView.setCategoryItemFocuesByPos(3);// 最底层一排翻页让第下一页最低层第一个获取焦点
					} else if (pageViews.get(currIndex).currentFocuesId == R.id.jingping_item11) {
						curPageView.setCategoryItemFocuesByPos(1);
					} else {
						curPageView.setCategoryItemFocuesByPos(0);// 其它情况让第一个获取焦点
					}
				} else if (currIndex == arg0 + 1) {// 从右往左翻页
					if (pageViews.get(currIndex).currentFocuesId == R.id.jingping_itema4) {
						curPageView.setPostItemFocuesByPos(11);// 最底层一排翻页让第上一页最低层最后一个获取焦点
					} else if (pageViews.get(currIndex).currentFocuesId == R.id.jingping_itema2
							|| pageViews.get(currIndex).currentFocuesId == R.id.jingping_itema3) {
						curPageView.setCategoryItemFocuesByPos(10);
					} else {
						curPageView.setPostItemFocuesByPos(9);// 其它情况让最后一列最上面一个获取焦点
					}
				}
			}
			if (currIndex == arg0 - 1) {
				curPageView.startAnimation(anim_rightin);// 从左往右翻页动画
			} else if (currIndex == arg0 + 1) {
				curPageView.startAnimation(anim_leftin);// 从右往左翻页动画
			}
			currIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	private Dialog updateDialog = null;

	private void checkUpdate() {
		try {
			int nativeVersion = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
			L.d("mainactivity readUpdate navVersion=" + nativeVersion + " serverVer " + MyApplication.SERVER_VERSION);
			if (NetworkUtils.isConnectInternet(context) && nativeVersion < MyApplication.SERVER_VERSION
					&& !TextUtils.isEmpty(MyApplication.UPDATE_APKURL)) {
				if (updateDialog == null || (updateDialog != null && !updateDialog.isShowing())) {

					updateDialog = DialogUtil.showMyAlertDialog(context, "提示：", "有新版本更新。", "马上更新", "下次再说",
							new DialogBtnOnClickListener() {

								@Override
								public void onSubmit(DialogMessage dialogMessage) {

									UpdateService updateService = new UpdateService(context, null, progressDialog);
									AppDetail appDetail = new AppDetail();
									appDetail.setApkFilePath(MyApplication.UPDATE_APKURL);
									updateService.update(appDetail, false);
									if (dialogMessage != null && dialogMessage.dialogInterface != null) {
										dialogMessage.dialogInterface.dismiss();
									}
								}

								@Override
								public void onCancel(DialogMessage dialogMessage) {
									if (dialogMessage != null && dialogMessage.dialogInterface != null) {
										dialogMessage.dialogInterface.dismiss();
									}
								}
							});
				}
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
