package com.changhong.gdappstore.test;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.changhong.gdappstore.R;
import com.changhong.gdappstore.model.Ranking_Item;

public class Ranking_data_test {
	private Context context;
	public Ranking_data_test(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	public ArrayList<Ranking_Item> getNewArrayList(){
		ArrayList<Ranking_Item> newArrayList = new ArrayList<Ranking_Item> ();
		Ranking_Item item;
		
		item = new Ranking_Item(1, null, "植物大战僵尸", "100万+", "117.6M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_zhiwudazhan);
		newArrayList.add(item);
		
		item = new Ranking_Item(2, null, "酷狗音乐", "300万+", "4.7M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_kugou);
		newArrayList.add(item);
		
		item = new Ranking_Item(3, null, "全家盒电视", "60万+", "4.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_quanjiahe);
		newArrayList.add(item);
		
		item = new Ranking_Item(4, null, "我叫MT 2", "50000+", "207.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_mt);
		newArrayList.add(item);
		
		item = new Ranking_Item(5, null, "水果忍者", "100万+", "10.2M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_shuiguo);
		newArrayList.add(item);
		
		item = new Ranking_Item(6, null, "运动加加Online", "40万+", "6.3M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_yundong);
		newArrayList.add(item);
		
		item = new Ranking_Item(7, null, "电视QQ", "100万+", "6.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_dianshiqq);
		newArrayList.add(item);
		
		item = new Ranking_Item(8, null, "我是车神", "100万+", "31.2M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_woshicheshen);
		newArrayList.add(item);
		
		item = new Ranking_Item(9, null, "开心酷跑", "100万+", "35.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_kaixinkupao);
		newArrayList.add(item);
		
		item = new Ranking_Item(10, null, "文件管理器", "80000+", "2.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_wenjian);
		newArrayList.add(item);
		
		return newArrayList;
	}
	
	public ArrayList<Ranking_Item> getHotArrayList(){
		ArrayList<Ranking_Item> hotArrayList = new ArrayList<Ranking_Item> ();
		Ranking_Item item;
		
		item = new Ranking_Item(1, null, "全家盒电视", "60万+", "4.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_quanjiahe);
		hotArrayList.add(item);
		
		item = new Ranking_Item(2, null, "酷狗音乐", "300万+", "4.7M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_kugou);
		hotArrayList.add(item);
		
		item = new Ranking_Item(3, null, "花样三国", "20万+", "139.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_huayangsanguo);
		hotArrayList.add(item);
		
		item = new Ranking_Item(4, null, "天天西游酷跑", "20万+", "10.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_tiantianxiyou);
		hotArrayList.add(item);
		
		item = new Ranking_Item(5, null, "决斗吧！兄弟", "10万+", "46.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_juedouba);
		hotArrayList.add(item);
		
		item = new Ranking_Item(6, null, "多乐助手", "30万+", "15.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_duolezhushou);
		hotArrayList.add(item);
		
		item = new Ranking_Item(7, null, "Q版赛车", "10万+", "22.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_qbansaiche);
		hotArrayList.add(item);
		
		item = new Ranking_Item(8, null, "雷电2015", "10万+", "14.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_leidian);
		hotArrayList.add(item);
		
		item = new Ranking_Item(9, null, "卡牌萌兽", "10万+", "39.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_kapaimengshou);
		hotArrayList.add(item);
		
		item = new Ranking_Item(10, null, "拳皇97 高清版", "40万+", "127.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_quanhuang);
		hotArrayList.add(item);
		
		return hotArrayList;
	}
	
	public ArrayList<Ranking_Item> getSurgeHotArrayList(){
		ArrayList<Ranking_Item> surgeArrayList = new ArrayList<Ranking_Item> ();
		Ranking_Item item;
		
		item = new Ranking_Item(1, null, "我叫MT 2", "50000+", "207.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_mt);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(2, null, "酷狗音乐", "300万+", "4.7M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_kugou);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(3, null, "全家盒电视", "60万+", "4.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_quanjiahe);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(4, null, "电视QQ", "100万+", "6.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_dianshiqq);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(5, null, "酷我K歌", "100万+", "14.1M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_kuwokge);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(6, null, "水果忍者", "100万+", "10.2M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_shuiguo);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(7, null, "植物大战僵尸", "100万+", "117.6M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_zhiwudazhan);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(8, null, "运动加加Online", "40万+", "6.3M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_yundong);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(9, null, "我是车神", "100万+", "31.2M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_woshicheshen);
		surgeArrayList.add(item);
		
		item = new Ranking_Item(10, null, "电视淘宝", "30万+", "35.0M");		
		item.appBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_test_taobao);
		surgeArrayList.add(item);
		
		return surgeArrayList;
	}

}
