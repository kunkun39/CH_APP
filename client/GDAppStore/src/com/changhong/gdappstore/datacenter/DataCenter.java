package com.changhong.gdappstore.datacenter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.net.HttpRequestUtil;
import com.changhong.gdappstore.net.LoadListener.LoadCompleteListener;
import com.changhong.gdappstore.net.LoadListener.LoadListListener;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;

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
	public void loadCategoryAndPageData(final LoadCompleteListener completeListener) {
		loadCategories(new LoadCompleteListener() {

			@Override
			public void onComplete() {
				loadPageApps(completeListener);
			}
		});
	}

	/**
	 * 加载栏目分类数据
	 */
	public void loadCategories(final LoadCompleteListener completeListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getCategoryUrl));
				if (Config.ISTEST && TextUtils.isEmpty(json)) {
					json = Parse.json_categories;// TODO 临时使用测试数据
				}
				if (!TextUtils.isEmpty(json)) {
					Parse.json_categories = json;
					categories = Parse.parseCategory(json);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				if (completeListener != null) {
					completeListener.onComplete();// 请求操作完毕
				}
				super.onPostExecute(result);
			}

		}.execute("");
	}

	/**
	 * 加载页面海报app数据（在category数据加载完后调用。）
	 */
	public void loadPageApps(final LoadCompleteListener completeListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getPagesUrl));
				if (Config.ISTEST && TextUtils.isEmpty(json)) {
					json = Parse.json_pageapps;// TODO 临时使用测试数据
				}
				if (!TextUtils.isEmpty(json)) {
					Parse.json_pageapps = json;
					Parse.parsePageApps(json);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Object result) {
				if (completeListener != null) {
					completeListener.onComplete();// 请求操作完毕
				}
				super.onPostExecute(result);
			}

		}.execute("");
	}

	/**
	 * 请求某分类下面的应用
	 * 
	 * @param categoryId
	 *            分类id
	 */
	public void loadAppsByCategoryId(final int categoryId, final LoadListListener loadAppListListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String jsonString = "";
				if (!Parse.json_categoryapps.containsKey(categoryId)) {
					// 缓存中没有就去服务器请求
					String url = Config.getCategoryAppsUrl + "?categoryId=" + categoryId;
					jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url));
				} else {
					jsonString = Parse.json_categoryapps.get(categoryId);
				}
				if (Config.ISTEST && TextUtils.isEmpty(jsonString)) {
//					jsonString = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"values\":[{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":114,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":108,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":102,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":123,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":124,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":128,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":127,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":131,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":135,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":134,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":121,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}]}";
					jsonString = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"values\":[{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":114,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":108,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"}]}";// TODO 临时使用测试数据
					// TODO 临时使用测试数据
				}
				if (!TextUtils.isEmpty(jsonString) && !Parse.json_categoryapps.containsKey(categoryId)) {
					Parse.json_categoryapps.put(categoryId, jsonString);// 添加进缓存
				}
				List<Object> categoryApps = Parse.parseCategoryApp(jsonString);

				return categoryApps;
			}

			@Override
			protected void onPostExecute(Object result) {
				if (loadAppListListener != null) {
					loadAppListListener.onComplete((List<Object>) result);
				}
				super.onPostExecute(result);
			}

		}.execute("");
	}

	/**
	 * 请求应用详情
	 * 
	 * @param appId
	 *            应用id
	 * @param completeListener
	 */
	public void loadAppDetail(final int appId, final LoadObjectListener loadObjectListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				// 缓存中没有就去服务器请求
				String url = Config.getAppDetailUrl + "?categoryId=" + appId;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url));
				if (Config.ISTEST && TextUtils.isEmpty(jsonString)) {
					// TODO 临时使用测试数据
					jsonString = Parse.json_appdetail;
				}
				AppDetail appDetail = Parse.parseAppDetail(jsonString);
				return appDetail;
			}

			@Override
			protected void onPostExecute(Object result) {
				if (loadObjectListener != null) {
					loadObjectListener.onComplete(result);
				}
				super.onPostExecute(result);
			}

		}.execute("");
	}

	/**
	 * 加载搜索列表
	 * 
	 * @param keywords
	 * @param loadListListener
	 */
	public static void loadAppSearch(final String keywords, final LoadListListener loadListListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String url = Config.getAppSearchUrl + "?keywords=" + keywords;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url));
				if (Config.ISTEST && TextUtils.isEmpty(jsonString)) {
					jsonString = Parse.json_appsearch;// TODO 临时使用测试数据
				}
				Parse.json_appsearch = jsonString;
				List<Object> categoryApps = Parse.parseSearchApps(jsonString);
				return categoryApps;
			}

			@Override
			protected void onPostExecute(Object result) {
				if (loadListListener != null) {
					loadListListener.onComplete((List<Object>) result);
				}
				super.onPostExecute(result);
			}
		}.execute("");
	}

	public static void loadAppsUpdateData(final List<String> packages, final LoadListListener loadListListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String url = Config.getAppVersionsUrl;
				if (packages == null || packages.size() == 0) {
					return null;
				}
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (int i = 0; i < packages.size(); i++) {
					paramList.add(new BasicNameValuePair("", packages.get(i)));
				}
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doPostRequest(url, paramList));
				if (Config.ISTEST && TextUtils.isEmpty(jsonString)) {
					jsonString = Parse.json_appversions;// TODO 临时使用测试数据
				}
				Parse.json_appversions = jsonString;
				List<Object> apps = Parse.parseSearchApps(jsonString);
				return apps;
			}

			@Override
			protected void onPostExecute(Object result) {
				if (loadListListener != null) {
					loadListListener.onComplete((List<Object>) result);
				}
				super.onPostExecute(result);
			}
		}.execute("");
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
