package com.example.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	/**
	 * 数据库操作类
	 * 
	 * @param context
	 *            上下文
	 * @param name
	 *            数据库名字
	 * @param version
	 *            数据库版本号信息
	 */
	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* 初始化黑名单表 */
		db.execSQL("create table blacksheet(id integer primary key,telnum varchar(20));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/* 更新表的时候调用 */
	}

}
