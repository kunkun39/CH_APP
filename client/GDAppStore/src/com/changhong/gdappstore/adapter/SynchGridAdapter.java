package com.changhong.gdappstore.adapter;

import java.util.ArrayList;
import java.util.List;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.model.App;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SynchGridAdapter extends BaseAdapter {

	private List<App> items = new ArrayList<App>();

	private Context context;

	public SynchGridAdapter(Context context, List<App> items) {
		this.context = context;
		this.items = items;
	}

	public void updateList(List<App> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items == null ? 0 : items.size();
	}

	@Override
	public Object getItem(int position) {
		return items == null ? null : items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView==null) {
			viewHolder =new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.item_synch, parent,false);
			viewHolder.iv_icon=(ImageView) convertView.findViewById(R.id.iv_icon);
			viewHolder.tv_name=(TextView)convertView.findViewById(R.id.tv_appname);
			viewHolder.tv_version=(TextView)convertView.findViewById(R.id.tv_version);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.tv_version.setText((position+1)+" "	);
		return convertView;
	}

	private class ViewHolder {
		public ImageView iv_icon;
		public TextView tv_name;
		public TextView tv_version;
	}

}
