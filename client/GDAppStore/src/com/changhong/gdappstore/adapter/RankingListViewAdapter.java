package com.changhong.gdappstore.adapter;

import java.util.ArrayList;
import com.changhong.gdappstore.R;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.util.ImageLoadUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RankingListViewAdapter extends BaseAdapter {
	private Context mContext = null;
	private ArrayList<Ranking_Item> mArrayList = null;
	private LayoutInflater flater = null;
	private RankingData rankingData;

	public RankingListViewAdapter(Context context,ArrayList<Ranking_Item> array) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mArrayList = array;
		flater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rankingData = RankingData.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mArrayList != null) {
			return mArrayList.size();
		}
		
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mArrayList != null) {
			return mArrayList.get(position);
		}

		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//Debug.i("位置：" + position);
		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = (RelativeLayout)flater.inflate(R.layout.rankings_item, null);
			convertView.setBackgroundColor(Color.TRANSPARENT);
			
			holder = new ViewHolder();
			
			holder.top_num = (TextView)convertView.findViewById(R.id.top_num);
			holder.app_icon = (ImageView)convertView.findViewById(R.id.item_app_icon);
			holder.app_name = (TextView)convertView.findViewById(R.id.item_app_name);
			holder.download_num = (TextView)convertView.findViewById(R.id.item_app_download_num);
			holder.app_size = (TextView)convertView.findViewById(R.id.item_app_size);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		Ranking_Item ranking_Item = mArrayList.get(position);
		holder.top_num.setText(ranking_Item.getTopNum() + "");
		ranking_Item.setTopImg(holder.top_num);
		
		Bitmap bitmap = ranking_Item.getAppBitmap();
		if (null != bitmap) {
			holder.app_icon.setImageBitmap(bitmap);
		}
		else {
			ImageLoadUtil.displayImgByMemoryDiscCache(rankingData.getHost() + ranking_Item.getAppKey() + "/" + ranking_Item.getAppIconPath(), holder.app_icon);
		}
		
		holder.app_name.setText(ranking_Item.getAppName());
		holder.download_num.setText(mContext.getString(R.string.str_download) + ranking_Item.getDownload_num());
		holder.app_size.setText(ranking_Item.getAppSize() + "M");
		
		return convertView;
	}
}
