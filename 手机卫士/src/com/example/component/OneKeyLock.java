package com.example.component;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.utils.MUtils;

public class OneKeyLock extends Activity implements OnClickListener {

	private TextView startManager;
	private TextView unstall;
	private DevicePolicyManager mDpm;
	private ComponentName mDeviceAdminSample;
	private TextView wipedata;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onekeylock);

		mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);/* 获取设备策略管理员 */
		mDeviceAdminSample = new ComponentName(this, MyDeviceAdmin.class);
		if (mDpm.isAdminActive(mDeviceAdminSample)) {
			mDpm.lockNow();
			finish();
		} else {
			new MUtils(this).printToast("此功能需要激活设备管理器");
		}

		startManager = (TextView) findViewById(R.id.start_devicemanager);
		unstall = (TextView) findViewById(R.id.unstaller);
		wipedata = (TextView) findViewById(R.id.wipedata);
		startManager.setOnClickListener(this);
		unstall.setOnClickListener(this);
		wipedata.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_devicemanager:
			Intent intent = new Intent(
					DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
					mDeviceAdminSample);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
					"一键锁屏设备管理器");
			startActivity(intent);
			finish();
			break;
		case R.id.unstaller:
			mDpm.removeActiveAdmin(mDeviceAdminSample);

			Intent unstallintent = new Intent();
			unstallintent.setAction(Intent.ACTION_DELETE);
			unstallintent.addCategory(Intent.CATEGORY_DEFAULT);
			unstallintent.setData(Uri.parse("package:" + getPackageName()));
			startActivity(unstallintent);
			break;
		case R.id.wipedata:
			if (mDpm.isAdminActive(mDeviceAdminSample)) {
				mDpm.wipeData(0);
			}
			break;
		}
	}

}
