package com.changhong.gdappstore.model;

/***
 * 应用基础数据模型
 * 
 * @author wangxiufeng
 * 
 */
public class App {
	// 应用id
	protected int appid;
	// 应用key
	protected String appkey = "";
	// 应用名称
	protected String appname;
	// 应用大小
	protected String apkSize;
	// 应用下载量
	protected String download;
	// 应用海报地址
	protected String posterFilePath;
	// 应用图标下载地址
	protected String iconFilePath;
	// 应用版本号,int类型，如versionCode
	protected int versionInt;
	// 应用版本号如versionName
	protected String version;
	// 应用包名
	protected String packageName;
	// 应用评分
	protected int scores;

	public App() {
		super();
	}

	public App(int appid, String appkey, String appname, String apkSize, String download, String posterFilePath,
			String iconFilePath, int versionInt, String version, String packageName) {
		super();
		this.appid = appid;
		this.appkey = appkey;
		this.appname = appname;
		this.apkSize = apkSize;
		this.download = download;
		this.posterFilePath = posterFilePath;
		this.iconFilePath = iconFilePath;
		this.versionInt = versionInt;
		this.version = version;
		this.packageName = packageName;
	}



	@Override
	public String toString() {
		return "App [appid=" + appid + ", appkey=" + appkey + ", appname=" + appname + ", apkSize=" + apkSize
				+ ", download=" + download + ", posterFilePath=" + posterFilePath + ", iconFilePath=" + iconFilePath
				+ ", versionInt=" + versionInt + ", version=" + version + ", packageName=" + packageName + "]";
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getApkSize() {
		return apkSize;
	}

	public void setApkSize(String apkSize) {
		this.apkSize = apkSize;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public int getVersionInt() {
		return versionInt;
	}

	public void setVersionInt(int versionInt) {
		this.versionInt = versionInt;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPosterFilePath() {
		return posterFilePath;
	}

	public void setPosterFilePath(String posterFilePath) {
		this.posterFilePath = posterFilePath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getIconFilePath() {
		return iconFilePath;
	}

	public void setIconFilePath(String iconFilePath) {
		this.iconFilePath = iconFilePath;
	}

	public int getScores() {
		return scores;
	}

	public void setScores(int scores) {
		this.scores = scores;
	}

	
}
