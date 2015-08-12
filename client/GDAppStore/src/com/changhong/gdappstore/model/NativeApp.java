package com.changhong.gdappstore.model;

import android.graphics.drawable.Drawable;

public class NativeApp {
	// 应用id
	public int appid;
	// 应用名称
	public String appname;
	// 应用版本号
	public String versionName;
	// 应用版本code
	public int versionCode;
	// 应用图标
	public Drawable appIcon;
	// 应用包名
	public String appPackage;

	public NativeApp() {
	}

	public NativeApp(int appid, String appname, String versionName, int versionCode, Drawable appIcon, String appPackage) {
		super();
		this.appid = appid;
		this.appname = appname;
		this.versionName = versionName;
		this.versionCode = versionCode;
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

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
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

}
