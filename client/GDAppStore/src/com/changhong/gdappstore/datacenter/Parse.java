package com.changhong.gdappstore.datacenter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.changhong.gdappstore.Config;
import com.changhong.gdappstore.MyApplication;
import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.util.L;
import com.changhong.gdappstore.util.Util;

/**
 * 数据解析类
 * 
 * @author wangxiufeng
 * 
 */
public class Parse {
	
	/**************************************************************************/
	public final static String HOST = "host";
	public final static String VALUES = "values";
	/**
	 * *******************************Category
	 * Part*********************************************
	 */

	public final static String CATEGORY_ID = "id";

	public final static String CATEGORY_NAME = "name";

	public final static String CATEGORY_PARENTID = "pid";

	public final static String CATEGORY_FILENAME = "fn";

	/************************************ APP Part ************************************************/

	public final static String APP_ID = "id";

	public final static String APP_NAME = "name";

	public final static String APP_KEY = "key";

	public final static String APP_VERSION_INT = "ver_int";

	public final static String APP_VERSION = "ver";

	public final static String APP_PACKAGE = "package";

	public final static String APP_SIZE = "size";

	public final static String APP_DOWNLOAD = "download";

	public final static String APP_UPDATE_DATE = "date";

	public final static String APP_DESCRIPTION = "desc";

	public final static String APP_ICON_FILEPATH = "icon_fp";

	public final static String APP_POSTER_FILEPATH = "poster_fp";

	public final static String APP_APK_FILEPATH = "apk_fp";

	public final static String APP_CATEGORY_ID = "cate_id";

	public final static String APP_SCORES = "scores";

	public final static String APP_RECOMMEND = "recommend";

