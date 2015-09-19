package com.example.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	/**
	 * ���ݿ������
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            ���ݿ�����
	 * @param version
	 *            ���ݿ�汾����Ϣ
	 */
	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/* ��ʼ���������� */
		db.execSQL("create table blacksheet(id integer primary key,telnum varchar(20));");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/* ���±��ʱ����� */
	}

}
