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
	// 应用海报地址(也有可能是图标，具体根据服务器的传值)
	protected String posterFilePath;
	// 应用版本号
	protected String version;
	// 应用包名
	protected String packageName;

	public App() {
		super();
	}

	public App(int appid, String appkey, String appname, String apkSize, String download, String posterFilePath,
			String version, String packageName) {
		super();
		this.appid = appid;
		this.appkey = appkey;
		this.appname = appname;
		this.apkSize = apkSize;
		this.download = download;
		this.posterFilePath = posterFilePath;
		this.version = version;
		this.packageName = packageName;
	}

	@Override
	public String toString() {
		return "App [appid=" + appid + ", appkey=" + appkey + ", appname=" + appname + ", apkSize=" + apkSize
				+ ", download=" + download + ", posterFilePath=" + posterFilePath + ", version=" + version
				+ ", packageName=" + packageName + "]";
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

	public String getPosterFilePath() {
		return posterFilePath;
	}

	public void setPosterFilePath(String posterFilePath) {
		this.posterFilePath = posterFilePath;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
