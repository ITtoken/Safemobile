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
		/* 接收短信 */
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras();
			Object[] objs = (Object[]) bundle.get("pdus");
			for (Object obj : objs) {
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) obj);
				String address = sm.getOriginatingAddress();/* 获取电话号码 */
				if (address.startsWith("+86")) {
					/* 以+86开头的电话 */
					address = address.substring(address.indexOf("6") + 1);
				}
				if (!TextUtils.isEmpty(address)) {
					boolean exsist = operater.queryFromTelnum(address);
					if (exsist) {
						/* 去掉国号+86后的真正的号码 */
						abortBroadcast();
					}
				}
			}
		}
	}

}
