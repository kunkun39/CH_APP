package com.changhong.gdappstore.model;

public class MainPostItemModel {
	/** 应用图标true还是大海报false？ */
	public boolean isapp = false;
	public int imageId;
	public String name;

	public MainPostItemModel() {

	}

	public MainPostItemModel(boolean isapp, int imageId, String name) {
		super();
		this.isapp = isapp;
		this.imageId = imageId;
		this.name = name;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
