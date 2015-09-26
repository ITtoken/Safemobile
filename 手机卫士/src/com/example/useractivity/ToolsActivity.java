package com.example.useractivity;

import com.example.mobilesafe.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ToolsActivity extends Activity implements OnClickListener {
	private Button show_location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tool);

		show_location = (Button) findViewById(R.id.show_location);
		show_location.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_location:
			startActivity(new Intent(this, LocationShowActivity.class));
			overridePendingTransition(R.anim.transeanim_left_coming,
					R.anim.transeanim_left_now);
			break;
		}
	}
}
