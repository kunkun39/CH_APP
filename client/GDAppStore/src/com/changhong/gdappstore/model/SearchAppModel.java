package com.changhong.gdappstore.model;

public class SearchAppModel {

	public String appname;
	public int appicon;
	public float appscore;

	public SearchAppModel() {
	}

	public SearchAppModel(String appname, int appicon, float appscore) {
		super();
		this.appname = appname;
		this.appicon = appicon;
		this.appscore = appscore;
	}


	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public int getAppicon() {
		return appicon;
	}

	public void setAppicon(int appicon) {
		this.appicon = appicon;
	}

	public float getAppscore() {
		return appscore;
	}

	public void setAppscore(int appscore) {
		this.appscore = appscore;
	}

}
