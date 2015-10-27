package com.changhong.gdappstore.model;

import android.graphics.drawable.Drawable;

/**
 * 云同步使用对象
 * 
 * @author wangxiufeng
 * 
 */
public class SynchApp extends App {
	/** 批量操作时候是否已经选中 */
	private boolean isChecked = false;
	/** 是否已经备份 */
	private Type synchType = Type.NORMAL;
	/** 应用图标,有本地图标时候使用 */
	private Drawable appIcon;
	
	public enum Type {
		NORMAL, BACKUPED, RECOVERED;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Type getSynchType() {
		return synchType;
	}

	public void setSynchType(Type synchType) {
		this.synchType = synchType;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}

	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}

	
}
