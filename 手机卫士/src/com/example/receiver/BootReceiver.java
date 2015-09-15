package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.example.utils.AppConst;
import com.example.utils.FileInstance;
import com.example.utils.MUtils;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences preferences = FileInstance.getPref(context);
		String sim = preferences.getString("sim", null);/*
														 * ��ȡ֮ǰ�ɵ�SIM�������к�,
														 * ���Ϊnull˵��û�а�SIM��
														 */
		if (!TextUtils.isEmpty(sim)) {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			switch (manager.getSimState()) {/* ��ȡ��ǰ��SIm��״̬ */

			case TelephonyManager.SIM_STATE_ABSENT:/* δ����״̬ */
				new MUtils(context).printToast("SIM��δ����");
				break;

			case TelephonyManager.SIM_STATE_READY:/* ׼������״̬ */
				String currentSimNum = manager.getSimSerialNumber();// ��ȡ���´��ص�sim�������к�
				if (!currentSimNum.equals(sim)) {
					String safenum = preferences.getString("safenum", null);

					/* ��ȫ���뷢��"SIM���仯"�ı������� */
					new MUtils(context).sendSMS(safenum, "SIM���Ѹı�");
				}
				break;
			}
		} else {
			return;
		}

	}

}
