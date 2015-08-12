package com.changhong.gdappstore.model;

/***
 * 应用基础数据模型
 * 
 * @author wangxiufeng
 * 
 */
public class App{
	// 应用id
	protected int appid;
	// 应用key
	protected String appkey="";
	// 应用名称
	protected String appname;
	// 应用海报地址
	protected String posterFilePath;
	// 应用版本号
	protected float version;

	public App() {
		super();
	}

	public App(int appid, String appkey, String appname, String posterFilePath) {
		super();
		this.appid = appid;
		this.appkey = appkey;
		this.appname = appname;
		this.posterFilePath = posterFilePath;
	}

	public App(int appid, String appkey, String appname, String posterFilePath, float version) {
		super();
		this.appid = appid;
		this.appkey = appkey;
		this.appname = appname;
		this.posterFilePath = posterFilePath;
		this.version = version;
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

	public String getPosterFilePath() {
		return posterFilePath;
	}

	public void setPosterFilePath(String posterFilePath) {
		this.posterFilePath = posterFilePath;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "App [appid=" + appid + ", appkey=" + appkey + ", appname=" + appname + ", posterFilePath="
				+ posterFilePath + ", version=" + version + "]";
	}

}