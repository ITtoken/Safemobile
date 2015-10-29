package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.example.dao.DBOperater;

public class BlackContactsReceiver extends BroadcastReceiver {
	private DBOperater operater;

	@Override
	public void onReceive(Context context, Intent intent) {
		operater = new DBOperater(context);
		/* ���ն��� */
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();
			Object[] objs = (Object[]) bundle.get("pdus");
			for (Object obj : objs) {
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) obj);
				String address = sm.getOriginatingAddress();/* ��ȡ�绰���� */
				if (address.startsWith("+86")) {
					/* ��+86��ͷ�ĵ绰 */
					address = address.substring(address.indexOf("6") + 1);
				}
				if (!TextUtils.isEmpty(address)) {
					boolean exsist = operater.queryFromTelnum(address);
					if (exsist) {
						/* ȥ������+86��������ĺ��� */
						abortBroadcast();
					}
				}
			}
		}
	}

}
