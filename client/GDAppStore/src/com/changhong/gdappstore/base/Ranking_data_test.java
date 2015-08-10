package com.changhong.gdappstore.base;

import java.util.ArrayList;

public class Ranking_data_test {

	public Ranking_data_test() {
		// TODO Auto-generated constructor stub
	}
	
	public static ArrayList<Ranking_Item> getNewArrayList(){
		ArrayList<Ranking_Item> newArrayList = new ArrayList<Ranking_Item> ();
		
		newArrayList.add(new Ranking_Item(1, null, "酷狗音乐酷狗音乐", "300万+", "4.7M"));
		newArrayList.add(new Ranking_Item(2, null, "百灵K歌", "50000+", "26.0M"));
		newArrayList.add(new Ranking_Item(3, null, "全家盒电视", "50万+", "4.0M"));
		newArrayList.add(new Ranking_Item(4, null, "电视QQ", "60万+", "15.2M"));
		newArrayList.add(new Ranking_Item(5, null, "TVOS", "20000+", "15.2M"));
		newArrayList.add(new Ranking_Item(6, null, "Android", "60万+", "15.2M"));
		newArrayList.add(new Ranking_Item(7, null, "java", "60万+", "15.2M"));
		newArrayList.add(new Ranking_Item(8, null, "IOS", "60万+", "15.2M"));
		
		return newArrayList;
	}

}
