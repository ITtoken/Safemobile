package com.example.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.dao.DBOperater;
import com.example.utils.MUtils;

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
				new MUtils(context).printToast("来源号码：" + address);
				if (!TextUtils.isEmpty(address)) {
					boolean exsist = operater.queryFromTelnum(address);
					if (exsist) {
						/* 去掉国号+86后的真正的号码 */
						abortBroadcast();
					}
				}
			}
		} else {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Service.TELEPHONY_SERVICE);
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	/* 电话监听类 */
	PhoneStateListener listener = new PhoneStateListener() {
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:/* 响铃的时候 */
				Log.i("NUM", "来电电话：" + incomingNumber);
				break;
			}
		};
	};

}
