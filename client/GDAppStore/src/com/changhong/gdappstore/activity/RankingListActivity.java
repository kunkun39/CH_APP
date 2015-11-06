package com.changhong.gdappstore.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.RankingListViewAdapter;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
import com.changhong.gdappstore.util.DialogUtil;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.NetworkUtils;
import com.changhong.gdappstore.util.Util;
import com.changhong.gdappstore.view.FocusView;
import com.changhong.gdappstore.view.ListViewChange;
import com.changhong.gdappstore.view.ListViewPosition;

public class RankingListActivity extends Activity {
	public static final int LOAD_RANKING_OK = 0x1001;
	public static final int LOAD_RANKING_FAIL = 0x1002;
	public static final int TOP_MARGIN = 197;
	public static final int LEFT_MARGIN_NEW = 107;
	public static final int LEFT_MARGIN_HOT = 466;
	public static final int LEFT_MARGIN_SURGE = 826;
	public static final int ITEM_WIDTH = 345;
	public static final int ITEM_HEIGHT = 116;
	public static final int LISTVIEW_HEIGHT = 440;
	private ListView listView_new;
	private ListView listView_hot;
	private ListView listView_surge;
	private RelativeLayout focusItem;
	RelativeLayout.LayoutParams mlayout;
	private ArrayList<Ranking_Item> newArrayList = new ArrayList<Ranking_Item>();
	private ArrayList<Ranking_Item> hotArrayList = new ArrayList<Ranking_Item>();
	private ArrayList<Ranking_Item> surgeArrayList = new ArrayList<Ranking_Item>();
	
	private RankingListViewAdapter newArrayListAdapter;
	private RankingListViewAdapter hotArrayListAdapter;
	private RankingListViewAdapter surgeArrayListAdapter;
	
	private FocusView focusView;
	private ListViewPosition newListViewPosition;
	private ListViewPosition hotListViewPosition;
	private ListViewPosition surgeListViewPosition;
	private ListViewChange listViewChange;
	private ProgressDialog dialog;
	
	enum FocusSelect{
		NEW_LISTVIEW,
		HOT_LISTVIEW,
		SURGE_LISTVIEW
	}
	//记录listview焦点
	private FocusSelect currFocusSelect = FocusSelect.NEW_LISTVIEW;

	private DataCenter dataCenter;

