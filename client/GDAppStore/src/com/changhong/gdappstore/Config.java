package com.changhong.gdappstore;

/**
 * 应用配置文件
 * 
 * @author wangxiufeng
 * 
 */
public class Config {
	public static final boolean ISTEST = false;
	
	public static final int REQUEST_RESTTIEM=10*60*60;
	/***************************** 网络请求配置项 ******************************************/
	/** 请求链接超时 */
	public static int CONNECTION_TIMEOUT = 6000;
	/** 服务器基础地址 **/
	public static String BASEURL = "http://192.168.0.103:8081/appmarket/";
	/** 获取分类地址 **/
	public static String getCategoryUrl = BASEURL + "client/appcategories.html";
	/** 获取页面数据地址 **/
	public static String getPagesUrl = BASEURL + "client/boxpages.html";
	/** 获取分类下app地址 **/
	public static String getCategoryAppsUrl = BASEURL + "client/categoryapps.html";
	/** 获取app详情地址 **/
	public static String getAppDetailUrl = BASEURL + "client/appdetails.html";
	/** 获取app排行榜地址 **/
	public static String getAppRankLisUrl = BASEURL + "client/appranklist.html";
	/** 搜索app地址 **/
	public static String getAppSearchUrl = BASEURL + "client/appsearch.html";
	/** 获取app升级信息地址 **/
	public static String getAppVersionsUrl = BASEURL + "client/appversions.html";
	/** app下载成功后提交统计 **/
	public static String putAppDownloadOK = BASEURL + "/client/appdownload.html";

	/**
	 * 
	 * 
	 * 
	 */
	/******************************* Intent传递key配置项 *********************************/
	/** key_appid 应用id **/
	public static final String KEY_APPID = "appid";
	/** 父栏目id **/
	public static final String KEY_PARENT_CATEGORYID = "parentcategoryid";
	/** 当前栏目id **/
	public static final String KEY_CURRENT_CATEGORYID = "currentcategoryid";
}
