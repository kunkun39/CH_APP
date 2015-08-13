package com.changhong.gdappstore;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 应用Application
 * 
 * @author wangxiufeng
 * 
 */
public class MyApplication extends Application {

	public static ImageLoader imageLoader;// 图片加载器

	public static DiscCacheAware discCacheAware;// 图片加载器缓存目录
	/** 图片加载：内存缓存+磁盘缓存 */
	public static DisplayImageOptions option_memory_disc;
	/** 图片加载：磁盘缓存，内存不缓存 */
	public static DisplayImageOptions option_nomemory_disc;
	/** 图片加载：磁盘内存都不缓存 */
	public static DisplayImageOptions option_nomemory_nodisc;// 图片加载器设置（含缓存）

	private static Context context;
	/** 磁盘缓存大小 **/
	private static int discCacheSize = 10 * 1024 * 1024;

	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoaderCacheDir();
		initImageLoader(this);
	}

	/**
	 * 初始化图片加载插件
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		int lruMemCachSize = 10 * 1024 * 1024;
		int threadPoolSize = 7;
		Bitmap.Config bmConfig = Bitmap.Config.ARGB_4444;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).discCacheSize(discCacheSize)
				.tasksProcessingOrder(QueueProcessingType.FIFO).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(lruMemCachSize)).threadPoolSize(threadPoolSize)
				.threadPriority(Thread.NORM_PRIORITY).discCache(discCacheAware).build();
		ImageLoader.getInstance().init(config);
		imageLoader=ImageLoader.getInstance();

		option_memory_disc = new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer())
				.bitmapConfig(bmConfig).imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory(true)
				.showImageForEmptyUri(R.drawable.img_loadfail).showImageOnFail(R.drawable.img_loadfail)
				.cacheOnDisc(true).build();
		option_nomemory_disc = new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer())
				.bitmapConfig(bmConfig).imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory(false)
				.showImageForEmptyUri(R.drawable.img_loadfail).showImageOnFail(R.drawable.img_loadfail)
				.cacheOnDisc(true).build();
		option_nomemory_nodisc = new DisplayImageOptions.Builder().displayer(new SimpleBitmapDisplayer())
				.bitmapConfig(bmConfig).imageScaleType(ImageScaleType.IN_SAMPLE_INT).cacheInMemory(false)
				.showImageForEmptyUri(R.drawable.img_loadfail).showImageOnFail(R.drawable.img_loadfail)
				.cacheOnDisc(false).build();
	}

	/**
	 * 初始化图片缓存目录
	 */
	private void initImageLoaderCacheDir() {
//		String imgCacheDirStr = context.getCacheDir() + File.separator + "picCache" + File.separator;
//		File cacheDir = new File(imgCacheDirStr);// 图片缓存目录文件
		File cacheDir =StorageUtils.getCacheDirectory(this);// 图片缓存目录文件
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		discCacheAware = new FileCountLimitedDiscCache(cacheDir, discCacheSize);
	}

}
