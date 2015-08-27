package com.changhong.gdappstore.view;

import android.view.View;
import android.widget.RelativeLayout;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.ViewHolder;

public class FocusView {
	private ViewHolder holder;
	private RelativeLayout focusItem;
	private RelativeLayout.LayoutParams mlayout;
	private int width;
	private int height;
	private boolean haschanged;
	public FocusView(RelativeLayout focusItem,int width,int height) {
		// TODO Auto-generated constructor stub
		this.focusItem = focusItem;
		this.width = width;
		this.height = height;
		
		holder = new ViewHolder();

		holder.ranking_item = (RelativeLayout) focusItem.findViewById(R.id.ranking_item);
		
		mlayout = new RelativeLayout.LayoutParams(this.width + 50, this.height + 45);
		
		haschanged = false;
	}
	
	public void focusViewChange(int position,int leftMargin,int topMargin) {
		if(haschanged == false) {
			haschanged = true;
			focusItem.setVisibility(View.VISIBLE);
		}
		mlayout.leftMargin = leftMargin - 23;
		mlayout.topMargin = topMargin - 22;
		focusItem.setLayoutParams(mlayout);	
		
		holder.ranking_item.setBackgroundResource(R.drawable.focues_ranking);
		focusItem.bringToFront();
	}
	
	public void refreshView() {
	}
	
	public boolean hasChanged() {
		return haschanged;
	}
	
	public void reset() {
		haschanged = false;
	}
}
