package com.changhong.gdappstore.model;

/**
 * 应用详情
 * 
 * @author wangxiufeng
 * 
 */
public class AppDetail extends App {
	// 应用图标下载地址
	protected String iconFilePath;
	// 应用apk下载地址
	protected String apkFilePath;
	// 应用描述
	protected String description;
	// 应用路径
	protected String host;
	// 分类id
	protected int categoryId;
	// 应用路径
	protected String updateDate;

	public AppDetail() {
		super();
	}

	public String getIconFilePath() {
		return iconFilePath;
	}

	public void setIconFilePath(String iconFilePath) {
		this.iconFilePath = iconFilePath;
	}

	public String getApkFilePath() {
		return apkFilePath;
	}

	public void setApkFilePath(String apkFilePath) {
		this.apkFilePath = apkFilePath;
	}

	public String getApkSize() {
		return apkSize;
	}

	public void setApkSize(String apkSize) {
		this.apkSize = apkSize;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	@Override
	public String toString() {
		return "AppDetail [" + super.toString() + "iconFilePath=" + iconFilePath + ", apkFilePath=" + apkFilePath
				+ ", description=" + description + ", host=" + host + ", categoryId=" + categoryId + ", updateDate="
				+ updateDate + "]";
	}

}
