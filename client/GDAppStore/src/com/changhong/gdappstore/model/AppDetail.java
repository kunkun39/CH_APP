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
	// 应用大小
	protected String apkSize;
	// 应用描述
	protected String description;
	// 应用下载量
	protected String download;
	// 应用路径
	protected String host;

	public AppDetail() {
		super();
	}

	public AppDetail(int appid, String appkey, String appname, String posterFilePath, float version,
			String iconFilePath, String apkFilePath, String apkSize, String description, String download, String host) {
		super();
		this.appid = appid;
		this.appkey = appkey;
		this.appname = appname;
		this.posterFilePath = posterFilePath;
		this.version = version;
		this.iconFilePath = iconFilePath;
		this.apkFilePath = apkFilePath;
		this.apkSize = apkSize;
		this.description = description;
		this.download = download;
		this.host = host;
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

	@Override
	public String toString() {
		;
		return "AppDetail ["+super.toString()+"iconFilePath=" + iconFilePath + ", apkFilePath=" + apkFilePath + ", apkSize=" + apkSize
				+ ", description=" + description + ", download=" + download + ", host=" + host + "]";
	}
	
	

}
