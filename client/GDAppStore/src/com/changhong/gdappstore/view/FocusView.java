package com.changhong.gdappstore.view;

import java.util.ArrayList;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.ViewHolder;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FocusView {
	private ViewHolder holder;
	private ArrayList<Ranking_Item> mArrayList;
	private Context mContext;
	private RelativeLayout focusItem;
	private RelativeLayout.LayoutParams mlayout;
	private int width;
	private int height;
	private RankingData rankingData;
	private boolean haschanged;
	private int position;
	public FocusView(Context context,RelativeLayout focusItem,int width,int height) {
		// TODO Auto-generated constructor stub
		this.focusItem = focusItem;
		this.mContext = context;
		this.width = width;
		this.height = height;
		
		holder = new ViewHolder();
		
		holder.top_num = (TextView)focusItem.findViewById(R.id.top_num);
		holder.app_icon = (ImageView)focusItem.findViewById(R.id.item_app_icon);
		holder.app_name = (TextView)focusItem.findViewById(R.id.item_app_name);
		holder.download_num = (TextView)focusItem.findViewById(R.id.item_app_download_num);
		holder.app_size = (TextView)focusItem.findViewById(R.id.item_app_size);
		holder.ranking_item = (RelativeLayout) focusItem.findViewById(R.id.ranking_item);
		
		mlayout = new RelativeLayout.LayoutParams(width + 50, height + 50);
		
		rankingData = RankingData.getInstance();
		
		haschanged = false;
	}
	
	public void setArrayList(ArrayList<Ranking_Item> arrayList) {
		mArrayList = arrayList;
	}
	
	public void focusViewChange(int position,int leftMargin,int topMargin) {
		L.i("focusViewChange position : " + position);
		if(haschanged == false) {
			haschanged = true;
			focusItem.setVisibility(View.VISIBLE);
		}
		if(mArrayList == null || mArrayList.isEmpty()) {
			L.w("focusViewChange mArrayList error!");
			return ;
		}
		if(position >= mArrayList.size()) {
			L.w("focusViewChange parameter error!");
			return ;
		}
		
		Ranking_Item ranking_Item = mArrayList.get(position);
		holder.top_num.setText(ranking_Item.getTopNum() + "");
		
		Bitmap bitmap = ranking_Item.getAppBitmap();
		if (null != bitmap) {
			holder.app_icon.setImageBitmap(bitmap);
		}
		else {
			ImageLoadUtil.displayImgByMemoryDiscCache(rankingData.getHost() + ranking_Item.getAppKey() + "/" + ranking_Item.getAppIconPath(), holder.app_icon);
		}
		holder.app_name.setText(ranking_Item.getAppName());
		holder.download_num.setText(mContext.getString(R.string.str_download) + ranking_Item.getDownload_num());
		holder.app_size.setText(ranking_Item.getAppSize());
		
		mlayout.leftMargin = leftMargin - 25;
		mlayout.topMargin = topMargin - 25;
		
		focusItem.setLayoutParams(mlayout);
		
		holder.ranking_item.setBackgroundResource(R.drawable.item_float_focus_bg);
		
		Animation scallBigAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale_big);
		focusItem.startAnimation(scallBigAnimation);
		focusItem.bringToFront();
		
		this.position = position;
	}
	
	public void refreshView() {
		if(mArrayList == null || mArrayList.isEmpty()) {
			L.w("refreshView mArrayList error!");
			return ;
		}
		if(position >= mArrayList.size()) {
			L.w("refreshView position error!");
			return ;
		}
		
		Ranking_Item ranking_Item = mArrayList.get(position);
		
		holder.top_num.setText(ranking_Item.getTopNum() + "");
		
		Bitmap bitmap = ranking_Item.getAppBitmap();
		if (null != bitmap) {
			holder.app_icon.setImageBitmap(bitmap);
		}
		else {
			ImageLoadUtil.displayImgByMemoryDiscCache(rankingData.getHost() + ranking_Item.getAppKey() + "/" + ranking_Item.getAppIconPath(), holder.app_icon);
		}
		holder.app_name.setText(ranking_Item.getAppName());
		holder.download_num.setText(mContext.getString(R.string.str_download) + ranking_Item.getDownload_num());
		holder.app_size.setText(ranking_Item.getAppSize());
	}
	
	public boolean hasChanged() {
		return haschanged;
	}
	
	public void reset() {
		haschanged = false;
	}

}
