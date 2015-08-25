package com.changhong.gdappstore.datacenter;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.model.AppDetail;
import com.changhong.gdappstore.model.Category;
import com.changhong.gdappstore.model.PageApp;
import com.changhong.gdappstore.model.RankingData;
import com.changhong.gdappstore.model.Ranking_Item;
import com.changhong.gdappstore.util.L;

/**
 * 数据解析类
 * 
 * @author wangxiufeng
 * 
 */
public class Parse {
	// /** 栏目（分类）的json数据 */
	public static String json_appranklist = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"FASTEST\":[{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"posterFilePath\":\"sssth0c89d.png\"}],\"HOTEST\":[{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":128,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":127,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":131,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":135,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":134,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":121,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}],\"NEWEST\":[{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":137,\"appKey\":\"j4pyq3zv\",\"appName\":\"酷狗音乐\",\"posterFilePath\":\"2b0ckiz81r.apk\"},{\"appId\":2,\"appKey\":\"17c0cu1y\",\"appName\":\"网页新闻\",\"posterFilePath\":\"6ec33tpang.apk\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":123,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":124,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}]}";

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
			JSONObject categoryObject=new JSONObject(categoryJson);
			String host="";
			if (categoryObject.has("host")) {
				host=categoryObject.getString("host");
			}
			JSONArray array = categoryObject.getJSONArray("values");
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Category category = new Category();
				if (object.has("id")) {
					category.setId(object.getInt("id"));
				}
				if (object.has("name")) {
					category.setName(object.getString("name"));
				}
				if (object.has("filename")) {
					category.setIconFilePath(host+object.getString("filename"));
				}
				int parentid = -1;
				if (object.has("parentId")) {
					parentid = object.getInt("parentId");
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
			categories.add(0, new Category(0, -1, "首页", "", new ArrayList<Category>(), new ArrayList<PageApp>()));
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
			String host = object.getString("host");
			JSONArray array = object.getJSONArray("pages");
			for (int i = 0; i < array.length(); i++) {
				PageApp app = new PageApp();
				JSONObject appobject = array.getJSONObject(i);
				if (appobject.has("appId")) {
					app.setAppid(appobject.getInt("appId"));
				}
				if (appobject.has("appKey")) {
					app.setAppkey(appobject.getString("appKey"));
				}
				if (appobject.has("appName")) {
					app.setAppname(appobject.getString("appName"));
				}
				if (appobject.has("page")) {
					app.setPageid(appobject.getInt("page"));
				}
				if (appobject.has("position")) {
					app.setPosition(appobject.getInt("position"));
				}
				if (appobject.has("posterFilePath")) {
					app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString("posterFilePath"));
				}

				// TODO 临时写死的匹配
				for (int j = 0; j < dataCenter.getCategories().size(); j++) {
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
			for (int i = 0; i < dataCenter.getCategories().size(); i++) {
				L.d("parse pageapps--" + dataCenter.getCategories().get(i));
			}
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
			String host = object.getString("host");
			JSONArray array = object.getJSONArray("values");
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

	/**
	 * 解析详情页面推荐位app
	 * 
	 * @param categoryAppJson
	 * @return
	 */
	public static List<Object> parseRecommendApp(String categoryAppJson) {
		List<Object> apps = new ArrayList<Object>();

		if (TextUtils.isEmpty(categoryAppJson)) {
			L.w("returned by categoryAppJson is empty when parseCategoryApp");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(categoryAppJson);
			String host = object.getString("host");
			JSONArray array = object.getJSONArray("values");
			for (int i = 0; i < array.length(); i++) {
				App app = new App();
				JSONObject appobject = array.getJSONObject(i);
				if (appobject.has("appId")) {
					app.setAppid(appobject.getInt("appId"));
				}
//				if (appobject.has("package")) {
//					app.setPackageName(appobject.getString("package"));
//				}
				if (appobject.has("appKey")) {
					app.setAppkey(appobject.getString("appKey"));
				}
				if (appobject.has("appName")) {
					app.setAppname(appobject.getString("appName"));
				}
				if (appobject.has("download")) {
				}
				if (appobject.has("iconFilePath")) {
					app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString("iconFilePath"));
				}
				apps.add(app);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return apps;
	}

	public static AppDetail parseAppDetail(String appdetailJson) {
		AppDetail appDetail = new AppDetail();
		if (TextUtils.isEmpty(appdetailJson)) {
			L.w("returned by appdetailJson is empty when parseAppDetail");
			return appDetail;
		}
		try {
			JSONObject object = new JSONObject(appdetailJson);
			String host = object.getString("host");
			String appKey = object.getString("appKey");
			appDetail.setAppkey(appKey);
			appDetail.setApkFilePath(host + appKey + "/" + object.getString("apkFilePath"));
			appDetail.setAppid(object.getInt("appId"));
			appDetail.setAppname(object.getString("appName"));
			appDetail.setApkSize(object.getString("appSize"));
			appDetail.setVersion(object.getString("appVersion"));
			appDetail.setDescription(object.getString("description"));
			appDetail.setDownload(object.getString("download"));
			appDetail.setPackageName(object.getString("appPackage"));
			appDetail.setCategoryId(object.getInt("categoryId"));
			appDetail.setIconFilePath(host + appKey + "/" + object.getString("iconFilePath"));
			appDetail.setPosterFilePath(host + appKey + "/" + object.getString("posterFilePath"));

		} catch (JSONException e) {
			e.printStackTrace();
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
			String host = object.getString("host");
			JSONArray array = object.getJSONArray("values");
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

	/**
	 * 解析需要升级的json数据
	 * 
	 * @param appUpdateJson
	 * @return
	 */
	public static List<Object> parseAppVersions(String appVersionsJson) {
		List<Object> apps = new ArrayList<Object>();

		if (TextUtils.isEmpty(appVersionsJson)) {
			L.w("returned by categoryAppJson is empty when parseCategoryApp");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(appVersionsJson);
			String host = object.getString("host");
			JSONArray array = object.getJSONArray("values");
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
			if (appobject.has("appId")) {
				app.setAppid(appobject.getInt("appId"));
			}
			if (appobject.has("package")) {
				app.setPackageName(appobject.getString("package"));
			}
			if (appobject.has("appKey")) {
				app.setAppkey(appobject.getString("appKey"));
			}
			if (appobject.has("appName")) {
				app.setAppname(appobject.getString("appName"));
			}
			if (appobject.has("appVersion")) {
				app.setVersion(appobject.getString("appVersion"));
			}
			if (appobject.has("posterFilePath")) {
				app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString("posterFilePath"));
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

			String host = object.getString("host");
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
				if (object.has("appId")) {
					ranking_Item.setAppId(object.getInt("appId"));
				}
				if (object.has("appKey")) {
					ranking_Item.setAppKey(object.getString("appKey"));
				}
				if (object.has("appName")) {
					ranking_Item.setAppName(object.getString("appName"));
				}
				if (object.has("iconFilePath")) {
					ranking_Item.setAppIconPath(object.getString("iconFilePath"));
				}
				if (object.has("download")) {
					ranking_Item.setDownload_num(object.getString("download"));
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
