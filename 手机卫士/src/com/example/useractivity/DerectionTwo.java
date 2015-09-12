package com.example.useractivity;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listener.BtnTouchListener;
import com.example.mobilesafe.R;
import com.example.splash.SettingRelativeLayout;
import com.example.utils.ExitDialogTip;
import com.example.utils.MUtils;

public class DerectionTwo extends BaseDerectActivity implements OnClickListener {
	private TextView perior_two;
	private TextView next_two;
	private SettingRelativeLayout drect2_sr;
	private CheckBox cb;
	private TextView desc;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_derect_2);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		drect2_sr = (SettingRelativeLayout) findViewById(R.id.drect2_sr);
		perior_two = (TextView) findViewById(R.id.perior_two);
		next_two = (TextView) findViewById(R.id.next_two);

		perior_two.setOnClickListener(this);
		perior_two.setOnTouchListener(new BtnTouchListener(perior_two));
		next_two.setOnClickListener(this);
		next_two.setOnTouchListener(new BtnTouchListener(next_two));
		drect2_sr.setOnClickListener(this);

		drect2_sr.setDefaultStat(false);
		cb = (CheckBox) drect2_sr.findViewById(R.id.cb);
		desc = (TextView) drect2_sr.findViewById(R.id.desc);

		String sim = prefer.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			cb.setChecked(true);
			desc.setText("SIM卡已经绑定");
		}
	}

	@Override
	public void onBackPressed() {
		ExitDialogTip.alertExitTip(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.perior_two:
			showPrevPage(this, DerectionOne.class);
			break;
		case R.id.next_two:
			showNextPage(this, DerectionThree.class);
			break;
		case R.id.drect2_sr:
			switch (tm.getSimState()) {
			case TelephonyManager.SIM_STATE_ABSENT:
				new MUtils(this).printToast("SIM卡未插入");
				break;
			case TelephonyManager.SIM_STATE_READY:
				if (cb.isChecked()) {
					cb.setChecked(false);
					desc.setText("SIM卡没有绑定");
					prefer.edit().remove("sim").commit();
				} else {
					cb.setChecked(true);
					desc.setText("SIM卡已经绑定");
					String simSerialNumber = tm.getSimSerialNumber();
					prefer.edit().putString("sim", simSerialNumber).commit();
				}
				break;
			}
			break;
		}
	}

	@Override
	public void nextPage() {
		showNextPage(this, DerectionThree.class);
	}

	@Override
	public void prevPage() {
		showPrevPage(this, DerectionOne.class);
	}

}
