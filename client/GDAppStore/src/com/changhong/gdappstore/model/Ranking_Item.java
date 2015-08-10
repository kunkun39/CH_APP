package com.changhong.gdappstore.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ranking_Item implements Comparable<Ranking_Item>{
	private int topNum;
	private String imgPath;
	private String appName;
	private String download_num;
	private String appSize;
	public Bitmap appBitmap;

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

	public String getAppName() {
		return appName;
	}

	public String getDownload_num() {
		return download_num;
	}

	public String getAppSize() {
		return appSize;
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
