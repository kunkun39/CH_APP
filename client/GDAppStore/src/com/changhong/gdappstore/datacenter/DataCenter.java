package com.changhong.gdappstore.datacenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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

/**
 * 数据中心类
 * 
 * @author wangxiufeng
 * 
 */
public class DataCenter {
	
	public static final int LOAD_CACHEDATA_SUCCESS = 1;
	public static final int LOAD_CACHEDATA_FAIL = 2;
	public static final int LOAD_CACHEDATA_NO_UPDATE = 3;
	public static final int LOAD_SERVERDATA_SUCCESS = 4;
	public static final int LOAD_SERVERDATA_FAIL = 5;

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
	 *  请求解析栏目分类和每页应用数据
	 * @param context
	 * @param completeListener
	 * @param getCacheData 是否预加载本地缓存
	 */
	public void loadCategoryAndPageData(final Context context, final LoadCompleteListener completeListener,boolean getCacheData) {
		loadCategories(context, new LoadCompleteListener() {
			
			@Override
			public void onComplete() {
				loadPageApps(context, completeListener,true);
			}
		},true);
	}
	/**
	 * 加载栏目分类数据
	 * @param context
	 * @param completeListener
	 * @param getCacheData 是否预加载本地缓存
	 */
	public void loadCategories(final Context context, final LoadCompleteListener completeListener,boolean getCacheData) {
		if (getCacheData&&Config.ISCACHEABLE) {
			String json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_CATEGORIES);
			categories = Parse.parseCategory(json);
			if (categories != null && categories.size() > 0 && completeListener != null) {
				completeListener.onComplete();
			}
		}
		loadCategories(context, completeListener);
	}
	/**
	 * 加载栏目分类数据
	 * @param context
	 * @param completeListener
	 */
	public void loadCategories(final Context context, final LoadCompleteListener completeListener) {
		if (Config.ISCACHEABLE && (System.currentTimeMillis() - lastRequestCategoriesTime) < Config.REQUEST_RESTTIEM) {
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
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getCategoryUrl,context),context);
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
	 * 加载一级页面推荐应用
	 * @param context
	 * @param completeListener
	 * @param getCacheData 是否预加载本地缓存
	 */
	public void loadPageApps(final Context context, final LoadCompleteListener completeListener, boolean getCacheData) {
		if (getCacheData && Config.ISCACHEABLE) {
			String json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_PAGEAPPS);
			Parse.parsePageApps(json);
			if (categories != null && categories.size() > 0 && completeListener != null) {
				completeListener.onComplete();
			}
		}
		if (categories == null || categories.size() <=0 ) {
			loadCategories(context, new LoadCompleteListener() {
				
				@Override
				public void onComplete() {//没有栏目数据要先去获取栏目数据
					loadPageApps(context, completeListener);
				}
			});
		}else {
			loadPageApps(context, completeListener);
		}
	}
	/**
	 * 加载一级页面推荐应用
	 * @param context
	 * @param completeListener
	 */
	public void loadPageApps(final Context context, final LoadCompleteListener completeListener) {
		if (Config.ISCACHEABLE && (System.currentTimeMillis() - lastRequestPageAppsTime) < Config.REQUEST_RESTTIEM) {
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
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getPagesUrl,context),context);
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
		if (Config.ISCACHEABLE && lastRequestAppsByCategoryId.get(categoryId) != null
				&& (System.currentTimeMillis() - lastRequestAppsByCategoryId.get(categoryId)) < Config.REQUEST_RESTTIEM) {
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
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url,context),context);
				if (!TextUtils.isEmpty(jsonString)) {
					lastRequestAppsByCategoryId.put(categoryId, System.currentTimeMillis());
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_CATEGORYAPPS + categoryId, jsonString);
				} else {
					jsonString = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_CATEGORYAPPS + categoryId);
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
	public void loadAppDetail(final int appId, final LoadObjectListener loadObjectListener,final Context context) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				// 缓存中没有就去服务器请求
				String url = Config.getAppDetailUrl + "?appId=" + appId;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url,context),context);
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
	public void loadAppSearch(final String keywords, final LoadListListener loadListListener,final Context context) {
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				String url = Config.getAppSearchUrl + "?keywords=" + keywords;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url,context),context);
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
	public void loadAppsUpdateData(final List<String> packages, final LoadListListener loadListListener,final Context context) {
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
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doPostRequest(url, paramList,context),context);
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
	 * 获取详情页面推荐应用
	 * 
	 * @param packages
	 *            需要检测应用的包名
	 * @param loadListListener
	 */
	public void loadRecommendData(final Context context, final int categoryId,
			final LoadListListener loadAppListListener) {
		if (Config.ISCACHEABLE && lastRequestRecommendApps.get(categoryId) != null
				&& (System.currentTimeMillis() - lastRequestRecommendApps.get(categoryId)) < Config.REQUEST_RESTTIEM) {
			String jsonString = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_RECOMMENDAPPS + categoryId);
			if (!TextUtils.isEmpty(jsonString) && loadAppListListener != null) {
				loadAppListListener.onComplete(Parse.parseRecommendApp(jsonString));
				return;
			}
		}
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				// 缓存中没有就去服务器请求
				String url = Config.getDetailRecommendUrl + "?categoryId=" + categoryId;
				String jsonString = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(url,context),context);
				if (!TextUtils.isEmpty(jsonString)) {
					lastRequestRecommendApps.put(categoryId, System.currentTimeMillis());
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_RECOMMENDAPPS + categoryId, jsonString);
				} else {
					jsonString = CacheManager
							.getJsonFileCache(context, CacheManager.KEYJSON_RECOMMENDAPPS + categoryId);
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
	public void submitAppDownloadOK(final String appId,final Context context) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				String url = Config.putAppDownloadOK + "?" + "appId=" + appId + "&boxMac=" + MyApplication.deviceMac;
				HttpRequestUtil.doGetRequest(url,context);
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
	public void loadRankingList(final Context context, final LoadObjectListener objectListener,boolean getCacheData) {
		boolean result;
		if(getCacheData && Config.ISCACHEABLE) {
			String json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_RANKLIST);
			result = Parse.parseRankingList(json);
			if (result == true) {
				objectListener.onComplete(LOAD_CACHEDATA_SUCCESS);
			}
		}
		
		loadRankingList(context,objectListener);
	}
	public void loadRankingList(final Context context, final LoadObjectListener objectListener) {
		boolean result;
		if (Config.ISCACHEABLE && (System.currentTimeMillis() - lastRequestRankListTime) < Config.REQUEST_RESTTIEM
				&& lastRequestRankListTime != 0) {
			String json = CacheManager.getJsonFileCache(context, CacheManager.KEYJSON_RANKLIST);
			result = Parse.parseRankingList(json);
			if (result == true) {
				objectListener.onComplete(LOAD_CACHEDATA_NO_UPDATE);
				return;// 在一定的时间段内使用缓存数据不用重复请求服务器。
			}
		}

		new AsyncTask<Void, Void, Boolean>() {
			boolean result;

			@Override
			protected Boolean doInBackground(Void... params) {
				// TODO Auto-generated method stub
				String json = HttpRequestUtil.getEntityString(HttpRequestUtil.doGetRequest(Config.getAppRankListUrl,context),context);
				if (!TextUtils.isEmpty(json)) {
					lastRequestRankListTime = System.currentTimeMillis();// 更改上次请求时间
					CacheManager.putJsonFileCache(context, CacheManager.KEYJSON_RANKLIST, json);// 缓存json数据
				} else {
					L.d("datacenter-loadRankingList--server json is null");
					// 没有请求到服务器数据使用缓存文件
					return false;
				}
				result = Parse.parseRankingList(json);
				return result;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if(result == true) {
					objectListener.onComplete(LOAD_SERVERDATA_SUCCESS);
				}
				else {
					objectListener.onComplete(LOAD_SERVERDATA_FAIL);
				}
			}

		}.execute((Void[]) null);
	}
}
