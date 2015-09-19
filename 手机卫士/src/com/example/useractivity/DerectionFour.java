package com.example.useractivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.splash.SettingRelativeLayout;
import com.example.utils.ExitDialogTip;
import com.example.utils.MUtils;

public class DerectionFour extends BaseDerectActivity implements
		OnClickListener {
	private TextView perior_four;
	private TextView setting_finish;
	private SettingRelativeLayout sr;
	private CheckBox cb;
	private TextView desc;
	private boolean protect_stat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_derect_4);

		perior_four = (TextView) findViewById(R.id.perior_four);
		setting_finish = (TextView) findViewById(R.id.setting_finish);
		sr = (SettingRelativeLayout) findViewById(R.id.derectfour_sr);
		cb = (CheckBox) sr.findViewById(R.id.cb);
		desc = (TextView) sr.findViewById(R.id.desc);
		sr.setDefaultStat(false);

		perior_four.setOnClickListener(this);
		setting_finish.setOnClickListener(this);
		sr.setOnClickListener(this);

		protect_stat = prefer.getBoolean("protect", false);
		if (protect_stat) {
			cb.setChecked(true);
			desc.setText("防盗保护已经开启");
		} else {
			cb.setChecked(false);
			desc.setText("防盗保护没有开启");
		}
	}

	@Override
	public void onBackPressed() {
		ExitDialogTip.alertExitTip(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.perior_four:
			showPrevPage(this, DerectionThree.class);
			break;
		case R.id.setting_finish:
			boolean protect_stat2 = prefer.getBoolean("protect", false);
			if (!protect_stat2) {
				new MUtils(this).printToast("您还没有防盗保护功能");
			} else {
				/* 只有开启防盗功能之后才能进入最终设置页面 */
				showNextPage(this, DerectionFive.class);
				prefer.edit().putBoolean("settingfinish", true).commit();/* 保存设置完成状态 */
			}
			break;
		case R.id.derectfour_sr:
			if (cb.isChecked()) {
				cb.setChecked(false);
				desc.setText("防盗保护没有开启");
				prefer.edit().putBoolean("protect", false).commit();
			} else {
				cb.setChecked(true);
				desc.setText("防盗保护已经开启");
				prefer.edit().putBoolean("protect", true).commit();
			}
			break;
		}
	}

	@Override
	public void nextPage() {
	}

	@Override
	public void prevPage() {
		showPrevPage(this, DerectionThree.class);
	}

}
