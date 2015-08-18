package com.changhong.gdappstore.activity;

import java.util.ArrayList;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.RankingListViewAdapter;
import com.changhong.gdappstore.datacenter.DataCenter;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
import com.changhong.gdappstore.test.Ranking_data_test;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.view.FocusView;
import com.changhong.gdappstore.view.ListViewChange;
import com.changhong.gdappstore.view.ListViewPosition;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class RankingListActivity extends Activity {
	public static final int LOAD_RANKING_OK = 0x1001;
	public static final int LOAD_RANKING_FAIL = 0x1002;
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
	
	enum FocusSelect{
		NEW_LISTVIEW,
		HOT_LISTVIEW,
		SURGE_LISTVIEW
	}
	//记录listview焦点
	private FocusSelect currFocusSelect = FocusSelect.NEW_LISTVIEW;
	
	private Ranking_data_test data_test;
	private DataCenter dataCenter;

	public RankingListActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking_list);
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
				
				position = newListViewPosition.caculateAbsolutePosition(hotListViewPosition.getRelativePosition());
				offset = newListViewPosition.caculateOffset(position,false);
				listView_new.setSelectionFromTop(position,offset);
				focusView.setArrayList(newArrayList);
				focusView.focusViewChange(position, 105, offset + 180);
//				newArrayListAdapter.setSelectionNum(0);
//				newArrayListAdapter.notifyDataSetChanged();
//				focusViewChange(listView_new,0,105,180);
				//重绘hotListview
				hotArrayListAdapter.notifyDataSetChanged();
				View localView = listView_new.getChildAt(position - listView_new.getFirstVisiblePosition());
				listViewChange.hideView((RelativeLayout) localView.findViewById(R.id.ranking_item));
				L.i("开始位置：" + listView_new.getFirstVisiblePosition());
				L.i("选择位置：" + listView_new.getSelectedItemPosition());
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
				listView_hot.setSelectionFromTop(position,offset);
				focusView.setArrayList(hotArrayList);
				focusView.focusViewChange(position, 465, offset + 180);
//				hotArrayListAdapter.setSelectionNum(0);
//				hotArrayListAdapter.notifyDataSetChanged();
//				focusViewChange(listView_hot,0,465,180);
				//重绘hotListview
				surgeArrayListAdapter.notifyDataSetChanged();
				
				View localView = listView_hot.getChildAt(position - listView_hot.getFirstVisiblePosition());
				listViewChange.hideView((RelativeLayout) localView.findViewById(R.id.ranking_item));
				L.i("开始位置：" + listView_hot.getFirstVisiblePosition());
				L.i("选择位置：" + listView_hot.getSelectedItemPosition());
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
				listView_hot.setSelectionFromTop(position,offset);
				focusView.setArrayList(hotArrayList);
				focusView.focusViewChange(position, 465, offset + 180);
//				hotArrayListAdapter.setSelectionNum(0);
//				hotArrayListAdapter.notifyDataSetChanged();
//				focusViewChange(listView_hot,0,465,180);
				//重绘hotListview
				newArrayListAdapter.notifyDataSetChanged();
				
				View localView = listView_hot.getChildAt(position - listView_hot.getFirstVisiblePosition());
				listViewChange.hideView((RelativeLayout) localView.findViewById(R.id.ranking_item));
				L.i("开始位置：" + listView_hot.getFirstVisiblePosition());
				L.i("选择位置：" + listView_hot.getSelectedItemPosition());
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
				listView_surge.setSelectionFromTop(position,offset);
				focusView.setArrayList(surgeArrayList);
				focusView.focusViewChange(position, 825, offset + 180);
