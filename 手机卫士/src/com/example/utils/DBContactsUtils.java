package com.example.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class DBContactsUtils {

	public static List<String> gteContacts(Context context) {
		List<String> list = new ArrayList<String>();// �������ظ�,����ʹ��

		Uri raw_contectUri = Uri
				.parse("content://com.android.contacts/raw_contacts");

		Uri dataUri = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = context.getContentResolver().query(raw_contectUri,
				new String[] { "contact_id" }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				String conteact_id = cursor.getString(0);
				Log.i("NUM", conteact_id);

				// ����id��data���в�ѯ����
				Cursor content = context.getContentResolver().query(dataUri,
						new String[] { "mimetype", "data1" }, "contact_id=?",
						new String[] { conteact_id }, null);

				if (content != null && content.getCount() > 0) {
					while (content.moveToNext()) {
						String pNum = null;
						String name = null;
						String mimetype = content.getString(0);
						if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
							pNum = "�绰����";
						} else {
							name = "��ϵ������";
						}
						list.add(name + ":" + pNum);
						// String data = cursor.getString(1);
						// Log.i("NUM", conteact_id + " : " + /* data + */" : "
						//	+ mimetype);
					}
				}

			}
		}
		return list;
	}

}
