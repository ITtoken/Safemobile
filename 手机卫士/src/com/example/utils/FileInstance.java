package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FileInstance {
	private static FileInstance fi = new FileInstance();
	private static SharedPreferences pref;

	private FileInstance() {
	}

	/**
	 * ��ȡһ������Ӧ����Ϣ�ļ���sharePreferences����
	 * 
	 * @param context
	 * @return
	 */
	public static SharedPreferences getPref(Context context) {
		pref = context.getSharedPreferences(AppConst.APPINFO,
				Context.MODE_PRIVATE);
		return pref;
	}

}
