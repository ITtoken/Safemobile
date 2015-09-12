package com.example.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

	private SharedPreferences pref;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		pref = getSharedPreferences("appinfo", MODE_PRIVATE);
		/* GPS追踪 */
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);/* 是否循序链接网络 */
		criteria.setAccuracy(Criteria.ACCURACY_FINE);/*设置精确度(FINE表示适中)*/
		String provider = lm.getBestProvider(criteria, false);/*
															 * 获取最优为位置提供者,根据状态改变选择
															 */
		lm.requestLocationUpdates(provider, 60, 50, new MyGpsLocation());
		
	}
	
	/**
	 * GPS定位监听类
	 */
	class MyGpsLocation implements LocationListener {
		/* 位置改变时调用 */
		@Override
		public void onLocationChanged(Location location) {

			String longitude = ""+location.getLongitude();/* 获取经度 */
			String latitude = ""+location.getLatitude();/* 获取纬度 */
			pref.edit().putString("GPS", "经度:"+latitude+" 纬度:"+longitude).commit();/*保存经纬度*/
			stopSelf();/*停止服务*/
		}
		/* GPS定位服务状态改变时调用（打开或者关闭） */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		/* 位置提供者可用时调用 */
		@Override
		public void onProviderEnabled(String provider) {
		}
		/* 位置提供者不可用时调用 */
		@Override
		public void onProviderDisabled(String provider) {
		}

	}
}
