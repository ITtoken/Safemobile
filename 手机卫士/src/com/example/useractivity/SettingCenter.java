package com.example.useractivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.service.CommingShowService;
import com.example.splash.SettingRelativeLayout;
import com.example.utils.FileInstance;

public class SettingCenter extends Activity implements OnClickListener {
	private SettingRelativeLayout sr;
	private CheckBox cb;
	private TextView desc;
	private SharedPreferences pref;
	private SettingRelativeLayout location_show;
	private CheckBox cb_location;
	private TextView desc_location;
	private SettingRelativeLayout comming_show;
	private CheckBox cb_comming_show;
	private TextView desc_comming_show;

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

		/* 归属查询 设置 */
		boolean location_stat = pref.getBoolean("location_stat", false);
		location_show = (SettingRelativeLayout) findViewById(R.id.location_show);
		cb_location = (CheckBox) location_show.findViewById(R.id.cb);
		desc_location = (TextView) location_show.findViewById(R.id.desc);
		location_show.setDefaultStat(location_stat);
		location_show.setOnClickListener(this);

		/* 来电显示归属地 */
		boolean commingshow_stat = pref.getBoolean("commingshow_stat", false);
		comming_show = (SettingRelativeLayout) findViewById(R.id.comming_show);
		cb_comming_show = (CheckBox) comming_show.findViewById(R.id.cb);
		desc_comming_show = (TextView) comming_show.findViewById(R.id.desc);
		comming_show.setDefaultStat(commingshow_stat);
		comming_show.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sr:
			if (cb.isChecked()) {
				cb.setChecked(false);
				desc.setText("关闭");
				pref.edit().remove("stat").commit();
			} else {
				cb.setChecked(true);
				desc.setText("开启");
				pref.edit().putBoolean("stat", true).commit();
			}
			break;
		case R.id.location_show:
			if (cb_location.isChecked()) {
				cb_location.setChecked(false);
				desc_location.setText("关闭");
				pref.edit().remove("location_stat").commit();
			} else {
				cb_location.setChecked(true);
				desc_location.setText("开启");
				pref.edit().putBoolean("location_stat", true).commit();
			}
			break;
		case R.id.comming_show:
			if (cb_comming_show.isChecked()) {
				cb_comming_show.setChecked(false);
				desc_comming_show.setText("关闭");
				pref.edit().remove("commingshow_stat").commit();
				stopService(new Intent(this, CommingShowService.class));
			} else {
				cb_comming_show.setChecked(true);
				desc_comming_show.setText("开启");
				pref.edit().putBoolean("commingshow_stat", true).commit();
				startService(new Intent(this, CommingShowService.class));
			}
			break;

		}

	}
}
