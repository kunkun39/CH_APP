package com.changhong.gdappstore.datacenter;

import java.util.ArrayList;
import java.util.List;

import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.net.LoadCompleteListener;

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

	/************************** 缓存数据定义区域begin *************************/

	/** 栏目分类列表 **/
	private List<Category> categories = new ArrayList<Category>();
	/** 栏目分类下的应用 **/
	public List<Object> categoryApps = new ArrayList<Object>();

	/************************** 缓存数据定义区域end *************************/
	//
	//
	//
	//
	/************************** 请求方法定义区域begin *************************/
	/**
	 * 请求解析栏目分类和每页应用数据
	 * 
	 * @param completeListener
	 */
	public void loadCategoryAndPageData(LoadCompleteListener completeListener) {
		loadCategories(null);
		loadPageApps(null);
		if (completeListener != null) {
			completeListener.onComplete();
		}
	}

	/**
	 * 加载栏目分类数据
	 */
	public void loadCategories(LoadCompleteListener completeListener) {
		// TODO 请求代码
		categories = Parse.parseCategory(Parse.json_categories);
		if (completeListener != null) {
			completeListener.onComplete();
		}
	}

	/**
	 * 加载栏目应用海报数据
	 */
	public void loadPageApps(LoadCompleteListener completeListener) {
		// TODO 请求代码
		Parse.parsePageApps(Parse.json_pageapps);
		if (completeListener != null) {
			completeListener.onComplete();
		}
	}

	/**
	 * 请求某分类下面的应用
	 * 
	 * @param categoryId
	 *            分类id
	 */
	public void loadAppsByCategoryId(int categoryId, LoadCompleteListener completeListener) {

		if (!Parse.json_categoryapps.containsKey(categoryId)) {
			// TODO 请求代码
		}
		String json = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"values\":[{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":114,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":108,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":102,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":123,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":124,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":128,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":127,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":131,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":135,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":134,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":121,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}]}";
		Parse.json_categoryapps.put(categoryId, json);
		categoryApps = Parse.parseCategoryApp(json);
		if (completeListener != null) {
			completeListener.onComplete();
		}
	}

	/************************** 请求方法定义区域end *************************/
	//
	//
	//
	/************************** 其它方法定义区域 *************************/
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
	 * 
	 * @param categoryId
	 *            栏目id
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
				// 查询子栏目
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
