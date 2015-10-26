package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.SynchGridAdapter;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.model.SynchApp;
import com.changhong.gdappstore.model.SynchApp.Type;
import com.changhong.gdappstore.util.L;

public class SynchBackUpActivity extends BaseActivity implements OnClickListener, OnKeyListener {

	private GridView gridView;
	private SynchGridAdapter adapter;
	private Button bt_batch;
	private ImageView iv_shandow_item2, iv_shandow_item3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synch_backup);
		initView();
		initData();
	}

	private void initView() {
		gridView = findView(R.id.gridview);
		iv_shandow_item2 = findView(R.id.iv_shandow_item2);
		iv_shandow_item3 = findView(R.id.iv_shandow_item3);

		gridView.setOnItemSelectedListener(itemSelectedListener);
		gridView.setOnItemClickListener(onItemClickListener);
		adapter = new SynchGridAdapter(context, null);
		gridView.setAdapter(adapter);
		bt_batch = findView(R.id.bt_batch);
		bt_batch.setOnKeyListener(this);
		gridView.setOnKeyListener(this);
		bt_batch.setOnClickListener(this);
	}

	private void initData() {
		List<SynchApp> items = new ArrayList<SynchApp>();
		for (int i = 0; i < 49; i++) {
			SynchApp app = new SynchApp();
			app.setVersionInt(i + 1);
			app.setChecked(false);
			app.setSynchType(Type.NORMAL);
			items.add(app);
		}
		adapter.setBatch(true);
		adapter.updateList(items);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_batch:
			// 批量备份

			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		switch (v.getId()) {
		case R.id.bt_batch:
			// 批量操作按钮按又选中上次选中位置
			if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
				gridView.requestFocus();// 选中上次选中的位置
				return true;
			}
			break;
		case R.id.gridview:
			if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				// 处理最后一排不能按下，不然焦点会跑到批量操作上面去
				int itemCount = adapter.getCount();
				int curSelectPos = gridView.getSelectedItemPosition();
				if (itemCount > 0) {
					int lastRowCount = itemCount % 3;
					if (curSelectPos >= (itemCount - lastRowCount)) {
						return true;
					}
				}
			}
			break;

		default:
			break;
		}
		return false;
	}
	
	OnItemClickListener onItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (adapter.isBatch()) {
				//批量操作
				SynchApp app=(SynchApp) adapter.getItem(position);
				if (app!=null) {
					app.setChecked(!app.isChecked());
					adapter.notifyDataSetChanged();
				}
			}else {
				//普通操作
				
			}
		}
	};

	OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			{
				// 处理最后一排不满3个时候，倒影显示问题
				int firstVisiblePos = gridView.getFirstVisiblePosition();
				int lastRowCount = adapter.getCount() % 3;
				L.d("onItemSelected firstvisible=" + firstVisiblePos + " " + (adapter.getCount() - (lastRowCount + 6)));
				if (firstVisiblePos >= (adapter.getCount() - (lastRowCount + 6))) {
					if (lastRowCount == 1) {
						iv_shandow_item2.setVisibility(INVISIBLE);
						iv_shandow_item3.setVisibility(INVISIBLE);
					} else if (lastRowCount == 2) {
						iv_shandow_item2.setVisibility(VISIBLE);
						iv_shandow_item3.setVisibility(INVISIBLE);
					} else {
						iv_shandow_item2.setVisibility(VISIBLE);
						iv_shandow_item3.setVisibility(VISIBLE);
					}
				} else {
					iv_shandow_item2.setVisibility(VISIBLE);
					iv_shandow_item3.setVisibility(VISIBLE);
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};
}
