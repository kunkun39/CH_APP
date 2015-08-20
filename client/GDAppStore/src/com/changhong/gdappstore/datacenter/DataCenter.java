package com.changhong.gdappstore.datacenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.MyApplication;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.net.HttpRequestUtil;
import com.changhong.gdappstore.net.LoadListener.LoadCompleteListener;
import com.changhong.gdappstore.net.LoadListener.LoadListListener;
import com.changhong.gdappstore.net.LoadListener.LoadObjectListener;
import com.changhong.gdappstore.service.CacheManager;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.SharedPreferencesUtil;

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
	/** 上次请求分类数据的时间 */
	private static long lastRequestCategoriesTime = 0;
	/** 上次请求分类数据的时间 */
	private static long lastRequestPageAppsTime = 0;
	/** 上一次请求的排行榜数据的时间 **/
	private static long lastRequestRankListTime = 0;
	/** 上次请求分类下app列表时间 **/
	private static Map<Integer, Long> lastRequestAppsByCategoryId = new HashMap<Integer, Long>();
	/** 上次请求详情推荐列表时间 **/
	private static Map<Integer, Long> lastRequestRecommendApps = new HashMap<Integer, Long>();


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
	public void loadCategoryAndPageData(final Context context, final LoadCompleteListener completeListener) {
		loadCategories(context, new LoadCompleteListener() {

			@Override
			public void onComplete() {
				loadPageApps(context, completeListener);
			}
		});
	}

	/**
	 * 加载栏目分类数据
	 */
	public void loadCategories(final Context context, final LoadCompleteListener completeListener) {
		if (System.currentTimeMillis() - lastRequestCategoriesTime < Config.REQUEST_RESTTIEM) {
			String json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_CATEGORIES);
			categories = Parse.parseCategory(json);
			if (categories != null && categories.size() > 0 && completeListener != null) {
				completeListener.onComplete();
				return;// 在一定的时间段内使用缓存数据不用重复请求服务器。
			}
		}
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getCategoryUrl));
				if (Config.ISTEST && TextUtils.isEmpty(json)) {
					json = "[{\"id\":\"1\",\"name\":\"娱乐\",\"parentId\":\"-1\"},{\"id\":\"2\",\"name\":\"生活\",\"parentId\":\"1\"},{\"id\":\"3\",\"name\":\"音乐\",\"parentId\":\"1\"},{\"id\":\"4\",\"name\":\"健康\",\"parentId\":\"1\"},{\"id\":\"5\",\"name\":\"其他\",\"parentId\":\"1\"},{\"id\":\"6\",\"name\":\"游戏\",\"parentId\":\"-1\"},{\"id\":\"7\",\"name\":\"休闲\",\"parentId\":\"6\"},{\"id\":\"8\",\"name\":\"棋牌\",\"parentId\":\"6\"},{\"id\":\"9\",\"name\":\"动作\",\"parentId\":\"6\"},{\"id\":\"10\",\"name\":\"其他\",\"parentId\":\"6\"},{\"id\":\"11\",\"name\":\"专题\",\"parentId\":\"-1\"},{\"id\":\"12\",\"name\":\"工具\",\"parentId\":\"11\"},{\"id\":\"13\",\"name\":\"教育\",\"parentId\":\"11\"},{\"id\":\"14\",\"name\":\"咨询\",\"parentId\":\"11\"},{\"id\":\"15\",\"name\":\"其他\",\"parentId\":\"11\"},{\"id\":\"16\",\"name\":\"首页\",\"parentId\":\"-1\"}]";
				}
				if (!TextUtils.isEmpty(json)) {
					lastRequestCategoriesTime = System.currentTimeMillis();// 更改上次请求时间
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_CATEGORIES, json);// 缓存json数据
				} else {
					L.d("datacenter-loadCategories--server json is null,getting cache data");
					// 没有请求到服务器数据使用缓存文件
					json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_CATEGORIES);
				}
				categories = Parse.parseCategory(json);
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
	public void loadPageApps(final Context context, final LoadCompleteListener completeListener) {
		if (System.currentTimeMillis() - lastRequestPageAppsTime < Config.REQUEST_RESTTIEM) {
			String json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_PAGEAPPS);
			Parse.parsePageApps(json);
			if (categories != null && categories.size() > 0 && completeListener != null) {
				completeListener.onComplete();
				return;// 在一定的时间段内使用缓存数据不用重复请求服务器。
			}
		}
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getPagesUrl));
				if (Config.ISTEST && TextUtils.isEmpty(json)) {
					json = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"pages\":[{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"1\",\"position\":1,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"page\":\"1\",\"position\":2,\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"page\":\"1\",\"position\":3,\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"1\",\"position\":4,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"page\":\"1\",\"position\":5,\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":9,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"2\",\"position\":1,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"2\",\"position\":2,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"2\",\"position\":3,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":4,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":5,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":9,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"page\":\"3\",\"position\":1,\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"page\":\"3\",\"position\":2,\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"page\":\"3\",\"position\":3,\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":4,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":5,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":9,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":1,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":2,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":3,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":4,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":5,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"4\",\"position\":9,\"posterFilePath\":\"j9gd3z32ru.jpg\"}]}";
					// TODO 临时使用测试数据
				}
				if (!TextUtils.isEmpty(json)) {
					lastRequestPageAppsTime = System.currentTimeMillis();// 更改上次请求时间
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_PAGEAPPS, json);// 缓存json数据
				} else {
					L.d("datacenter-loadpageapps--server json is null,getting cache data");
					// 没有请求到服务器数据使用缓存文件
					json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_PAGEAPPS);
				}
				Parse.parsePageApps(json);
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
	public void loadAppsByCategoryId(final Context context, final int categoryId,
			final LoadListListener loadAppListListener) {
		if (lastRequestAppsByCategoryId.get(categoryId) != null
				&& System.currentTimeMillis() - lastRequestAppsByCategoryId.get(categoryId) < Config.REQUEST_RESTTIEM) {
			String jsonString = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_CATEGORYAPPS + categoryId);
			if (!TextUtils.isEmpty(jsonString) && loadAppListListener != null) {
				loadAppListListener.onComplete(Parse.parseCategoryApp(jsonString));
				return;
			}
		}
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				// 缓存中没有就去服务器请求
				String url = Config.getCategoryAppsUrl + "?categoryId=" + categoryId;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url));
				if (!TextUtils.isEmpty(jsonString)) {
					lastRequestAppsByCategoryId.put(categoryId, System.currentTimeMillis());
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_CATEGORYAPPS + categoryId, jsonString);
				} else {
					jsonString = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_CATEGORYAPPS + categoryId);
				}
				if (Config.ISTEST && TextUtils.isEmpty(jsonString)) {
					jsonString = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"values\":[{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":114,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":108,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":102,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":123,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":124,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":128,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":127,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":131,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":135,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":134,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":121,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}]}";
					// TODO 临时使用测试数据
				}

				return Parse.parseCategoryApp(jsonString);
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
				String url = Config.getAppDetailUrl + "?appId=" + appId;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url));
				if (Config.ISTEST && TextUtils.isEmpty(jsonString)) {
					// TODO 临时使用测试数据
					jsonString = "{\"apkFilePath\":\"2tx4dsmnx8.apk\",\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"appSize\":\"10.65\",\"appVersion\":\"2.004\",\"description\":\"西米隐隐西米隐隐\",\"download\":0,\"host\":\"http://localhost:8081/appmarket/upload/\",\"iconFilePath\":\"wrr3i3wy62.png\",\"posterFilePath\":\"sssth0c89d.png\"}";
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
	public  void loadAppSearch(final String keywords, final LoadListListener loadListListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String url = Config.getAppSearchUrl + "?keywords=" + keywords;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url));
				if (Config.ISTEST && TextUtils.isEmpty(jsonString)) {
					jsonString = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"values\":[{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":114,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":108,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":102,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":123,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":124,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":128,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}]}";
				}
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

	/**
	 * 获取需要更新的应用
	 * 
	 * @param packages
	 *            需要检测应用的包名
	 * @param loadListListener
	 */
	public  void loadAppsUpdateData(final List<String> packages, final LoadListListener loadListListener) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String url = Config.getAppVersionsUrl;
				if (packages == null || packages.size() == 0) {
					return null;
				}
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (int i = 0; i < packages.size(); i++) {
					paramList.add(new BasicNameValuePair("appPackages", packages.get(i)));
				}
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doPostRequest(url, paramList));
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
	/**
	 *获取详情页面推荐应用
	 * 
	 * @param packages
	 *            需要检测应用的包名
	 * @param loadListListener
	 */
	public  void loadRecommendData(final Context context,final int categoryId, final LoadListListener loadAppListListener) {
		if (lastRequestRecommendApps.get(categoryId) != null
				&& System.currentTimeMillis() - lastRequestRecommendApps.get(categoryId) < Config.REQUEST_RESTTIEM) {
			String jsonString = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_RECOMMENDAPPS + categoryId);
			if (!TextUtils.isEmpty(jsonString) && loadAppListListener != null) {
				loadAppListListener.onComplete(Parse.parseCategoryApp(jsonString));
				return;
			}
		}
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				// 缓存中没有就去服务器请求
				String url = Config.getDetailRecommendUrl + "?categoryId=" + categoryId;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url));
				if (!TextUtils.isEmpty(jsonString)) {
					lastRequestRecommendApps.put(categoryId, System.currentTimeMillis());
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_RECOMMENDAPPS + categoryId, jsonString);
				} else {
					jsonString = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_RECOMMENDAPPS + categoryId);
				}
				return Parse.parseRecommendApp(jsonString);
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
	 * app下载成功后提交服务器用于服务器统计
	 * 
	 * @param appId
	 *            appid信息
	 */
	public  void submitAppDownloadOK(final String appId) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = Config.putAppDownloadOK + "?" + "appId=" + appId + "&boxMac=" + MyApplication.deviceMac;
				HttpRequestUtil.doGetRequest(url);
			}
		}).start();
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
	
	public void loadRankingList(final Context context, final LoadObjectListener objectListener) {
		boolean result;
		if (System.currentTimeMillis() - lastRequestRankListTime < Config.REQUEST_RESTTIEM
				&& lastRequestRankListTime != 0) {
			String json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_RANKLIST);
			result = Parse.parseRankingList(json);
			if (result == true) {
				objectListener.onComplete(result);
				return;// 在一定的时间段内使用缓存数据不用重复请求服务器。
			}
		}
		
		new AsyncTask<Void, Void, Boolean>() {
			boolean result;
			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getAppRankListUrl));
				if (Config.ISTEST && TextUtils.isEmpty(json)) {
					json = Parse.json_appranklist;
				}
				if (!TextUtils.isEmpty(json)) {
					lastRequestRankListTime = System.currentTimeMillis();// 更改上次请求时间
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_RANKLIST, json);// 缓存json数据
				} else {
					L.d("datacenter-loadRankingList--server json is null,getting cache data");
					// 没有请求到服务器数据使用缓存文件
					json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_RANKLIST);
				}
				result = Parse.parseRankingList(json);
				return result;
			}
			@Override
			protected void onPostExecute(Boolean result) {
				objectListener.onComplete(result);
			}
			
		}.execute((Void[]) null);
	}
}
