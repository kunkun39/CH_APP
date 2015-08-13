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
import com.changhong.gdappstore.util.L;

/**
 * 数据解析类
 * 
 * @author wangxiufeng
 * 
 */
public class Parse {
	/** 栏目（分类）的json数据 */
	public static String json_categories = "[{\"id\":\"1\",\"name\":\"娱乐\",\"parentId\":\"-1\"},{\"id\":\"2\",\"name\":\"生活\",\"parentId\":\"1\"},{\"id\":\"3\",\"name\":\"音乐\",\"parentId\":\"1\"},{\"id\":\"4\",\"name\":\"健康\",\"parentId\":\"1\"},{\"id\":\"5\",\"name\":\"其他\",\"parentId\":\"1\"},{\"id\":\"6\",\"name\":\"游戏\",\"parentId\":\"-1\"},{\"id\":\"7\",\"name\":\"休闲\",\"parentId\":\"6\"},{\"id\":\"8\",\"name\":\"棋牌\",\"parentId\":\"6\"},{\"id\":\"9\",\"name\":\"动作\",\"parentId\":\"6\"},{\"id\":\"10\",\"name\":\"其他\",\"parentId\":\"6\"},{\"id\":\"11\",\"name\":\"专题\",\"parentId\":\"-1\"},{\"id\":\"12\",\"name\":\"工具\",\"parentId\":\"11\"},{\"id\":\"13\",\"name\":\"教育\",\"parentId\":\"11\"},{\"id\":\"14\",\"name\":\"咨询\",\"parentId\":\"11\"},{\"id\":\"15\",\"name\":\"其他\",\"parentId\":\"11\"},{\"id\":\"16\",\"name\":\"首页\",\"parentId\":\"-1\"}]";
	/** 一级页面每个页面app获取json数据 */
	public static String json_pageapps = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"pages\":[{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"1\",\"position\":1,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"page\":\"1\",\"position\":2,\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"page\":\"1\",\"position\":3,\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"1\",\"position\":4,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"page\":\"1\",\"position\":5,\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"1\",\"position\":9,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"2\",\"position\":1,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"2\",\"position\":2,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"2\",\"position\":3,\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":4,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":5,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"2\",\"position\":9,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"page\":\"3\",\"position\":1,\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"page\":\"3\",\"position\":2,\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"page\":\"3\",\"position\":3,\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":4,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":5,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"3\",\"position\":9,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":1,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":2,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":3,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":4,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"page\":\"4\",\"position\":5,\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":6,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":7,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"page\":\"4\",\"position\":8,\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"page\":\"4\",\"position\":9,\"posterFilePath\":\"j9gd3z32ru.jpg\"}]}";
	/** 分类下appjson数据缓存对象 */
	public static Map<Integer, String> json_categoryapps = new HashMap<Integer, String>();

	public static String json_appdetail = "{\"apkFilePath\":\"2tx4dsmnx8.apk\",\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"appSize\":\"10.65\",\"appVersion\":\"2.004\",\"description\":\"西米隐隐西米隐隐\",\"download\":0,\"host\":\"http://localhost:8081/appmarket/upload/\",\"iconFilePath\":\"wrr3i3wy62.png\",\"posterFilePath\":\"sssth0c89d.png\"}";
	public static String json_appranklist = "{\"FASTEST\":[{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"posterFilePath\":\"sssth0c89d.png\"}],\"HOTEST\":[{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":128,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":127,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":131,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":135,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":134,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":121,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}],\"NEWEST\":[{\"appId\":141,\"appKey\":\"w13d6kma\",\"appName\":\"西米隐隐\",\"posterFilePath\":\"sssth0c89d.png\"},{\"appId\":140,\"appKey\":\"b3t5nv0d\",\"appName\":\"大话西游\",\"posterFilePath\":\"j9gd3z32ru.jpg\"},{\"appId\":139,\"appKey\":\"vo92sv6v\",\"appName\":\"游戏互动\",\"posterFilePath\":\"xy9sfxgope.png\"},{\"appId\":138,\"appKey\":\"z8d6gvqd\",\"appName\":\"百度音乐\",\"posterFilePath\":\"2muqe5g4wr.png\"},{\"appId\":137,\"appKey\":\"j4pyq3zv\",\"appName\":\"酷狗音乐\",\"posterFilePath\":\"2b0ckiz81r.apk\"},{\"appId\":2,\"appKey\":\"17c0cu1y\",\"appName\":\"网页新闻\",\"posterFilePath\":\"6ec33tpang.apk\"},{\"appId\":1,\"appKey\":\"nefbaxki\",\"appName\":\"大话宿友\",\"posterFilePath\":\"kk73gncxbr.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":123,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":124,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}]}";
	public static String json_appsearch = "{\"host\":\"http://localhost:8081/appmarket/upload/\",\"values\":[{\"appId\":132,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":126,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":120,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":114,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":108,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":102,\"appKey\":\"1234567890\",\"appName\":\"Intel Me\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":122,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":123,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":124,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"},{\"appId\":128,\"appKey\":\"1234567890\",\"appName\":\"NOEHFOE\",\"posterFilePath\":\"1234567890.png\"}]}";

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
			JSONArray array = new JSONArray(categoryJson);
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				Category category = new Category();
				if (object.has("id")) {
					category.setId(object.getInt("id"));
				}
				if (object.has("name")) {
					category.setName(object.getString("name"));
				}
				int parentid = -1;
				if (object.has("parentId")) {
					parentid = object.getInt("parentId");
					category.setParentId(parentid);
				}
				if (parentid == -1) {// 添加为父栏目
					if (category.getName().equals("首页")) {
						categories.add(0, category);// TODO 将首页添加为第一个
					} else {
						categories.add(category);
					}
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
					if (dataCenter.getCategories().get(j).getId() == 16 && app.getPageid() == 1) {
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
				L.d("parse category--" + dataCenter.getCategories().get(i));
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
				if (appobject.has("appId")) {
					app.setAppid(appobject.getInt("appId"));
				}
				if (appobject.has("appKey")) {
					app.setAppkey(appobject.getString("appKey"));
				}
				if (appobject.has("appName")) {
					app.setAppname(appobject.getString("appName"));
				}
				if (appobject.has("posterFilePath")) {
					app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString("posterFilePath"));
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
			appDetail.setIconFilePath(host + appKey + "/" + object.getString("iconFilePath"));
			appDetail.setPosterFilePath(host + appKey + "/" + object.getString("posterFilePath"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return appDetail;
	}
	/**
	 * 解析搜索json数据
	 * @param searchAppsJson
	 * @return
	 */
	public static List<Object> parseSearchApps(String searchAppsJson) {
		List<Object> apps = new ArrayList<Object>();

		if (TextUtils.isEmpty(searchAppsJson)) {
			L.w("returned by categoryAppJson is empty when parseCategoryApp");
			return apps;
		}
		try {
			JSONObject object = new JSONObject(searchAppsJson);
			String host = object.getString("host");
			JSONArray array = object.getJSONArray("values");
			for (int i = 0; i < array.length(); i++) {
				App app = new App();
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
				if (appobject.has("posterFilePath")) {
					app.setPosterFilePath(host + app.getAppkey() + "/" + appobject.getString("posterFilePath"));
				}
				apps.add(app);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return apps;
	}
}
