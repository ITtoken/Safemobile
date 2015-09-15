package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class FileInstance {
	private static FileInstance fi = new FileInstance();
	private static SharedPreferences pref;

	private FileInstance() {
	}

	/**
	 * 获取一个返回应用信息文件的sharePreferences对象；
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
