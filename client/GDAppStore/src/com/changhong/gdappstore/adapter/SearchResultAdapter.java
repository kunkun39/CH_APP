package com.changhong.gdappstore.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.util.ImageLoadUtil;
import com.changhong.gdappstore.view.ScoreView;
/**
 * 搜索结果grideviewadapter
 * @author wangxiufeng
 *
 */
public class SearchResultAdapter extends BaseAdapter {
	private List<Object> datas = new ArrayList<Object>();
	private Context context;

	public SearchResultAdapter(Context context) {
		this.context = context;
	}

	public SearchResultAdapter(Context context, List<Object> datas) {
		super();
		this.datas = datas;
		this.context = context;
	}

	/**
	 * Update the datas and notifyDataSetChanged();
	 * 
	 * @param datas
	 */
	public void updateData(List<Object> datas) {
		this.datas = datas;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas == null ? null : datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_appsearch, null);
			viewHolder.iv_appicon = (ImageView) convertView.findViewById(R.id.iv_appicon);
			viewHolder.tv_appname = (TextView) convertView.findViewById(R.id.tv_appname);
			viewHolder.scoreView = (ScoreView) convertView.findViewById(R.id.scoreview);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		RelativeLayout rl_content = (RelativeLayout) convertView.findViewById(R.id.rl_appsearch_content);
		rl_content.setBackgroundColor(Color.TRANSPARENT);
		
		App model=(App) getItem(position);
		viewHolder.scoreView.setScoreBy5Total(5);
		viewHolder.tv_appname.setText(model.getAppname());
		ImageLoadUtil.displayImgByNoCache(model.getPosterFilePath(), viewHolder.iv_appicon);
		return convertView;
	}

	private class ViewHolder {
		public ImageView iv_appicon;
		public TextView tv_appname;
		public ScoreView scoreView;
	}

}
