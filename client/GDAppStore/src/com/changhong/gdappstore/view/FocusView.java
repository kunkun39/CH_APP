package com.changhong.gdappstore.view;

import java.util.ArrayList;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.adapter.ViewHolder;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.util.L;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

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
		
		mlayout = new RelativeLayout.LayoutParams(width + 50, height + 40);
		
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
		Bitmap bitmap = ranking_Item.getAppBitmap();
		if (null != bitmap) {
			holder.app_icon.setImageBitmap(bitmap);
		}
		else {
			holder.app_icon.setVisibility(View.INVISIBLE);
			ImageLoadUtil.displayImgByMemoryDiscCache(rankingData.getHost() + ranking_Item.getAppKey() + "/" + ranking_Item.getAppIconPath(), holder.app_icon,imageLoadingListener);
		}
		holder.top_num.setText(ranking_Item.getTopNum() + "");
		ranking_Item.setTopImg(holder.top_num);
		
		holder.app_name.setText(ranking_Item.getAppName());
		holder.download_num.setText(mContext.getString(R.string.str_download) + ranking_Item.getDownload_num());
		holder.app_size.setText(ranking_Item.getAppSize() + "M");
		
		mlayout.leftMargin = leftMargin - 25;
		mlayout.topMargin = topMargin - 22;
		
		focusItem.setLayoutParams(mlayout);
		
		holder.ranking_item.setBackgroundResource(R.drawable.focues_ranking);
		
		//Animation scallBigAnimation = AnimationUtils.loadAnimation(mContext, R.anim.scale_big);
		//focusItem.startAnimation(scallBigAnimation);
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
		Bitmap bitmap = ranking_Item.getAppBitmap();
		if (null != bitmap) {
			holder.app_icon.setImageBitmap(bitmap);
		}
		else {
			holder.app_icon.setVisibility(View.INVISIBLE);
			ImageLoadUtil.displayImgByMemoryDiscCache(rankingData.getHost() + ranking_Item.getAppKey() + "/" + ranking_Item.getAppIconPath(), holder.app_icon,imageLoadingListener);
		}
		
		holder.top_num.setText(ranking_Item.getTopNum() + "");

		holder.app_name.setText(ranking_Item.getAppName());
		holder.download_num.setText(mContext.getString(R.string.str_download) + ranking_Item.getDownload_num());
		holder.app_size.setText(ranking_Item.getAppSize() + "M");
	}
	
	public boolean hasChanged() {
		return haschanged;
	}
	
	public void reset() {
		haschanged = false;
	}
	
	private ImageLoadingListener imageLoadingListener = new ImageLoadingListener() {
		
		@Override
		public void onLoadingStarted(String arg0, View arg1) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
			// TODO Auto-generated method stub
			holder.app_icon.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
			// TODO Auto-generated method stub
			holder.app_icon.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onLoadingCancelled(String arg0, View arg1) {
			// TODO Auto-generated method stub
			holder.app_icon.setVisibility(View.VISIBLE);
		}
	};
}
