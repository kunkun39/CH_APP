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

	// 单例模式
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
	 * 
	 * @return
	 */
	public synchronized List<Category> getCategories() {
		return categories;
	}

	/**
	 * 根据栏目id获取栏目
	 * @param categoryId 栏目id
	 * @return
	 */
	public Category getCategoryById(int categoryId) {
		if (categories == null || categories.size() == 0) {
			return null;
		}
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (category.getId() == categoryId) {
				return category;
			} else {
				//查询子栏目
				if (category.getCategoyChildren() != null && category.getCategoyChildren().size() > 0) {
					for (int j = 0; j < category.getCategoyChildren().size(); j++) {
						if (category.getCategoyChildren().get(j).getId() == categoryId) {
							return category.getCategoyChildren().get(j);
						}
					}
				}
			}
		}
		return null;
	}
}
