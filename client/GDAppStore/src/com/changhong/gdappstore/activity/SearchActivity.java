package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.net.LoadListener.LoadListListener;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
import com.changhong.gdappstore.post.PostSetting;
import com.changhong.gdappstore.post.PosterLayoutView;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.post.view.base.BasePosterLayoutView;
import com.post.view.listener.IPosteDateListener;
import com.post.view.listener.Listener.IItemOnClickListener;

/**
 * searchActivity
 * 
 * This activity used to search app by word or number. The search results will
 * be listed by GridView at the right side in this activity, the gridview will
 * be notifyDataSetChanged when you change the editText data.
 * 
 * @author wangxiufeng
 * 
 */
public class SearchActivity extends BaseActivity implements OnClickListener {

	private int[] id_keybords = { R.id.bt_fullkeybord_0, R.id.bt_fullkeybord_1, R.id.bt_fullkeybord_2,
			R.id.bt_fullkeybord_3, R.id.bt_fullkeybord_4, R.id.bt_fullkeybord_5, R.id.bt_fullkeybord_6,
			R.id.bt_fullkeybord_7, R.id.bt_fullkeybord_8, R.id.bt_fullkeybord_9, R.id.bt_fullkeybord_0,
			R.id.bt_fullkeybord_a, R.id.bt_fullkeybord_b, R.id.bt_fullkeybord_c, R.id.bt_fullkeybord_d,
			R.id.bt_fullkeybord_e, R.id.bt_fullkeybord_f, R.id.bt_fullkeybord_g, R.id.bt_fullkeybord_h,
			R.id.bt_fullkeybord_i, R.id.bt_fullkeybord_j, R.id.bt_fullkeybord_k, R.id.bt_fullkeybord_l,
			R.id.bt_fullkeybord_m, R.id.bt_fullkeybord_n, R.id.bt_fullkeybord_o, R.id.bt_fullkeybord_p,
			R.id.bt_fullkeybord_q, R.id.bt_fullkeybord_r, R.id.bt_fullkeybord_s, R.id.bt_fullkeybord_t,
			R.id.bt_fullkeybord_u, R.id.bt_fullkeybord_v, R.id.bt_fullkeybord_w, R.id.bt_fullkeybord_x,
			R.id.bt_fullkeybord_y, R.id.bt_fullkeybord_z };
	/** 输入框 */
	private EditText editText;
	/** 按钮：中文，回退，清楚，换一批 */
	private ImageView bt_backone, bt_space, bt_clear;
	/** 查询结果页码提示 */
	private TextView tv_searchresult;
	/** 查询结果 */
	private PosterLayoutView view_post;
	/** 海报配置 */
	private PostSetting postSetting;
	/** 搜索结果 */
	private List<Object> searchList = new ArrayList<Object>();
	/** 排行榜结果 */
	private List<Object> ranklist = new ArrayList<Object>();
	/** 请求缓存,LinkedHashMap有存数顺序，方便清理缓存 */
	private Map<String, List<Object>> cacheMap = new LinkedHashMap<String, List<Object>>();
	/**缓存保留数据数量*/
	private static final int CACHESIZE=30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		cacheMap = new LinkedHashMap<String, List<Object>>();
		initView();
		initPostView();
		initData();
		updateAppnumTextVisible();
	}

	private void initView() {
		for (int i = 0; i < id_keybords.length; i++) {
			findViewById(id_keybords[i]).setOnClickListener(keyBordOnClickListener);
		}
		findViewById(id_keybords[0]).requestFocus();
		editText = findView(R.id.edt_search);
		editText.addTextChangedListener(textWatcher);
		bt_backone = findView(R.id.bt_search_backone);
		bt_space = findView(R.id.bt_space);
		bt_clear = findView(R.id.bt_search_clear);
		tv_searchresult = findView(R.id.tv_num_searchresult);
		view_post = findView(R.id.post_search);

		bt_backone.setOnClickListener(this);
		bt_space.setOnClickListener(this);
		bt_clear.setOnClickListener(this);
	}

	private void initPostView() {

		// 海报墙设置，监听器没有可以设为空，行列设为负数则使用默认值
		postSetting = new PostSetting(3, 2, R.drawable.selector_bg_postitem, iPosteDateListener, null,
				postItemOnclickListener, null, null);
		postSetting.setVerticalScroll(true);// 纵向滚动
		postSetting.setVisibleClumn(1f);// 显示的页数
		postSetting.setMargins(0, 0, 0, 0);// item的距离
		postSetting.setFirstRowFocusUp(false);// 第一排是否允许焦点再往上
		postSetting.setFirstClumnFocusLeft(true);
		postSetting.setFristItemFocus(false);
		postSetting.setPosttype(PostSetting.TYPE_SEARCHAPP);
		// 如果需要海报墙使用自己的设置，要先调用设置设置方法，在调用设置数据
		view_post.init(postSetting);

	}

	private void initData() {
		List<Ranking_Item> rankingItems;
		rankingItems = RankingData.getInstance().getHotRankingData();
		if (rankingItems == null || rankingItems.size() <= 0) {
			DataCenter.getInstance().loadRankingList(context, new LoadObjectListener() {

				@Override
				public void onComplete(Object object) {
					List<Ranking_Item> rankingItems2 = RankingData.getInstance().getHotRankingData();
					updateRankListData(rankingItems2);
				}
			},true);
		} else {
			updateRankListData(rankingItems);
		}
	}

	public void updateRankListData(List<Ranking_Item> rankingItems) {
		if (rankingItems == null || rankingItems.size() <= 0) {
			return;
		}
		if (ranklist == null) {
			ranklist = new ArrayList<Object>();
		} else {
			ranklist.clear();
		}
		String host = RankingData.getInstance().getHost();
		for (int i = 0; i < rankingItems.size(); i++) {
			Ranking_Item ranking_Item = rankingItems.get(i);
			App app = new App();
			app.setAppid(ranking_Item.getAppId());
			app.setApkSize(ranking_Item.getAppSize());
			app.setAppkey(ranking_Item.getAppKey());
			app.setAppname(ranking_Item.getAppName());
			app.setDownload(ranking_Item.getDownload_num());
			app.setIconFilePath(host + ranking_Item.getAppKey() + "/" + ranking_Item.getAppIconPath());
			ranklist.add(app);
		}
		view_post.refreshAllData(ranklist, postSetting, ranklist.size());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_space:
			editText.append(" ");
			break;
		case R.id.bt_search_backone:
			String curEdtString = editText.getText().toString().trim();
			editText.setText(TextUtils.isEmpty(curEdtString) ? ""
					: curEdtString.substring(0, curEdtString.length() - 1));
			break;
		case R.id.bt_search_clear:
			editText.setText("");
			break;

		default:
			break;
		}
	}

	private OnClickListener keyBordOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Button button = (Button) v;
			editText.append(button.getText());
		}
	};

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			L.d("textWatcher ontextchanged--" + s + " " + start + " " + before + " " + count);

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			L.d("textWatcher beforeTextChanged--" + s + " " + start + " " + after + " " + count);

		}

		@Override
		public void afterTextChanged(final Editable s) {
			L.d("textWatcher afterTextChanged--" + s.toString());
			if (TextUtils.isEmpty(s.toString())) {
				initData();
				updateAppnumTextVisible();
			} else {
				if (cacheMap.containsKey(s.toString())) {
					// 有缓存就用缓存
					searchList.clear();
					searchList.addAll(cacheMap.get(s.toString()));
					view_post.refreshAllData(searchList, postSetting, searchList.size());
					updateAppnumTextVisible();
				} else {
					DataCenter.getInstance().loadAppSearch(s.toString(), new LoadListListener() {

						@Override
						public void onComplete(List<Object> items) {
							searchList.clear();
							searchList.addAll(items);
							view_post.refreshAllData(searchList, postSetting, searchList.size());
							updateAppnumTextVisible();
							int mapsize=cacheMap.size();
							if (mapsize > CACHESIZE) {
								Iterator iter = cacheMap.entrySet().iterator();
								int more=mapsize/10;// 清理最开始百分之10的缓存
								int delete=0;
								while (iter.hasNext()) {
									Map.Entry entry = (Map.Entry) iter.next();
									Object key = entry.getKey();
									cacheMap.remove(key);
									L.d("removed--key=="+key);
									if (++delete>=more) {
										break;
									}
								}
							}
							if (items == null) {
								items=new ArrayList<Object>();
							}
							cacheMap.put(s.toString(), items);
						}
					});
				}
			}
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
			Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
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
			// tv_page.setText("("+(totalpage<=0?0:curpage)+"/"+totalpage+")");
		}

		@Override
		public void lastPageOnKeyDpadDown() {
		}

		@Override
		public void firstPageOnKeyDpadup() {
		}
	};

	/**
	 * 跟新搜索列表上面文字和按钮显示 如果输入框内容为空，显示换一批按钮。如果有信息，显示搜索结果个数。
	 */
	private void updateAppnumTextVisible() {
		if (TextUtils.isEmpty(editText.getText().toString().trim())) {
			// 输入框为空时候，显示推荐。
			tv_searchresult.setVisibility(INVISIBLE);
			findViewById(R.id.tv_everybody_search).setVisibility(VISIBLE);
		} else {
			tv_searchresult.setVisibility(VISIBLE);
			findViewById(R.id.tv_everybody_search).setVisibility(INVISIBLE);
			int datasize = searchList.size();
			if (datasize > 0) {
				tv_searchresult.setText("当前搜索结果有：" + searchList.size() + " 个应用。");
			} else {
				tv_searchresult.setText("没有搜索到：“" + editText.getText().toString().trim() + "”相关应用，请重新输入搜索信息！");
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cacheMap.clear();
	}

}
