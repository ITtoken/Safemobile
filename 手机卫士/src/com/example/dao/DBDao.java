package com.example.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class DBDao {

	public static void gteContacts(Context context) {

		Uri raw_contectUri = Uri
				.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = context.getContentResolver().query(raw_contectUri,
				new String[] { "contact_id" }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String conteact_id = cursor.getString(0);
				// 根据id从data表中查询内容
				Cursor content = context.getContentResolver().query(dataUri,
						new String[] { "mimetype", "data1" }, "contact_id=?",
						new String[] { conteact_id }, null);

				if (content != null && content.getCount() > 0) {
					while (content.moveToNext()) {
						String mimetype = content.getString(0);
						String data = cursor.getString(1);
						System.out.println(conteact_id + " : " + data + " : "
								+ mimetype);
					}
				}
			}
		}
	}
}
