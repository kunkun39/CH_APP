package com.changhong.gdappstore.post;

public class PostModel {

	private int id;
	private int appicon;
	private String name;
	private float appscore;
	private String apptext;

	

	public PostModel(int id, int appicon, String name, float appscore, String apptext) {
		super();
		this.id = id;
		this.appicon = appicon;
		this.name = name;
		this.appscore = appscore;
		this.apptext = apptext;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppicon() {
		return appicon;
	}

	public void setAppicon(int appicon) {
		this.appicon = appicon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getAppscore() {
		return appscore;
	}

	public void setAppscore(float appscore) {
		this.appscore = appscore;
	}

	public String getApptext() {
		return apptext;
	}

	public void setApptext(String apptext) {
		this.apptext = apptext;
	}

}