//				surgeArrayListAdapter.setSelectionNum(0);
//				surgeArrayListAdapter.notifyDataSetChanged();
//				focusViewChange(listView_surge,0,825,180);
				//重绘surgeListview
				hotArrayListAdapter.notifyDataSetChanged();
				
				View localView = listView_surge.getChildAt(position - listView_surge.getFirstVisiblePosition());
				listViewChange.hideView((RelativeLayout) localView.findViewById(R.id.ranking_item));
				L.i("开始位置：" + listView_surge.getFirstVisiblePosition());
				L.i("选择位置：" + listView_surge.getSelectedItemPosition());
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
		listViewChange = new ListViewChange();
		data_test = new Ranking_data_test(this);
		listView_new = (ListView)findViewById(R.id.listView_new);
		listView_hot = (ListView)findViewById(R.id.listView_hot);
		listView_surge = (ListView)findViewById(R.id.listView_surge);
		focusItem = (RelativeLayout)findViewById(R.id.focus_item);
		mlayout = new RelativeLayout.LayoutParams(0, 0);
		
		focusView = new FocusView(RankingListActivity.this, focusItem, 350, 118);
		
		newArrayList = data_test.getNewArrayList();
		
		newListViewPosition = new ListViewPosition(newArrayList.size(), 450, 118);
		
		newArrayListAdapter = new RankingListViewAdapter(this, newArrayList);
		
		listView_new.setAdapter(newArrayListAdapter);
		
		listView_new.setVerticalScrollBarEnabled(false);
		listView_new.setOnItemSelectedListener(newOnItemSelectedListener);
		
		hotArrayList = data_test.getHotArrayList();
		hotListViewPosition = new ListViewPosition(hotArrayList.size(), 450, 118);
		hotArrayListAdapter = new RankingListViewAdapter(this, hotArrayList);
		
		listView_hot.setAdapter(hotArrayListAdapter);
		listView_hot.setVerticalScrollBarEnabled(false);
		listView_hot.setOnItemSelectedListener(hotOnItemSelectedListener);
		
		surgeArrayList = data_test.getSurgeHotArrayList();
		surgeListViewPosition = new ListViewPosition(surgeArrayList.size(), 450, 118);
		surgeArrayListAdapter = new RankingListViewAdapter(this, surgeArrayList);
		
		listView_surge.setAdapter(surgeArrayListAdapter);
		listView_surge.setVerticalScrollBarEnabled(false);
		listView_surge.setOnItemSelectedListener(surgeOnItemSelectedListener);
		
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
				if((Boolean)object == true) {
					handler.sendEmptyMessage(LOAD_RANKING_OK);
				}
				else {
					handler.sendEmptyMessage(LOAD_RANKING_FAIL);
				}
			}
		});
	}

	private OnItemSelectedListener newOnItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			if(currFocusSelect == FocusSelect.NEW_LISTVIEW) {
				L.i("当前listView_hot选择为：" + position + "FirstVisiblePosition:" + listView_hot.getFirstVisiblePosition());
				
				int offset = newListViewPosition.caculateOffset(position,true);
				if(-1 != offset || !focusView.hasChanged()) {
					listViewChange.hideView((RelativeLayout) view.findViewById(R.id.ranking_item));
					
					listView_new.setSelectionFromTop(position,offset);
					focusView.setArrayList(newArrayList);
					focusView.focusViewChange(position, 105, offset + 180);
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
				if(-1 != offset) {
					listViewChange.hideView((RelativeLayout) view.findViewById(R.id.ranking_item));
					
					listView_hot.setSelectionFromTop(position,offset);
					focusView.setArrayList(hotArrayList);
					focusView.focusViewChange(position, 465, offset + 180);
					
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
				if(-1 != offset) {
					listViewChange.hideView((RelativeLayout) view.findViewById(R.id.ranking_item));
					
					listView_surge.setSelectionFromTop(position,offset);
					focusView.setArrayList(surgeArrayList);
					focusView.focusViewChange(position, 825, offset + 180);
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
						newArrayList = newList;
						newArrayListAdapter = new RankingListViewAdapter(RankingListActivity.this, newArrayList);

						newListViewPosition.resetParameter(newArrayList.size());
						listView_new.setAdapter(newArrayListAdapter);
						newArrayListAdapter.notifyDataSetChanged();
						
						if(currFocusSelect == FocusSelect.NEW_LISTVIEW) {
							focusView.setArrayList(newArrayList);
							focusView.refreshView();
						}
					}
					if(hotList != null) {
						L.i(hotList.toString());
						hotArrayList = hotList;
						hotArrayListAdapter = new RankingListViewAdapter(RankingListActivity.this, hotArrayList);

						hotListViewPosition.resetParameter(hotArrayList.size());
						listView_hot.setAdapter(hotArrayListAdapter);
						hotArrayListAdapter.notifyDataSetChanged();
						
						if(currFocusSelect == FocusSelect.HOT_LISTVIEW) {
							focusView.setArrayList(hotArrayList);
							focusView.refreshView();
						}
					}
					if(surgeList != null) {
						L.i(surgeList.toString());
						surgeArrayList = surgeList;
						surgeArrayListAdapter = new RankingListViewAdapter(RankingListActivity.this, surgeArrayList);

						surgeListViewPosition.resetParameter(surgeArrayList.size());
						listView_surge.setAdapter(surgeArrayListAdapter);
						surgeArrayListAdapter.notifyDataSetChanged();
						
						if(currFocusSelect == FocusSelect.SURGE_LISTVIEW) {
							focusView.setArrayList(surgeArrayList);
							focusView.refreshView();
						}
					}
					break;
				case LOAD_RANKING_FAIL:
					break;
				default:
					break;
				}
			}
		};
	};
}
