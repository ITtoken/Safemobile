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
														 * 获取之前旧的SIM卡的序列号,
														 * 如果为null说明没有绑定SIM卡
														 */
		if (!TextUtils.isEmpty(sim)) {
			TelephonyManager manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			switch (manager.getSimState()) {/* 获取当前的SIm卡状态 */

			case TelephonyManager.SIM_STATE_ABSENT:/* 未插入状态 */
				new MUtils(context).printToast("SIM卡未插入");
				break;

			case TelephonyManager.SIM_STATE_READY:/* 准备就绪状态 */
				String currentSimNum = manager.getSimSerialNumber();// 获取重新搭载的sim卡的序列号
				if (!currentSimNum.equals(sim)) {
					String safenum = preferences.getString("safenum", null);

					/* 向安全号码发送"SIM卡变化"的报警短信 */
					new MUtils(context).sendSMS(safenum, "SIM卡已改变");
				}
				break;
			}
		} else {
			return;
		}

	}

}
