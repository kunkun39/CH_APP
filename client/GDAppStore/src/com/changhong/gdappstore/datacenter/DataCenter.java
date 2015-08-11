package com.changhong.gdappstore.datacenter;

import java.util.ArrayList;
import java.util.List;

import com.changhong.gdappstore.model.Category;

/**
 * 数据中心类
 * 
 * @author wangxiufeng
 * 
 */
public class DataCenter {

	private static DataCenter dataCenter = null;

	private DataCenter() {

	}
	//单例模式
	public static DataCenter getInstance() {
		if (dataCenter == null) {
			dataCenter = new DataCenter();
		}
		return dataCenter;
	}

	private List<Category> categories = new ArrayList<Category>();

	public void loadAllData() {
		loadCategories();
		loadPageApps();
	}
	/**
	 * 加载栏目数据
	 */
	public void loadCategories() {
		categories = Parse.parseCategory(Parse.categoryJosn);
	}
	/**
	 * 加载栏目应用海报数据
	 */
	public void loadPageApps() {
		 Parse.parsePageApps(Parse.pagebox);
	}
	/**
	 * 获取栏目数据
	 * @return
	 */
	public synchronized List<Category> getCategories() {
		return categories;
	}
}
