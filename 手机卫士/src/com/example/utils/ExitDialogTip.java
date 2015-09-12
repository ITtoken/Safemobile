package com.example.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

public class ExitDialogTip {

	static SharedPreferences pref;

	public static void alertExitTip(final Activity activity) {

		pref = activity.getSharedPreferences("appinfo", Activity.MODE_PRIVATE);

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		AlertDialog dialog = builder.create();
		dialog.setTitle("提示");
		dialog.setMessage("确认退出将不会保存设置的信息，确定退出安全设置吗?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
						/* 当确认退出的时候，删除之前设置的信息 */
						pref.edit().remove("sim").commit();/* SIM卡绑定信息 */
						pref.edit().remove("safenum").commit();/* 安全号码 */
						pref.edit().remove("changenum").commit();/* “改变号码”状态 */
						pref.edit().remove("settingfinish").commit();/* 设置完成状态 */
						pref.edit().remove("protect").commit();/* 防盗保护开启状态 */
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialog.show();
	}

}
