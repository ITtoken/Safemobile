package com.tianjiajia.service;

import com.example.brightnesscontrolor.R;
import com.tianjiajia.main.ScreenBrightnessWidget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

public class BrightnessService extends Service {

	private RemoteViews rv;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);

		rv = new RemoteViews(getPackageName(), R.layout.msafe_appwidget);
		ComponentName provider = new ComponentName(getApplicationContext(),
				ScreenBrightnessWidget.class);
		clickListener(R.id.btn_plus);
		clickListener(R.id.btn_sub);
		widgetManager.updateAppWidget(provider, rv);
	}

	
	private void clickListener(int viewId) {
		Intent intent = new Intent();
		intent.setAction("com.tjj.btnresolver");
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),
				0, intent, 0);

		if (viewId == R.id.btn_plus) {
			intent.putExtra("viewId", 1);
			sendBroadcast(intent);
		}
		if (viewId == R.id.btn_sub) {
			intent.putExtra("viewId", 0);
			sendBroadcast(intent);
		}

		rv.setOnClickPendingIntent(viewId, pi);
	}

}