	/**
	 * 解析栏目数据
	 * 
	 * @param categoryJson
	 *            栏目数据json
	 * @return
	 */
	public static List<Category> parseCategory(String categoryJson) {
		List<Category> categories = new ArrayList<Category>();
		if (TextUtils.isEmpty(categoryJson)) {
			return categories;
		}
		try {
			JSONObject categoryObject = new JSONObject(categoryJson);
			String host = "";
			if (categoryObject.has(HOST)) {
				host = categoryObject.getString(HOST).trim();
			}
			if (categoryObject.has("client_url")) {
				MyApplication.UPDATE_APKURL = categoryObject.getString("client_url").trim();
			}
			if (categoryObject.has("client_v")) {
				MyApplication.SERVER_VERSION = categoryObject.getInt("client_v");
			}
			L.d("server apk data  version=" + MyApplication.SERVER_VERSION + " apkurl=" + MyApplication.UPDATE_APKURL);
			JSONArray array = categoryObject.getJSONArray(VALUES);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Category category = new Category();
				if (object.has(CATEGORY_ID)) {
					category.setId(object.getInt(CATEGORY_ID));
				}
				if (object.has(CATEGORY_NAME)) {
					category.setName(object.getString(CATEGORY_NAME).trim());
				}
				if (object.has(CATEGORY_FILENAME) && !TextUtils.isEmpty(object.getString(CATEGORY_FILENAME))) {
					category.setIconFilePath(host + "category/" + object.getString(CATEGORY_FILENAME).trim());
				}
				int parentid = -1;
				if (object.has(CATEGORY_PARENTID)) {
					parentid = object.getInt(CATEGORY_PARENTID);
					category.setParentId(parentid);
				}
				if (parentid == -1) {// 添加为父栏目
					categories.add(category);
				} else {
					category.setCategoryPageApps(null);// 子栏目没有推荐位应用
					category.setCategoyChildren(null);// 子栏目没有子栏目
					for (int j = 0; j < categories.size(); j++) {
						if (categories.get(j).getId() == parentid) {
							// 添加为子栏目
							categories.get(j).getCategoyChildren().add(category);
							break;
						}
					}
				}
			}
			// TODO 将首页手动添加为第一个
			categories.add(0, new Category(0, -1, Config.HOMEPAGE, "", new ArrayList<Category>(),
					new ArrayList<PageApp>()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return categories;
	}

	/**
	 * 解析一级页面推荐位应用
	 * 
	 * @param pageAppJson
	 */
	public static void parsePageApps(String pageAppJson) {
		if (TextUtils.isEmpty(pageAppJson)) {
			L.w("returned by pageAppJson is empty when parsePageApps");
			return;
		}
		DataCenter dataCenter = DataCenter.getInstance();
		if (dataCenter.getCategories() == null || dataCenter.getCategories().size() == 0) {
			L.w("returned by dataCenter.getCategories() is empty when parsePageApps");
			return;
		}
		try {
			JSONObject object = new JSONObject(pageAppJson);
			String host = object.getString(HOST).trim();
			JSONArray array = object.getJSONArray("pages");
			for (int i = 0; i < array.length(); i++) {
				PageApp app = new PageApp();
				JSONObject appobject = array.getJSONObject(i);
				if (appobject.has(APP_ID)) {
					app.setAppid(appobject.getInt(APP_ID));
				}
				if (appobject.has(APP_KEY)) {
					app.setAppkey(appobject.getString(APP_KEY));
				}
				if (appobject.has(APP_NAME)) {
					app.setAppname(appobject.getString(APP_NAME));
				}
				if (appobject.has("page")) {
					app.setPageid(appobject.getInt("page"));
				}
				if (appobject.has("position")) {
					app.setPosition(appobject.getInt("position"));
				}
				if (appobject.has(APP_POSTER_FILEPATH) && !TextUtils.isEmpty(appobject.getString(APP_POSTER_FILEPATH))) {
					app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString(APP_POSTER_FILEPATH));
				}
				if (appobject.has(APP_ICON_FILEPATH) && !TextUtils.isEmpty(appobject.getString(APP_ICON_FILEPATH))) {
					app.setIconFilePath(host + app.getAppkey() + "/" + appobject.getString(APP_ICON_FILEPATH));
				}
				// TODO 临时写死的匹配
				for (int j = 0; j < dataCenter.getCategories().size(); j++) {
					// L.d("dataCenter.getCategories().get(j).getId()==="+dataCenter.getCategories().get(j).getId());
					if (dataCenter.getCategories().get(j).getId() == 0 && app.getPageid() == 1) {
						dataCenter.getCategories().get(j).getCategoryPageApps().add(app);
					} else if (dataCenter.getCategories().get(j).getId() == 1 && app.getPageid() == 2) {
						dataCenter.getCategories().get(j).getCategoryPageApps().add(app);
					} else if (dataCenter.getCategories().get(j).getId() == 6 && app.getPageid() == 3) {
						dataCenter.getCategories().get(j).getCategoryPageApps().add(app);
					} else if (dataCenter.getCategories().get(j).getId() == 11 && app.getPageid() == 4) {
						dataCenter.getCategories().get(j).getCategoryPageApps().add(app);
					}
				}
			}
			// for (int i = 0; i < dataCenter.getCategories().size(); i++) {
			// L.d("parse pageapps--" + dataCenter.getCategories().get(i));
			// }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析栏目（分类）下的app列表
	 * 
	 * @param categoryAppJson
	 * @return
	 */
	public static List<Object> parseCategoryApp(String categoryAppJson) {
		List<Object> apps = new ArrayList<Object>();

		if (TextUtils.isEmpty(categoryAppJson)) {
			L.w("returned by categoryAppJson is empty when parseCategoryApp");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(categoryAppJson);
			String host = object.getString(HOST).trim();
			JSONArray array = object.getJSONArray(VALUES);
			for (int i = 0; i < array.length(); i++) {
				App app = new App();
				JSONObject appobject = array.getJSONObject(i);
				parseApp(appobject, app, host);
				apps.add(app);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return sortAppByRecommend(apps);
	}

	private static List<Object> sortAppByRecommend(List<Object> apps) {
		// 将推荐标识的放在前面
		List<Object> sortapps = new ArrayList<Object>();
		if (apps == null) {
			return sortapps;
		}
		for (int i = 0; i < apps.size(); i++) {
			App app = (App) apps.get(i);
			if (app.isRecommend()) {
				sortapps.add(0, app);
			} else {
				sortapps.add(app);
			}
		}
		return sortapps;
	}

	/**
	 * 解析详情页面推荐位app
	 * 
	 * @param categoryAppJson
	 * @return
	 */
	public static List<Object> parseRecommendApp(String categoryAppJson) {
		List<Object> apps = new ArrayList<Object>();

		if (TextUtils.isEmpty(categoryAppJson)) {
			L.w("returned by categoryAppJson is empty when parseRecommendApp");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(categoryAppJson);
			String host = object.getString(HOST).trim();
			JSONArray array = object.getJSONArray(VALUES);
			for (int i = 0; i < array.length(); i++) {
				App app = new App();
				JSONObject appobject = array.getJSONObject(i);
				parseApp(appobject, app, host);
				apps.add(app);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return sortAppByRecommend(apps);
	}

	/**
	 * 解析静默安装静默卸载应用
	 * 
	 * @param categoryAppJson
	 * @return
	 */
	public static List<List<Object>> parseSilentInstallApp(String categoryAppJson) {
		List<List<Object>> apps=new ArrayList<List<Object>>();
		List<Object> silentInstallApps = new ArrayList<Object>();
		List<Object> silentUnInstallApps = new ArrayList<Object>();
		apps.add(silentInstallApps);//安装列表
		apps.add(silentUnInstallApps);//卸载列表
		if (TextUtils.isEmpty(categoryAppJson)) {
			L.w("returned by categoryAppJson is empty when parseRecommendApp");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(categoryAppJson);
			String host = object.getString(HOST).trim();
			JSONArray array = object.getJSONArray(VALUES);
			for (int i = 0; i < array.length(); i++) {
				App app = new App();
				JSONObject appobject = array.getJSONObject(i);
				if (appobject.has(APP_ID)) {
					app.setAppid(appobject.getInt(APP_ID));
				}
				if (appobject.has(APP_PACKAGE)) {
					app.setPackageName(appobject.getString(APP_PACKAGE).trim());
				}
				if (appobject.has(APP_KEY)) {
					app.setAppkey(appobject.getString(APP_KEY).trim());
				}
				if (appobject.has(APP_VERSION_INT)) {
					app.setVersionInt(appobject.getInt(APP_VERSION_INT));
				}
				if (appobject.has(APP_APK_FILEPATH) && !TextUtils.isEmpty(appobject.getString(APP_APK_FILEPATH))) {
					app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString(APP_APK_FILEPATH).trim());
					//TODO 使用海报图片路径代替apk路径
				}
				boolean install = appobject.getBoolean("install");
				if (install) {
					silentInstallApps.add(app);
				} else {
					silentUnInstallApps.add(app);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return apps;
	}
	/**
	 * 解析广告数据
	 * @param bootADJson
	 * @return
	 */
	public static String parseBootAD(String bootADJson) {
		if (TextUtils.isEmpty(bootADJson)) {
			L.w("returned by appdetailJson is empty when bootADJson");
			return "";
		}
		String bootADUrl="";
		try {
			JSONObject object=new JSONObject(bootADJson);
			String host=object.getString(HOST);
			String bootImg=object.getString("boot_img");
			bootADUrl=host+bootImg;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bootADUrl;
	}

	public static AppDetail parseAppDetail(String appdetailJson) {
		AppDetail appDetail = new AppDetail();
		if (TextUtils.isEmpty(appdetailJson)) {
			L.w("returned by appdetailJson is empty when parseAppDetail");
			return null;
		}
		try {
			JSONObject object = new JSONObject(appdetailJson);
			String host = object.getString(HOST).trim();
			String appKey = object.getString(APP_KEY).trim();
			appDetail.setAppkey(appKey);
			appDetail.setHost(host);
			appDetail.setApkFilePath(host + appKey + "/" + object.getString(APP_APK_FILEPATH).trim());
			appDetail.setAppid(object.getInt(APP_ID));
			appDetail.setAppname(object.getString(APP_NAME).trim());
			appDetail.setApkSize(object.getString(APP_SIZE).trim());
			appDetail.setVersion(object.getString(APP_VERSION).trim());
			appDetail.setVersionInt(object.getInt(APP_VERSION_INT));
			appDetail.setDescription(object.getString(APP_DESCRIPTION).trim());
			appDetail.setDownload(object.getString(APP_DOWNLOAD).trim());
			appDetail.setPackageName(object.getString(APP_PACKAGE).trim());
			appDetail.setUpdateDate(object.getString(APP_UPDATE_DATE).trim());
			appDetail.setCategoryId(object.getInt(APP_CATEGORY_ID));
			appDetail.setScores(object.getInt(APP_SCORES));
			appDetail.setRecommend(object.getBoolean(APP_RECOMMEND));
			if (object.has(APP_ICON_FILEPATH) && !TextUtils.isEmpty(object.getString(APP_ICON_FILEPATH))) {
				appDetail.setIconFilePath(host + appKey + "/" + object.getString(APP_ICON_FILEPATH).trim());
			}
			if (object.has(APP_POSTER_FILEPATH) && !TextUtils.isEmpty(object.getString(APP_POSTER_FILEPATH))) {
				appDetail.setPosterFilePath(host + appKey + "/" + object.getString(APP_POSTER_FILEPATH).trim());
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return appDetail;
	}

	/**
	 * 解析搜索json数据
	 * 
	 * @param searchAppsJson
	 * @return
	 */
	public static List<Object> parseSearchApps(String searchAppsJson) {
		List<Object> apps = new ArrayList<Object>();

		if (TextUtils.isEmpty(searchAppsJson)) {
			L.w("returned by searchAppsJson is empty when parseSearchApps");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(searchAppsJson);
			String host = object.getString(HOST).trim();
			JSONArray array = object.getJSONArray(VALUES);
			for (int i = 0; i < array.length(); i++) {
				App app = new App();
				JSONObject appobject = array.getJSONObject(i);
				parseApp(appobject, app, host);
				apps.add(app);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return sortAppByRecommend(apps);
	}

	/**
	 * 解析需要升级的json数据
	 * 
	 * @param appUpdateJson
	 * @return
	 */
	public static List<Object> parseAppVersions(String appVersionsJson) {
		List<Object> apps = new ArrayList<Object>();

		if (TextUtils.isEmpty(appVersionsJson)) {
			L.w("returned by categoryAppJson is empty when parseAppVersions");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(appVersionsJson);
			String host = object.getString(HOST).trim();
			JSONArray array = object.getJSONArray(VALUES);
			for (int i = 0; i < array.length(); i++) {
				App app = new App();
				JSONObject appobject = array.getJSONObject(i);
				parseApp(appobject, app, host);
				apps.add(app);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return apps;
	}

	private static void parseApp(JSONObject appobject, App app, String host) {
		try {
			if (appobject.has(APP_ID)) {
				app.setAppid(appobject.getInt(APP_ID));
			}
			if (appobject.has(APP_PACKAGE)) {
				app.setPackageName(appobject.getString(APP_PACKAGE).trim());
			}
			if (appobject.has(APP_KEY)) {
				app.setAppkey(appobject.getString(APP_KEY).trim());
			}
			if (appobject.has(APP_NAME)) {
				app.setAppname(appobject.getString(APP_NAME).trim());
			}
			if (appobject.has(APP_SIZE)) {
				app.setApkSize(appobject.getString(APP_SIZE).trim());
			}
			if (appobject.has(APP_VERSION)) {
				app.setVersion(appobject.getString(APP_VERSION).trim());
			}
			if (appobject.has(APP_VERSION_INT)) {
				app.setVersionInt(appobject.getInt(APP_VERSION_INT));
			}
			if (appobject.has(APP_SCORES)) {
				app.setScores(appobject.getInt(APP_SCORES));
			}
			if (appobject.has(APP_RECOMMEND)) {
				app.setRecommend(appobject.getBoolean(APP_RECOMMEND));
			}
			if (appobject.has(APP_POSTER_FILEPATH) && !TextUtils.isEmpty(appobject.getString(APP_POSTER_FILEPATH))) {
				app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString(APP_POSTER_FILEPATH).trim());
			}
			if (appobject.has(APP_ICON_FILEPATH) && !TextUtils.isEmpty(appobject.getString(APP_ICON_FILEPATH))) {
				app.setIconFilePath(host + app.getAppkey() + "/" + appobject.getString(APP_ICON_FILEPATH).trim());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean parseRankingList(String rankingListJson) {
		if (TextUtils.isEmpty(rankingListJson)) {
			L.w("rankingListJson is null!");
			return false;
		}
		RankingData rankingData = RankingData.getInstance();

		try {
			JSONObject object = new JSONObject(rankingListJson);

			String host = object.getString(HOST).trim();
			JSONArray surgeListJsonArray = object.getJSONArray("FASTEST");
			JSONArray hotListJsonArray = object.getJSONArray("HOTEST");
			JSONArray newListJsonArray = object.getJSONArray("NEWEST");

			ArrayList<Ranking_Item> newArrayList = parseRankingApp(newListJsonArray);
			ArrayList<Ranking_Item> hotArrayList = parseRankingApp(hotListJsonArray);
			ArrayList<Ranking_Item> surgeArrayList = parseRankingApp(surgeListJsonArray);

			rankingData.setHost(host);

			if (newArrayList != null && !newArrayList.isEmpty()) {
				rankingData.setNewRankingData(newArrayList);
			}
			if (hotArrayList != null && !hotArrayList.isEmpty()) {
				rankingData.setHotRankingData(hotArrayList);
			}
			if (surgeArrayList != null && !surgeArrayList.isEmpty()) {
				rankingData.setSurgeRankingData(surgeArrayList);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	private static ArrayList<Ranking_Item> parseRankingApp(JSONArray jsonArray) {
		ArrayList<Ranking_Item> rankingList = null;

		if (jsonArray == null) {
			return rankingList;
		}
		rankingList = new ArrayList<Ranking_Item>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				Ranking_Item ranking_Item = new Ranking_Item();
				JSONObject object = jsonArray.getJSONObject(i);
				if (object.has(APP_ID)) {
					ranking_Item.setAppId(object.getInt(APP_ID));
				}
				if (object.has(APP_KEY)) {
					ranking_Item.setAppKey(object.getString(APP_KEY).trim());
				}
				if (object.has(APP_NAME)) {
					ranking_Item.setAppName(object.getString(APP_NAME).trim());
				}
				if (object.has(APP_ICON_FILEPATH)) {
					ranking_Item.setAppIconPath(object.getString(APP_ICON_FILEPATH).trim());
				}
				if (object.has(APP_DOWNLOAD)) {
					ranking_Item.setDownload_num(Util.intToStr(object.getInt(APP_DOWNLOAD)));
				}
				if (object.has(APP_SIZE)) {
					ranking_Item.setAppSize(object.getString(APP_SIZE).trim());
				}
				if (object.has(APP_SCORES)) {
					ranking_Item.setScores(object.getInt(APP_SCORES));
				}
				ranking_Item.setTopNum(i + 1);

				rankingList.add(ranking_Item);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			L.w("Ranking_Item parse error!");
			if (rankingList != null) {
				rankingList.clear();
			}
			rankingList = null;
		}
		return rankingList;
	}
}
