package com.changhong.gdappstore.view;

import android.view.View;

public class ListViewChange {
	private View lastContentView;
	public ListViewChange() {
		// TODO Auto-generated constructor stub
	}
	
	public void showView() {
		if(lastContentView != null) {
			lastContentView.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideView(View view) {
		if(view != null) {
			view.setVisibility(View.INVISIBLE);
			showView();
			lastContentView = view;
		}
	}
}