	public RankingListActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking_list);
		dialog = DialogUtil.showCirculProDialog(this, getString(R.string.tishi), getString(R.string.dataloading), true);
		initData();
		initView();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		int offset;
		int position;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(currFocusSelect == FocusSelect.NEW_LISTVIEW) {
				//当前foucus在最左边，不做任何处理
			}
			else if(currFocusSelect == FocusSelect.HOT_LISTVIEW) {
				//移动到newListview
				currFocusSelect = FocusSelect.NEW_LISTVIEW;
				
				listView_new.setFocusable(true);
				listView_hot.setFocusable(false);
				listView_surge.setFocusable(false);
				listView_new.requestFocus();
				
				if(!Util.listIsEmpty(newArrayList)) {
					position = newListViewPosition.caculateAbsolutePosition(hotListViewPosition.getRelativePosition());
					offset = newListViewPosition.caculateOffset(position,false);
					focusView.setArrayList(newArrayList);
					if(newListViewPosition.getEndNumPosition() == listView_new.getCount() - 1 && newListViewPosition.getStartNumPosition() != 0) {
						listView_new.setSelection(position);
						focusView.focusViewChange(position, LEFT_MARGIN_NEW, offset + TOP_MARGIN - 4);
					}
					else {
						listView_new.setSelectionFromTop(position,offset);
						focusView.focusViewChange(position, LEFT_MARGIN_NEW, offset + TOP_MARGIN);
					}
					//重绘hotListview
					hotArrayListAdapter.notifyDataSetChanged();
					View localView = listView_new.getChildAt(position - listView_new.getFirstVisiblePosition());
					listViewChange.hideBackground((RelativeLayout) localView.findViewById(R.id.ranking_bg));
					L.i("开始位置：" + listView_new.getFirstVisiblePosition());
					L.i("选择位置：" + listView_new.getSelectedItemPosition());
				}
			}
			else if(currFocusSelect == FocusSelect.SURGE_LISTVIEW) {
				//移动到newListview
				currFocusSelect = FocusSelect.HOT_LISTVIEW;
				
				listView_new.setFocusable(false);
				listView_hot.setFocusable(true);
				listView_surge.setFocusable(false);
				listView_hot.requestFocus();
				
				position = hotListViewPosition.caculateAbsolutePosition(surgeListViewPosition.getRelativePosition());
				offset = hotListViewPosition.caculateOffset(position,false);
				focusView.setArrayList(hotArrayList);
				if(!Util.listIsEmpty(hotArrayList)) {
					if(hotListViewPosition.getEndNumPosition() == listView_hot.getCount() - 1 && hotListViewPosition.getStartNumPosition() != 0) {
						listView_hot.setSelection(position);
						focusView.focusViewChange(position, LEFT_MARGIN_HOT, offset + TOP_MARGIN - 4);
					}
					else {
						listView_hot.setSelectionFromTop(position,offset);
						focusView.focusViewChange(position, LEFT_MARGIN_HOT, offset + TOP_MARGIN);
					}
					//重绘hotListview
					surgeArrayListAdapter.notifyDataSetChanged();
					
					View localView = listView_hot.getChildAt(position - listView_hot.getFirstVisiblePosition());
					listViewChange.hideBackground((RelativeLayout) localView.findViewById(R.id.ranking_bg));
					L.i("开始位置：" + listView_hot.getFirstVisiblePosition());
					L.i("选择位置：" + listView_hot.getSelectedItemPosition());
				}
			}
			else {
				//不做任何处理
			}
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(currFocusSelect == FocusSelect.NEW_LISTVIEW) {
				//移动到newListview
				currFocusSelect = FocusSelect.HOT_LISTVIEW;
				
				listView_new.setFocusable(false);
				listView_hot.setFocusable(true);
				listView_surge.setFocusable(false);
				
				listView_hot.requestFocus();
				
				position = hotListViewPosition.caculateAbsolutePosition(newListViewPosition.getRelativePosition());
				offset = hotListViewPosition.caculateOffset(position,false);
				focusView.setArrayList(hotArrayList);
				
				if(!Util.listIsEmpty(hotArrayList)) {
					if(hotListViewPosition.getEndNumPosition() == listView_hot.getCount() - 1 && hotListViewPosition.getStartNumPosition() != 0) {
						listView_hot.setSelection(position);
						focusView.focusViewChange(position, LEFT_MARGIN_HOT, offset + TOP_MARGIN - 4);
					}
					else {
						listView_hot.setSelectionFromTop(position,offset);
						focusView.focusViewChange(position, LEFT_MARGIN_HOT, offset + TOP_MARGIN);
					}
					//重绘hotListview
					newArrayListAdapter.notifyDataSetChanged();
					
					View localView = listView_hot.getChildAt(position - listView_hot.getFirstVisiblePosition());
					listViewChange.hideBackground((RelativeLayout) localView.findViewById(R.id.ranking_bg));
					L.i("开始位置：" + listView_hot.getFirstVisiblePosition());
					L.i("选择位置：" + listView_hot.getSelectedItemPosition());
				}
			}
			else if(currFocusSelect == FocusSelect.HOT_LISTVIEW) {
				//移动到surgeListview
				currFocusSelect = FocusSelect.SURGE_LISTVIEW;
				
				listView_new.setFocusable(false);
				listView_hot.setFocusable(false);
				listView_surge.setFocusable(true);
				listView_surge.requestFocus();
				
				position = surgeListViewPosition.caculateAbsolutePosition(hotListViewPosition.getRelativePosition());
				offset = surgeListViewPosition.caculateOffset(position,false);
				focusView.setArrayList(surgeArrayList);
				
				if(!Util.listIsEmpty(surgeArrayList)) {
					if(surgeListViewPosition.getEndNumPosition() == listView_surge.getCount() - 1 && surgeListViewPosition.getStartNumPosition() != 0) {
						listView_surge.setSelection(position);
						focusView.focusViewChange(position, LEFT_MARGIN_SURGE, offset + TOP_MARGIN - 4);
					}
					else {
						listView_surge.setSelectionFromTop(position,offset);
						focusView.focusViewChange(position, LEFT_MARGIN_SURGE, offset + TOP_MARGIN);
					}
					//重绘surgeListview
					hotArrayListAdapter.notifyDataSetChanged();
					
					View localView = listView_surge.getChildAt(position - listView_surge.getFirstVisiblePosition());
					listViewChange.hideBackground((RelativeLayout) localView.findViewById(R.id.ranking_bg));
					L.i("开始位置：" + listView_surge.getFirstVisiblePosition());
					L.i("选择位置：" + listView_surge.getSelectedItemPosition());
				}
			}
			else if(currFocusSelect == FocusSelect.SURGE_LISTVIEW) {
				//当前foucus在最右边，不做任何处理
			}
			else {
				//不做任何处理
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void initView() {
		listView_new = (ListView)findViewById(R.id.listView_new);
		listView_hot = (ListView)findViewById(R.id.listView_hot);
		listView_surge = (ListView)findViewById(R.id.listView_surge);
		focusItem = (RelativeLayout)findViewById(R.id.focus_item);
		
		listViewChange = new ListViewChange();
		
		listView_new.setVisibility(View.INVISIBLE);
		listView_hot.setVisibility(View.INVISIBLE);
		listView_surge.setVisibility(View.INVISIBLE);
		
		((TextView)findViewById(R.id.ranking_list)).setText(getString(R.string.rank_list));
		mlayout = new RelativeLayout.LayoutParams(0, 0);
		
		focusView = new FocusView(RankingListActivity.this, focusItem, ITEM_WIDTH, ITEM_HEIGHT);

		newListViewPosition = new ListViewPosition(newArrayList.size(), LISTVIEW_HEIGHT, ITEM_HEIGHT);
		newArrayListAdapter = new RankingListViewAdapter(this, newArrayList);

		listView_new.setVerticalScrollBarEnabled(false);
		listView_new.setOnItemSelectedListener(newOnItemSelectedListener);
		listView_new.setOnItemClickListener(newOnItemClickListener);

		hotListViewPosition = new ListViewPosition(hotArrayList.size(), LISTVIEW_HEIGHT, ITEM_HEIGHT);
		hotArrayListAdapter = new RankingListViewAdapter(this, hotArrayList);
		
		listView_hot.setVerticalScrollBarEnabled(false);
		listView_hot.setOnItemSelectedListener(hotOnItemSelectedListener);
		listView_hot.setOnItemClickListener(hotOnItemClickListener);

		surgeListViewPosition = new ListViewPosition(surgeArrayList.size(), LISTVIEW_HEIGHT, ITEM_HEIGHT);
		surgeArrayListAdapter = new RankingListViewAdapter(this, surgeArrayList);
		
		listView_surge.setVerticalScrollBarEnabled(false);
		listView_surge.setOnItemSelectedListener(surgeOnItemSelectedListener);
		listView_surge.setOnItemClickListener(surgeItemClickListener);
		
		listView_new.requestFocus();
		listView_hot.setFocusable(false);
		listView_surge.setFocusable(false);
	}
	
	private void initData() {
		dataCenter = DataCenter.getInstance();
		dataCenter.loadRankingList(this, new LoadObjectListener() {
			
			@Override
			public void onComplete(Object object) {
				// TODO Auto-generated method stub
				boolean dismissDialog = false;
				L.i("loadRankingList result : " + (Integer)object);
				switch((Integer)object) {
				case DataCenter.LOAD_CACHEDATA_SUCCESS :
					handler.sendEmptyMessage(LOAD_RANKING_OK);
					break;
				case DataCenter.LOAD_CACHEDATA_NO_UPDATE:
					dismissDialog = true;
					break;
				case DataCenter.LOAD_SERVERDATA_SUCCESS:
					handler.sendEmptyMessage(LOAD_RANKING_OK);
					dismissDialog = true;
					break;
				case DataCenter.LOAD_SERVERDATA_FAIL:
					handler.sendEmptyMessage(LOAD_RANKING_FAIL);
					dismissDialog = true;
					break;
				}
				
				if(true == dismissDialog && dialog != null) {
					dialog.dismiss();
				}
				
			}
		},true);
	}
	
	private void jumpToDetailActivity(ArrayList<Ranking_Item> arrayList, int position) {
		if(arrayList == null || arrayList.isEmpty()) {
			L.w("jumpToDetailActivity : arrayList error!");
			return ;
		}
		if(position >= arrayList.size()) {
			L.w("jumpToDetailActivity : position error!");
			return ;
		}
		
//		if (!NetworkUtils.ISNET_CONNECT) {
//			DialogUtil.showShortToast(this, this.getString(R.string.net_disconnected_pleasecheck));
//			return;
//		}
		
		Intent intent = new Intent(this, DetailActivity.class);
		intent.putExtra(Config.KEY_APPID, arrayList.get(position).getAppId());
		startActivity(intent);
	}

	private OnItemSelectedListener newOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(currFocusSelect == FocusSelect.NEW_LISTVIEW) {
				L.i("当前listView_new选择为：" + position + "FirstVisiblePosition:" + listView_new.getFirstVisiblePosition());
				int offset = newListViewPosition.caculateOffset(position,true);
				if(-1 != offset || !focusView.hasChanged()) {
					focusView.setArrayList(newArrayList);
					listViewChange.hideBackground((RelativeLayout)view.findViewById(R.id.ranking_bg));
					if(newListViewPosition.getEndNumPosition() == listView_new.getCount() - 1 && newListViewPosition.getStartNumPosition() != 0) {
						listView_new.setSelection(position);
						focusView.focusViewChange(position, LEFT_MARGIN_NEW, offset + TOP_MARGIN - 4);
					}
					else {
						listView_new.setSelectionFromTop(position,offset);
						focusView.focusViewChange(position, LEFT_MARGIN_NEW, offset + TOP_MARGIN);
					}
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private OnItemSelectedListener hotOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(currFocusSelect == FocusSelect.HOT_LISTVIEW) {
				L.i("当前listView_hot选择为：" + position + "FirstVisiblePosition:" + listView_hot.getFirstVisiblePosition());
				
				int offset = hotListViewPosition.caculateOffset(position,true);
				if(-1 != offset || !focusView.hasChanged()) {
					listViewChange.hideBackground((RelativeLayout) view.findViewById(R.id.ranking_bg));
					focusView.setArrayList(hotArrayList);
					if(hotListViewPosition.getEndNumPosition() == listView_hot.getCount() - 1 && hotListViewPosition.getStartNumPosition() != 0) {
						listView_hot.setSelection(position);
						focusView.focusViewChange(position, LEFT_MARGIN_HOT, offset + TOP_MARGIN - 4);
					}
					else {
						listView_hot.setSelectionFromTop(position,offset);
						focusView.focusViewChange(position, LEFT_MARGIN_HOT, offset + TOP_MARGIN);
					}
				}
				
				//focusViewChange(listView_hot,position,465,180);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	};
	private OnItemSelectedListener surgeOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(currFocusSelect == FocusSelect.SURGE_LISTVIEW) {
				L.i("当前listView_surge选择为：" + position + "FirstVisiblePosition:" + listView_surge.getFirstVisiblePosition());
			
				int offset = surgeListViewPosition.caculateOffset(position,true);
				if(-1 != offset || !focusView.hasChanged()) {
					listViewChange.hideBackground((RelativeLayout) view.findViewById(R.id.ranking_bg));
					focusView.setArrayList(surgeArrayList);
					if(surgeListViewPosition.getEndNumPosition() == listView_surge.getCount() - 1 && surgeListViewPosition.getStartNumPosition() != 0) {
						listView_surge.setSelection(position);
						focusView.focusViewChange(position, LEFT_MARGIN_SURGE, offset + TOP_MARGIN - 4);
					}
					else {
						listView_surge.setSelectionFromTop(position,offset);
						focusView.focusViewChange(position, LEFT_MARGIN_SURGE, offset + TOP_MARGIN);
					}
				}
				
				//focusViewChange(listView_surge,position,825,180);
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg != null) {
				L.i("Handler msg : " + msg.what);
				switch(msg.what) {
				case LOAD_RANKING_OK:
					RankingData rankingData = RankingData.getInstance();
					ArrayList<Ranking_Item> newList = rankingData.getNewRankingData();
					ArrayList<Ranking_Item> hotList = rankingData.getHotRankingData();
					ArrayList<Ranking_Item> surgeList = rankingData.getSurgeRankingData();
					
					if(newList != null) {
						L.i(newList.toString());
						if(listView_new.getVisibility() != View.VISIBLE) {
							listView_new.setVisibility(View.VISIBLE);
						}
						newArrayList = newList;
						newArrayListAdapter = new RankingListViewAdapter(RankingListActivity.this, newArrayList);

						newListViewPosition.resetParameter(newArrayList.size());
						if(currFocusSelect == FocusSelect.NEW_LISTVIEW) {
							focusView.setArrayList(newArrayList);
							focusView.refreshView();
							focusView.reset();
						}
						listView_new.setAdapter(newArrayListAdapter);
						newArrayListAdapter.notifyDataSetChanged();
					}
					if(hotList != null) {
						L.i(hotList.toString());
						if(listView_hot.getVisibility() != View.VISIBLE) {
							listView_hot.setVisibility(View.VISIBLE);
						}
						hotArrayList = hotList;
						hotArrayListAdapter = new RankingListViewAdapter(RankingListActivity.this, hotArrayList);

						hotListViewPosition.resetParameter(hotArrayList.size());
						if(currFocusSelect == FocusSelect.HOT_LISTVIEW) {
							focusView.setArrayList(hotArrayList);
							focusView.refreshView();
							focusView.reset();
						}
						listView_hot.setAdapter(hotArrayListAdapter);
						hotArrayListAdapter.notifyDataSetChanged();
					}
					if(surgeList != null) {
						L.i(surgeList.toString());
						if(listView_surge.getVisibility() != View.VISIBLE) {
							listView_surge.setVisibility(View.VISIBLE);
						}
						surgeArrayList = surgeList;
						surgeArrayListAdapter = new RankingListViewAdapter(RankingListActivity.this, surgeArrayList);

						surgeListViewPosition.resetParameter(surgeArrayList.size());
						if(currFocusSelect == FocusSelect.SURGE_LISTVIEW) {
							focusView.setArrayList(surgeArrayList);
							focusView.refreshView();
							focusView.reset();
						}
						listView_surge.setAdapter(surgeArrayListAdapter);
						surgeArrayListAdapter.notifyDataSetChanged();
					}
					break;
				case LOAD_RANKING_FAIL:
					Toast.makeText(RankingListActivity.this, getApplicationContext().getString(R.string.error_netconnect_please_checknet), Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			}
		};
	};
	private AdapterView.OnItemClickListener newOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			jumpToDetailActivity(newArrayList, position);
		}
	};
	private AdapterView.OnItemClickListener hotOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			jumpToDetailActivity(hotArrayList, position);
		}
	};
	private AdapterView.OnItemClickListener surgeItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			jumpToDetailActivity(surgeArrayList, position);
		}
	};
}
