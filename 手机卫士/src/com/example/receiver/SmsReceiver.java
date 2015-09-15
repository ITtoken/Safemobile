package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.example.mobilesafe.R;
import com.example.service.LocationService;
import com.example.utils.AppConst;
import com.example.utils.MUtils;

public class SmsReceiver extends BroadcastReceiver {
	SharedPreferences pre;

	@Override
	public void onReceive(Context context, Intent intent) {
		pre = context.getSharedPreferences(AppConst.APPINFO, Context.MODE_PRIVATE);
		
		Bundle bundle = intent.getExtras();
		Object[] object = (Object[]) bundle.get("pdus");
		
		for (Object obj : object) {
			SmsMessage message = SmsMessage.createFromPdu((byte[])obj);
			String num = message.getOriginatingAddress();/*��ȡ������Դ����*/
			String safenum = pre.getString("safenum", null);/*��ȡ�����õİ�ȫ����*/
			
			/*�����ź���Ϊ��ȫ����ʱ,�ն˶���,ִ����Ӧ���ݵĲ���*/
			if(!TextUtils.isEmpty(safenum) && num.equals(safenum)){
				String smsContent = message.getMessageBody();//��ȡ��������
				
				if("#*alarm*#".equals(smsContent)){
					/*���ű�������*/
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(true);/*ѭ������*/
					player.setVolume(1f, 1f);/*����������С(������������)--1Ϊ���*/
					player.start();
					
					abortBroadcast();
				}
				
				if("#*location*#".equals(smsContent)){
					/*������λ����*/
					context.startService(new Intent(context, LocationService.class));
					String location = pre.getString("GPS", null);
					if(!TextUtils.isEmpty(location)){
						new MUtils(context).sendSMS(safenum, location);
					}
					abortBroadcast();
				}
				
				
			}
		}
	}
}
