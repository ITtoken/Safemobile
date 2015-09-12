package com.example.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MUtils {
	private Context context;

	public MUtils(Context context) {
		this.context = context;
	}

	/**
	 * ������˾��ʾ
	 * 
	 * @param message
	 */
	public void printToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 
	 * �ڸ�����ȫ�����ʱ����֤��֤��ĵ���
	 * 
	 * @param titleName
	 * @param message
	 * @param v
	 * @param save_safenum
	 */
	public void printAlert(String titleName, String message, final EditText et,
			final TextView save_safenum) {

		final EditText et_validCode = new EditText(context);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titleName);
		dialog.setMessage(message);
		dialog.setView(et_validCode);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
				new android.content.DialogInterface.OnClickListener() {
					private SharedPreferences pref;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						/* �������֤��ͷ��͸���ȫ�������֤��Ϊͬһ��֤�룬�����ܸ��İ�ȫ���� */

						String input_vc = et_validCode.getText().toString();
						pref = context.getSharedPreferences("appinfo",
								Context.MODE_PRIVATE);
						String validCode = pref.getString("validCode", null);
						if (MD5Utils.encodingMD5(input_vc).equals(validCode)) {
							/* ��֤����֤�ɹ� */
							et.setEnabled(true);
							et.setText("");/* �����������Ϊ�� */
							et.setHint("�����밲ȫ����");
							printToast("��֤�ɹ�");
							save_safenum.setText("���氲ȫ����");
							pref.edit().putBoolean("changenum", false).commit();/* ������ȫ����״̬��ΪFALSE */
							pref.edit().remove("validCode").commit();
							pref.edit().remove("safenum").commit();
						} else {
							dialog.dismiss();
							printToast("��֤ʧ��");
						}
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

	/**
	 * ���Ͷ���
	 * 
	 * @param descAddress
	 * @param text
	 */
	public void sendSMS(String descAddress, String text) {
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(descAddress, null, text, null, null);
	}
	
}
