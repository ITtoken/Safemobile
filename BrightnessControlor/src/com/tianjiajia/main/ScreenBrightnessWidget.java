package com.tianjiajia.main;

import com.tianjiajia.service.BrightnessService;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

public class ScreenBrightnessWidget extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		context.startService(new Intent(context, BrightnessService.class));

	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		context.stopService(new Intent(context, BrightnessService.class));
	}
}
