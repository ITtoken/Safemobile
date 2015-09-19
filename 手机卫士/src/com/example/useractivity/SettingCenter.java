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

public class SettingCenter extends Activity {
	private SettingRelativeLayout sr;
	private CheckBox cb;
	private TextView desc;
	private SharedPreferences pref;

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

		sr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cb.isChecked()) {
					cb.setChecked(false);
					desc.setText("自动更新已关闭");
					pref.edit().putBoolean("stat", false).commit();
				} else {
					cb.setChecked(true);
					desc.setText("自动更新已开启");
					pref.edit().putBoolean("stat", true).commit();
				}
			}
		});
	}
}
