package com.example.useractivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.utils.ExitDialogTip;
import com.example.utils.FileInstance;

public class DerectionFive extends Activity implements OnClickListener {

	private TextView reset;
	private TextView exit_setting;
	private SharedPreferences preferences;
	private ImageView lock_stat;
	private TextView show_safeNum;
	private TextView stat_desc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_derect_5);

		preferences = FileInstance.getPref(this);
		boolean protect = preferences.getBoolean("protect", false);

		reset = (TextView) findViewById(R.id.reset);
		exit_setting = (TextView) findViewById(R.id.exit_setting);
		show_safeNum = (TextView) findViewById(R.id.show_safenum);

		reset.setOnClickListener(this);
		exit_setting.setOnClickListener(this);
		lock_stat = (ImageView) findViewById(R.id.lock_stat);
		stat_desc = (TextView) findViewById(R.id.stat_desc);

		if (protect) {
			lock_stat.setBackgroundResource(R.drawable.lock);
			stat_desc.setText("防盗保护已经开启");
		} else {
			lock_stat.setBackgroundResource(R.drawable.unlock);
			stat_desc.setText("防盗保护没有开启");
		}
		showSafeNum();
	}

	/**
	 * 将安全号码显示到textview中
	 */
	private void showSafeNum() {
		String safeNum = preferences.getString("safenum", null);
		if (!TextUtils.isEmpty(safeNum)) {
			show_safeNum.setText(safeNum);
		} else {
			return;
		}
	}

	@Override
	public void onBackPressed() {
		ExitDialogTip.alertExitTip(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.reset:
			startActivity(new Intent(DerectionFive.this, DerectionOne.class));
			overridePendingTransition(R.anim.alphaanim_coming,
					R.anim.alphaanim_now);
			finish();
			break;
		case R.id.exit_setting:
			finish();
			break;
		}
	}

}
