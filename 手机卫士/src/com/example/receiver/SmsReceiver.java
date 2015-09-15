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
			String num = message.getOriginatingAddress();/*获取短信来源号码*/
			String safenum = pre.getString("safenum", null);/*获取已设置的安全号码*/
			
			/*当短信号码为安全号码时,终端短信,执行相应内容的操作*/
			if(!TextUtils.isEmpty(safenum) && num.equals(safenum)){
				String smsContent = message.getMessageBody();//获取短信内容
				
				if("#*alarm*#".equals(smsContent)){
					/*播放报警铃声*/
					MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
					player.setLooping(true);/*循环播放*/
					player.setVolume(1f, 1f);/*设置音量大小(左右声道控制)--1为最大*/
					player.start();
					
					abortBroadcast();
				}
				
				if("#*location*#".equals(smsContent)){
					/*启动定位服务*/
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
