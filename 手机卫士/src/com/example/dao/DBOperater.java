package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBOperater {

	private DBHelper dh;

	/* 初始化数据库和表 */
	public DBOperater(Context context) {
		dh = new DBHelper(context, "blackcontacts.db", 1);
	}

	/**
	 * 将信息插入数据库
	 * 
	 * @param telnum
	 *            telnum列的值
	 */
	public void insert(String telnum) {
		SQLiteDatabase db = dh.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("telnum", telnum);
		db.insert("blacksheet", null, values);
		db.close();
	}

	/**
	 * 删除指定id的信息
	 * 
	 * @param id
	 *            要删除的信息的id
	 */
	public void delete(int id) {
		SQLiteDatabase db = dh.getWritableDatabase();
		String strid = String.valueOf(id);
		db.delete("blacksheet", "id=?", new String[] { strid });

		db.close();
	}

	/**
	 * 更新id的值（在删除一条数据后，将id的值重新从1开始递增） ：：由于表初始值是从1开始递增，座椅这里也从1开始递增
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
	 * 查询数据库
	 * 
	 * @return 数据库中telnum信息封装后的list
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
