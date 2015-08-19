package com.changhong.gdappstore.model;

import android.graphics.drawable.Drawable;

public class NativeApp {
	/*** 应用id，小于等于0表示服务端没该应用 */
	public int appid = -1;
	/** 应用名称 */
	public String appname;
	/** 服务端配置版本（不一定是apk里面版本号） */
	public String ServerVersion="0";
	/** 本地数据库保存版本（不一定是apk里面版本号） */
	public String nativeVersion="0";
	/** 应用图标 */
	public Drawable appIcon;
	/** 应用包名 */
	public String appPackage;

	public NativeApp() {
	}


	public NativeApp(int appid, String appname, String serverVersion, String nativeVersion, Drawable appIcon,
			String appPackage) {
		super();
		this.appid = appid;
		this.appname = appname;
		ServerVersion = serverVersion;
		this.nativeVersion = nativeVersion;
		this.appIcon = appIcon;
		this.appPackage = appPackage;
	}

	public int getAppid() {
		return appid;
	}

	public void setAppid(int appid) {
		this.appid = appid;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}


	public String getServerVersion() {
		return ServerVersion;
	}


	public void setServerVersion(String serverVersion) {
		ServerVersion = serverVersion;
	}


	public String getNativeVersion() {
		return nativeVersion;
	}


	public void setNativeVersion(String nativeVersion) {
		this.nativeVersion = nativeVersion;
	}


	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage;
	}


	@Override
	public String toString() {
		return "NativeApp [appid=" + appid + ", appname=" + appname + ", ServerVersion=" + ServerVersion
				+ ", nativeVersion=" + nativeVersion + ", appIcon=" + appIcon + ", appPackage=" + appPackage + "]";
	}


}
