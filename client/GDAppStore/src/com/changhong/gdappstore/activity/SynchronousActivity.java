package com.changhong.gdappstore.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.base.BaseActivity;

public class SynchronousActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_synchronous);
		initView();
	}

	private void initView() {
		findViewById(R.id.bt_backup).setOnClickListener(this);
		findViewById(R.id.bt_recover).setOnClickListener(this);
		findViewById(R.id.bt_manage).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_backup:

			break;
		case R.id.bt_recover:

			break;
		case R.id.bt_manage:

			break;

		default:
			break;
		}
	}
}
