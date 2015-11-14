package com.example.useractivity;

import com.example.mobilesafe.R;
import com.example.utils.MUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ToolsActivity extends Activity implements OnClickListener {
	private Button show_location;
	private Button sms_backup;
	private Button update_wangted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tool);
		initView();
	}

	private void initView() {
		show_location = (Button) findViewById(R.id.show_location);
		show_location.setOnClickListener(this);

		sms_backup = (Button) findViewById(R.id.sms_backup);
		sms_backup.setOnClickListener(this);

		update_wangted = (Button) findViewById(R.id.update_wangted);
		update_wangted.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_location:
			startActivity(new Intent(this, LocationShowActivity.class));
			overridePendingTransition(R.anim.transeanim_left_coming,
					R.anim.transeanim_left_now);
			break;
		case R.id.sms_backup:
			startActivity(new Intent(this, SmsBackupActivity.class));
			break;
		case R.id.update_wangted:
			new AlertDialog.Builder(this).setTitle("欢迎使用")
					.setMessage("敬请期待新功能...").setPositiveButton("继续期待", null)
					.show();
			break;
		}
	}
}
