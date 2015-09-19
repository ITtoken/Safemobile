package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBOperater {

	private DBHelper dh;

	/* ��ʼ�����ݿ�ͱ� */
	public DBOperater(Context context) {
		dh = new DBHelper(context, "blackcontacts.db", 1);
	}

	/**
	 * ����Ϣ�������ݿ�
	 * 
	 * @param telnum
	 *            telnum�е�ֵ
	 */
	public void insert(String telnum) {
		SQLiteDatabase db = dh.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("telnum", telnum);
		db.insert("blacksheet", null, values);
		db.close();
	}

	/**
	 * ɾ��ָ��id����Ϣ
	 * 
	 * @param id
	 *            Ҫɾ������Ϣ��id
	 */
	public void delete(int id) {
		SQLiteDatabase db = dh.getWritableDatabase();
		String strid = String.valueOf(id);
		db.delete("blacksheet", "id=?", new String[] { strid });

		db.close();
	}

	/**
	 * ����id��ֵ����ɾ��һ�����ݺ󣬽�id��ֵ���´�1��ʼ������ �������ڱ��ʼֵ�Ǵ�1��ʼ��������������Ҳ��1��ʼ����
	 * 
	 * @param list
	 */
	public void update(List<String> list) {
		SQLiteDatabase db = dh.getWritableDatabase();
		ContentValues values = new ContentValues();
		if (list != null && list.size() > 0) {
			for (int i = 1; i <= list.size(); i++) {
				values.put("id", i);
				db.update("blacksheet", values, "telnum=?",
						new String[] { list.get(i - 1) });
			}
		}
		db.close();
	}

	/**
	 * ��ѯ���ݿ�
	 * 
	 * @return ���ݿ���telnum��Ϣ��װ���list
	 */
	public List<String> query() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = dh.getWritableDatabase();
		Cursor cursor = db.query("blacksheet", new String[] { "telnum" }, null,
				null, null, null, null);
		if (cursor != null && cursor.getColumnCount() > 0) {
			while (cursor.moveToNext()) {
				String telnum = cursor.getString(0);
				list.add(telnum);
			}
			db.close();
			return list;
		} else {
			return null;
		}
	}
}
