package com.changhong.gdappstore.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.GridView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.SynchGridAdapter;
import com.changhong.gdappstore.base.BaseActivity;
import com.changhong.gdappstore.model.App;

public class SynchBackUpActivity extends BaseActivity {
	
	private GridView gridView;
	private SynchGridAdapter adapter;
	private Button bt_batch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synch_backup);
		initView();
		initData();
	}
	
	private void initView() {
		gridView=findView(R.id.gridview);
		adapter=new SynchGridAdapter(context, null);
		gridView.setAdapter(adapter);
		bt_batch=findView(R.id.bt_batch);
		bt_batch.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction()==KeyEvent.ACTION_DOWN&&keyCode==KeyEvent.KEYCODE_DPAD_RIGHT) {
					gridView.requestFocus();//选中上次选中的位置
					return true;
				}
				return false;
			}
		});
	}

	
	private void initData() {
		List<App> items=new ArrayList<App>();
		for (int i = 0; i < 50; i++) {
			items.add(new App());
		}
		adapter.updateList(items);
	}
}
