package com.changhong.gdappstore.util;

import com.changhong.gdappstore.MyApplication;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.widget.ImageView;

/**
 * imageLoader加载图片工具类
 * 
 * @author wangxiufeng
 * 
 */
public class ImageLoadUtil {
	/**
	 * 加载图片 磁盘缓存和内存缓存
	 * 
	 * @param url
	 *            图片下载地址
	 * @param imageView
	 *            图片imageView
	 */
	public static void loadImgByMemoryDiscCache(String url, ImageView imageView) {
		MyApplication.imageLoader.loadImage(url, MyApplication.option_memory_disc, null);
	}

	/**
	 * 加载图片 磁盘缓存和内存缓存
	 * 
	 * @param url
	 *            图片下载地址
	 * @param imageView
	 *            图片imageView
	 * @param listener
	 *            下载监听器
	 */
	public static void loadImgByMemoryDiscCache(String url, ImageView imageView, ImageLoadingListener listener) {
		MyApplication.imageLoader.loadImage(url, MyApplication.option_memory_disc, listener);
	}

	/**
	 * 加载图片 磁盘缓存
	 * 
	 * @param url
	 *            图片下载地址
	 * @param imageView
	 *            图片imageView
	 */
	public static void loadImgByonlyDiscCache(String url, ImageView imageView) {
		MyApplication.imageLoader.loadImage(url, MyApplication.option_nomemory_disc, null);
	}

	/**
	 * 加载图片 磁盘缓存
	 * 
	 * @param url
	 *            图片下载地址
	 * @param imageView
	 *            图片imageView
	 * @param listener
	 *            下载监听器
	 */
	public static void loadImgByonlyDiscCache(String url, ImageView imageView, ImageLoadingListener listener) {
		MyApplication.imageLoader.loadImage(url, MyApplication.option_nomemory_disc, listener);
	}

	/**
	 * 加载图片 磁盘缓存
	 * 
	 * @param url
	 *            图片下载地址
	 * @param imageView
	 *            图片imageView
	 */
	public static void loadImgByNoCache(String url, ImageView imageView) {
		MyApplication.imageLoader.loadImage(url, MyApplication.option_nomemory_nodisc, null);
	}

	/**
	 * 加载图片 磁盘缓存
	 * 
	 * @param url
	 *            图片下载地址
	 * @param imageView
	 *            图片imageView
	 * @param listener
	 *            下载监听器
	 */
	public static void loadImgByNoCache(String url, ImageView imageView, ImageLoadingListener listener) {
		MyApplication.imageLoader.loadImage(url, MyApplication.option_nomemory_nodisc, listener);
	}

}
