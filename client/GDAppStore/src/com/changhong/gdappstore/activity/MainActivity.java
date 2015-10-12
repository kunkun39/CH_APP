package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.changhong.gdappstore.view.OtherCategoryView;
import com.changhong.gdappstore.view.PostTitleView;
import com.changhong.gdappstore.view.PostTitleView.TitleItemOnClickListener;
import com.changhong.gdappstore.view.PostTitleView.TitleItemOnFocuesChangedListener;

/**
 * homepage
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

	private static final int PAGESIZE = Config.showCatPageSize + 1;

	private BasePageView[] homePages = new BasePageView[PAGESIZE];
	
	private static boolean isShowedUpdateDialog=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		L.d("mainactivity on create ");
		setContentView(R.layout.activity_main);
		initView();

	}

	private void initView() {
		// init title view
		titleView = findView(R.id.titleview);
		titleView.setTitleItemOnClickListener(titleItemOnClickListener);
		titleView.setTitleItemOnFocuesChangedListener(titleItemOnFocuesChangedListener);
		homePages[0] = new HomePageView(context);// 首页娱乐游戏应用

		NetChangeReceiver.listeners.put(context.getClass().getName(), new NetChangeListener() {

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

		// init view pager
		viewPager = findView(R.id.viewpager);
		viewPagerAdapter = new MainViewPagerAdapter(getHomePageList(homePages));

		viewPager.setCurrentItem(0);
		viewPager.setAnimationCacheEnabled(true);
		viewPager.setOffscreenPageLimit(6);
		viewPager.setOnPageChangeListener(pageChangeListener);
		viewPager.setAdapter(viewPagerAdapter);
		handler.sendEmptyMessageDelayed(0, 100);
	}

	private void initOnCreateData() {
		DataCenter.getInstance().loadCategoryAndPageData(context, new LoadCompleteListener() {

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
		titleView.setMargin(0, 0);
		((HomePageView) homePages[0]).initNativeData();
		if (categories != null) {
			for (int i = 0; i < categories.size(); i++) {
				if (i > Config.showCatPageSize) {
					categories.remove(i);// TODO 第一阶段只显示4个标签，
					i--;
				}
			}
			titleView.initData(categories);
			int categorieSize = categories.size();
			// 目前是初始化默认数据
			for (int i = 0; i < PAGESIZE - 1; i++) {
				if (categories.size() > i) {
					if (homePages[i] == null) {
						homePages[i] = new HomePageView(context);
					}
					((HomePageView) homePages[i]).initData(categories.get(i));
					((HomePageView) homePages[i]).setNextFocuesUpId(titleView.getItemTextViewAt(i).getId());
				}
			}
			if (hasOtherPage()) {
				// 最后一个是其它页面
				if (homePages[PAGESIZE - 1] == null) {
					homePages[PAGESIZE - 1] = new OtherCategoryView(context);
				}
				((OtherCategoryView) homePages[PAGESIZE - 1]).initData(categories.get(categorieSize - 1));
				((OtherCategoryView) homePages[PAGESIZE - 1]).setNextFocuesUpId(titleView.getItemTextViewAt(
						categorieSize - 1).getId());
			}
		} else {
			L.d("mainactivity initdata category is null");
		}
//		viewPagerAdapter.updateList(Arrays.asList(homePages));
		viewPagerAdapter.updateList(getHomePageList(homePages));
		titleView.setFocusItem(0);
		checkUpdate();
	}
	
	private List<View> getHomePageList(BasePageView[] basePageViews) {
		List<View> views=new ArrayList<View>();
		if (basePageViews==null || basePageViews.length==0) {
			return views;
		}
		for (int i = 0; i < basePageViews.length; i++) {
			if (basePageViews[i]!=null) {
				views.add(basePageViews[i]);
			}
		}
		return views;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
//			 viewPager.setAdapter(viewPagerAdapter);
			initOnCreateData();
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			// 标签按下选择第一个位置
			int titleFocusPos = titleView.getFocuesPosition();
			if (titleFocusPos >= 0 && viewPager.getCurrentItem() == titleFocusPos && titleFocusPos < homePages.length
					&& homePages[titleFocusPos] != null) {
				if (hasOtherPage() && titleFocusPos == categories.size() - 1) {
					if (((OtherCategoryView) homePages[titleFocusPos]).getOtcItemViews()[0] != null) {
						((OtherCategoryView) homePages[titleFocusPos]).getOtcItemViews()[0].requestFocus();
						return true;
					}
				} else {
					if (homePages[titleFocusPos].getCategoryItemViews()[0] != null) {
						homePages[titleFocusPos].getCategoryItemViews()[0].requestFocus();
						return true;
					}
				}
			}
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

	private boolean hasOtherPage() {
		return categories == null ? false : categories.size() > Config.showCatPageSize;
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
		// view_homepage.setShandows();
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
			// overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	};
	/**
	 * 页卡切换监听
	 */
	OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			if (arg0 < 0 || arg0 > homePages.length || homePages[arg0] == null) {
				return;
			}
			BasePageView curPageView = homePages[arg0];
			titleView.setSelectedItem(arg0);
			if (!titleView.hasChildFocuesed()) {// 非标签上面切换情况下，处理默认交代呢
				if (hasOtherPage() && arg0 == categories.size() - 1) {
					// 当前页面是其它页面
					if (currIndex == arg0 - 1) {// 从左往右翻页
						if (homePages[currIndex].currentFocuesId == R.id.homepage_item12) {
							((OtherCategoryView) curPageView).setOtcItemFocuesByPos(1);// 最底层一排翻页让第下一页最低层第一个获取焦点
						} else {
							((OtherCategoryView) curPageView).setOtcItemFocuesByPos(0);// 其它情况让第一个获取焦点
						}
					}
				} else if (hasOtherPage() && arg0 == PAGESIZE - 2) {
					// 当前页面是其它页面的上一个页面
					if (currIndex == arg0 + 1) {// 从右往左翻页
						if (homePages[currIndex].currentFocuesId == R.id.othercat_item2) {
							curPageView.setPostItemFocuesByPos(11);// 最底层一排翻页让第上一页最低层最后一个获取焦点
						} else {
							curPageView.setPostItemFocuesByPos(9);// 其它情况让最后一列最上面一个获取焦点
						}
					}
				} else {
					if (currIndex == arg0 - 1) {// 从左往右翻页
						if (homePages[currIndex].currentFocuesId == R.id.homepage_item12) {
							curPageView.setCategoryItemFocuesByPos(3);// 最底层一排翻页让第下一页最低层第一个获取焦点
						} else if (homePages[currIndex].currentFocuesId == R.id.homepage_item11) {
							curPageView.setCategoryItemFocuesByPos(1);
						} else {
							curPageView.setCategoryItemFocuesByPos(0);// 其它情况让第一个获取焦点
						}
					} else if (currIndex == arg0 + 1) {// 从右往左翻页
						if (homePages[currIndex].currentFocuesId == R.id.homepage_itema4) {
							curPageView.setPostItemFocuesByPos(11);// 最底层一排翻页让第上一页最低层最后一个获取焦点
						} else if (homePages[currIndex].currentFocuesId == R.id.homepage_itema2
								|| homePages[currIndex].currentFocuesId == R.id.homepage_itema3) {
							curPageView.setCategoryItemFocuesByPos(10);
						} else {
							curPageView.setPostItemFocuesByPos(9);// 其它情况让最后一列最上面一个获取焦点
						}
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
					&& !TextUtils.isEmpty(MyApplication.UPDATE_APKURL)&&!isShowedUpdateDialog ) {
				if ( updateDialog == null || (updateDialog != null && !updateDialog.isShowing())) {
					isShowedUpdateDialog=true;
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
