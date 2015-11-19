package com.tianjiajia.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BtnResolveReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int viewId = intent.getIntExtra("viewId", -1);
		switch (viewId) {
		case -1:
			return;
		case 1:
			Toast.makeText(context, "����", 0).show();
			break;
		case 0:
			Toast.makeText(context, "��С", 0).show();
			break;
		}

	}

}
