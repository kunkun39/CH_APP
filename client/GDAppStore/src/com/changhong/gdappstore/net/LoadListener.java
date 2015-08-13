package com.changhong.gdappstore.net;

import java.util.List;

import com.changhong.gdappstore.model.App;

/**
 * 请求解析完成监听器
 * 
 * @author wangxiufeng
 * 
 */
public class LoadListener {
	/**
	 * 加载完成监听器
	 * 
	 * @author wangxiufeng
	 * 
	 */
	public interface LoadCompleteListener {
		public void onComplete();
	}

	/**
	 * 加载app列表完成监听器
	 * 
	 * @author wangxiufeng
	 * 
	 */
	public interface LoadListListener {
		public void onComplete(List<Object> items);
	}
}
