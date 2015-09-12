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
		dialog.setTitle("��ʾ");
		dialog.setMessage("ȷ���˳������ᱣ�����õ���Ϣ��ȷ���˳���ȫ������?");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						activity.finish();
						/* ��ȷ���˳���ʱ��ɾ��֮ǰ���õ���Ϣ */
						pref.edit().remove("sim").commit();/* SIM������Ϣ */
						pref.edit().remove("safenum").commit();/* ��ȫ���� */
						pref.edit().remove("changenum").commit();/* ���ı���롱״̬ */
						pref.edit().remove("settingfinish").commit();/* �������״̬ */
						pref.edit().remove("protect").commit();/* ������������״̬ */
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialog.show();
	}

}
