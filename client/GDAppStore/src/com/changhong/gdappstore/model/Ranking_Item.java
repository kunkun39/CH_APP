package com.changhong.gdappstore.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ranking_Item implements Comparable<Ranking_Item>{
	private int topNum;
	private String imgPath;
	private String appName;
	private String download_num;
	private String appSize;
	private int appId;
	private String appKey;
	public Bitmap appBitmap;

	public Ranking_Item() {
		
	}
	
	public Ranking_Item(int topNum, String imgPath, String appName,
			String download_num, String appSize) {
		this.topNum = topNum;
		this.imgPath = imgPath;
		this.appName = appName;
		this.download_num = download_num;
		this.appSize = appSize;
	}
	
	

	public int getTopNum() {
		return topNum;
	}
	public void setTopNum(int topNum) {
		this.topNum = topNum;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getDownload_num() {
		return download_num;
	}
	public void setDownload_num(String download_num) {
		this.download_num = download_num;
	}
	public String getAppSize() {
		return appSize;
	}
	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	
	public Bitmap getAppBitmap() {
		if(null == appBitmap && null != imgPath) {
			appBitmap = BitmapFactory.decodeFile(imgPath);
		}
		
		return appBitmap;
	}

	@Override
	public int compareTo(Ranking_Item another) {
		// TODO Auto-generated method stub
		return (topNum < another.topNum) ? 1 : -1;
	}
}
