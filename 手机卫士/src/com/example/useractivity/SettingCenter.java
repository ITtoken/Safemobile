package com.example.useractivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.splash.SettingRelativeLayout;
import com.example.utils.AppConst;
import com.example.utils.FileInstance;

public class SettingCenter extends Activity implements OnClickListener {
	private SettingRelativeLayout sr;
	private CheckBox cb;
	private TextView desc;
	private SharedPreferences pref;
	private SettingRelativeLayout location_show;
	private CheckBox cb_location;
	private TextView desc_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingcenter);

		pref = FileInstance.getPref(this);
		boolean stat = pref.getBoolean("stat", true);
		sr = (SettingRelativeLayout) findViewById(R.id.sr);
		cb = (CheckBox) sr.findViewById(R.id.cb);
		desc = (TextView) sr.findViewById(R.id.desc);
		sr.setDefaultStat(stat);
		sr.setOnClickListener(this);

		/* 归属查询 设置*/
		boolean location_stat = pref.getBoolean("location_stat", false);
		location_show = (SettingRelativeLayout) findViewById(R.id.location_show);
		cb_location = (CheckBox) location_show.findViewById(R.id.cb);
		desc_location = (TextView) location_show.findViewById(R.id.desc);
		location_show.setDefaultStat(location_stat);
		location_show.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sr:
			if (cb.isChecked()) {
				cb.setChecked(false);
				desc.setText("自动更新已关闭");
				pref.edit().putBoolean("stat", false).commit();
			} else {
				cb.setChecked(true);
				desc.setText("自动更新已开启");
				pref.edit().putBoolean("stat", true).commit();
			}
			break;
		case R.id.location_show:
			if (cb_location.isChecked()) {
				cb_location.setChecked(false);
				desc_location.setText("自动捕捉已关闭");
				pref.edit().putBoolean("location_stat", false).commit();
			} else {
				cb_location.setChecked(true);
				desc_location.setText("自动捕捉已开启");
				pref.edit().putBoolean("location_stat", true).commit();
			}
			break;

		}

	}
}
