package com.example.splash;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WelcomView extends Activity {

	private TextView tv_welcome;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv_welcome = (TextView) findViewById(R.id.tv_welcome);
		tv_welcome.setText("°æ±¾ºÅ£º" + getVersionName());
	}

	public String getVersionName() {
		try {
			PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			String vn = info.versionName;
			return vn;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

}
