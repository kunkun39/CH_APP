package com.changhong.gdappstore.model;

import java.util.ArrayList;

public class RankingData {
	private static RankingData rankingData;
	
	private String strHost;
	private ArrayList<Ranking_Item> newArrayList = null;
	private ArrayList<Ranking_Item> hotArrayList = null;
	private ArrayList<Ranking_Item> surgeArrayList = null;
	
	protected RankingData() {
		// TODO Auto-generated constructor stub
	}

	public static RankingData getInstance() {
		if(rankingData != null) {
			rankingData = new RankingData();
		}
		return rankingData;
	}
	
	public void setNewRankingData(ArrayList<Ranking_Item> arrayList) {
		newArrayList = arrayList;
	}
	
	public ArrayList<Ranking_Item> getNewRankingData() {
		return newArrayList;
	}
	
	public void setHotRankingData(ArrayList<Ranking_Item> arrayList) {
		hotArrayList = arrayList;
	}
	
	public ArrayList<Ranking_Item> getHotRankingData() {
		return hotArrayList;
	}
	
	public void setSurgeRankingData(ArrayList<Ranking_Item> arrayList) {
		surgeArrayList = arrayList;
	}
	
	public ArrayList<Ranking_Item> getSurgeRankingData() {
		return surgeArrayList;
	}
	
	public void setHost(String host) {
		strHost = host;
	}

	public String getHost() {
		return strHost;
	}
}
