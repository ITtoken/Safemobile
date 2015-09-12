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
		/* GPS׷�� */
		LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setCostAllowed(true);/* �Ƿ�ѭ���������� */
		criteria.setAccuracy(Criteria.ACCURACY_FINE);/*���þ�ȷ��(FINE��ʾ����)*/
		String provider = lm.getBestProvider(criteria, false);/*
															 * ��ȡ����Ϊλ���ṩ��,����״̬�ı�ѡ��
															 */
		lm.requestLocationUpdates(provider, 60, 50, new MyGpsLocation());
		
	}
	
	/**
	 * GPS��λ������
	 */
	class MyGpsLocation implements LocationListener {
		/* λ�øı�ʱ���� */
		@Override
		public void onLocationChanged(Location location) {

			String longitude = ""+location.getLongitude();/* ��ȡ���� */
			String latitude = ""+location.getLatitude();/* ��ȡγ�� */
			pref.edit().putString("GPS", "����:"+latitude+" γ��:"+longitude).commit();/*���澭γ��*/
			stopSelf();/*ֹͣ����*/
		}
		/* GPS��λ����״̬�ı�ʱ���ã��򿪻��߹رգ� */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
		/* λ���ṩ�߿���ʱ���� */
		@Override
		public void onProviderEnabled(String provider) {
		}
		/* λ���ṩ�߲�����ʱ���� */
		@Override
		public void onProviderDisabled(String provider) {
		}

	}
}
