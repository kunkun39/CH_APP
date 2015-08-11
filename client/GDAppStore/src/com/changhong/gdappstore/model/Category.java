package com.changhong.gdappstore.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 栏目数据类型
 * 
 * @author wangxiufeng
 * 
 */
public class Category {
	// 栏目id
	protected int id;
	// 栏目父栏目id
	protected int parentId = -1;
	// 栏目名字
	protected String name;
	// 栏目图片地址
	protected String iconFilePath;
	// 栏目子栏目
	protected List<Category> categoyChildren = new ArrayList<Category>();
	// 栏目应用
	protected List<PageApp> categoryPageApps = new ArrayList<PageApp>();

	public Category() {
		super();
	}

	public Category(int id, int parentId, String name) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	public Category(int id, int parentId, String name, String iconFilePath) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.iconFilePath = iconFilePath;
	}

	public Category(int id, int parentId, String name, String iconFilePath, List<Category> categoyChildren,
			List<PageApp> categoryPageApps) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.iconFilePath = iconFilePath;
		this.categoyChildren = categoyChildren;
		this.categoryPageApps = categoryPageApps;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconFilePath() {
		return iconFilePath;
	}

	public void setIconFilePath(String iconFilePath) {
		this.iconFilePath = iconFilePath;
	}

	public List<Category> getCategoyChildren() {
		return categoyChildren;
	}

	public void setCategoyChildren(List<Category> categoyChildren) {
		this.categoyChildren = categoyChildren;
	}

	public List<PageApp> getCategoryPageApps() {
		return categoryPageApps;
	}

	public void setCategoryPageApps(List<PageApp> categoryPageApps) {
		this.categoryPageApps = categoryPageApps;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", parentId=" + parentId + ", name=" + name + ", iconFilePath=" + iconFilePath
				+ ", categoyChildren=" + categoyChildren + ", categoryPageApps=" + categoryPageApps + "]";
	}
	
}
