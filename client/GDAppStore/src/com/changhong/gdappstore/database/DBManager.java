package com.changhong.gdappstore.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.changhong.gdappstore.model.App;
import com.changhong.gdappstore.util.L;

/**
 * 数据库管理类
 * 
 * @author wangxiufeng
 * 
 */
public class DBManager {

	private static DBManager dbManager = null;

	private DatabaseHelper helper;
	private SQLiteDatabase db;

	private static Context context;

	public static DBManager getInstance(Context context2) {
		context = context2;
		if (dbManager == null) {
			dbManager = new DBManager(context);
		}

		return dbManager;
	}

	private DBManager(Context context) {
		helper = DatabaseHelper.getInstance(context);
		db = helper.getWritableDatabase();
	}

	/**
	 * 插入app版本信息数据
	 * 
	 * @param app
	 * @return 插入的id，-1表示异常
	 */
	public long insertAppVersions(App app) {
		if (app == null || app.getAppid() < 0) {
			L.e("DBManager insertAppVersions app is null or id<0 ");
			return -1;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(helper.CLUM_APPID, app.getAppid());
		contentValues.put(helper.CLUM_PCKNAME, app.getPackageName());
		contentValues.put(helper.CLUM_VERSIONNAME, app.getVersion());
		contentValues.put(helper.CLUM_VERSIONCODE, app.getVersionInt());
		return db.insert(helper.APPVERSION_TABLE, "", contentValues);
		// db.beginTransaction(); // 开始事务
		// db.execSQL("INSERT INTO person VALUES(null, ?, ?, ?)", new Object[] {
		// person.name, person.age,person.info });
		// db.setTransactionSuccessful(); // 设置事务成功完成
		// db.endTransaction(); // 结束事务
	}

	/**
	 * 更新app数据库信息
	 * 
	 * @param app
	 * @return
	 */
	public int updateAppVersions(App app) {
		if (app == null || app.getAppid() < 0) {
			L.e("DBManager updateAppVersions app is null or id<0 ");
			return -1;
		}
		ContentValues contentValues = new ContentValues();
		contentValues.put(helper.CLUM_APPID, app.getAppid());
		contentValues.put(helper.CLUM_PCKNAME, app.getPackageName());
		contentValues.put(helper.CLUM_VERSIONNAME, app.getVersion());
		contentValues.put(helper.CLUM_VERSIONCODE, app.getVersionInt());
																		
		return db.update(helper.APPVERSION_TABLE, contentValues, helper.CLUM_APPID + " = ?",
				new String[] { app.getAppid() + "" });
	}
	/**
	 * 插入或者更新数据，如果有数据就更新，没有就插入
	 * @param app
	 */
	public void  insertOrUpdateVersions(App app) {
		if (app==null ||app.getAppid()<0) {
			L.d("insertOrUpdateVersions returned by app=null or appid<0");
			return;
		}
		App navApp=queryAppVersionById(app.getAppid());
		if (navApp==null) {
			long id=insertAppVersions(app);
			L.d("insertOrUpdateVersions insert id=="+id);
		}else {
			int num=updateAppVersions(app);
			L.d("insertOrUpdateVersions update num=="+num);
		}
	}

	/**
	 * 删除app版本信息
	 * 
	 * @param appid
	 */
	public void deleteAppVersions(int appid) {
		db.delete(helper.APPVERSION_TABLE, helper.CLUM_APPID + "= ?", new String[] { appid + "" });
	}

	/**
	 * 查询某个app信息
	 * 
	 * @param appid
	 * @return
	 */
	public App queryAppVersionById(int appid) {
		App app = null;
		Cursor c = db.rawQuery("SELECT * FROM " + helper.APPVERSION_TABLE + " where " + helper.CLUM_APPID + " =?",
				new String[] { appid + "" });
		if (c == null || c.getCount() == 0) {
			return app;
		}
		while (c.moveToNext()) {
			if (app == null) {
				app = new App();
				app.setAppid(c.getInt(c.getColumnIndex(helper.CLUM_APPID)));
				app.setPackageName(c.getString(c.getColumnIndex(helper.CLUM_PCKNAME)));
				app.setVersion(c.getString(c.getColumnIndex(helper.CLUM_VERSIONNAME)));
				app.setVersionInt(c.getInt(c.getColumnIndex(helper.CLUM_VERSIONCODE)));
			} else {
				// 删除多余列
				deleteAppVersions(c.getInt(c.getColumnIndex(helper.CLUM_APPID)));
			}
		}
		c.close();
		return app;
	}

	/**
	 * 查询某个app信息
	 * 
	 * @param appid
	 * @return
	 */
	public App queryAppVersionByPackagename(String packagename) {
		App app = null;
		Cursor c = db.rawQuery("SELECT * FROM " + helper.APPVERSION_TABLE + " where " + helper.CLUM_PCKNAME + " =?",
				new String[] { packagename });
		if (c == null || c.getCount() == 0) {
			return app;
		}
		while (c.moveToNext()) {
			if (app == null) {
				app = new App();
				app.setAppid(c.getInt(c.getColumnIndex(helper.CLUM_APPID)));
				app.setPackageName(c.getString(c.getColumnIndex(helper.CLUM_PCKNAME)));
				app.setVersion(c.getString(c.getColumnIndex(helper.CLUM_VERSIONNAME)));
				app.setVersionInt(c.getInt(c.getColumnIndex(helper.CLUM_VERSIONCODE)));
			} else {
				// 删除多余列
				deleteAppVersions(c.getInt(c.getColumnIndex(helper.CLUM_APPID)));
			}
		}
		c.close();
		return app;
	}

	/**
	 * 查询所有保存的app版本信息
	 * 
	 * @return 注：返回的app只有数据库中保存的字段
	 */
	public List<App> queryAppVersions() {
		List<App> apps = new ArrayList<App>();
		Cursor c = db.rawQuery("SELECT * FROM " + helper.APPVERSION_TABLE, null);
		if (c == null || c.getCount() == 0) {
			return apps;
		}
		while (c.moveToNext()) {
			App app = new App();
			app.setAppid(c.getInt(c.getColumnIndex(helper.CLUM_APPID)));
			app.setPackageName(c.getString(c.getColumnIndex(helper.CLUM_PCKNAME)));
			app.setVersion(c.getString(c.getColumnIndex(helper.CLUM_VERSIONNAME)));
			app.setVersionInt(c.getInt(c.getColumnIndex(helper.CLUM_VERSIONCODE)));
			apps.add(app);
		}
		c.close();
		return apps;
	}
}
