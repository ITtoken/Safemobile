package com.example.useractivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.listener.BtnTouchListener;
import com.example.mobilesafe.R;
import com.example.utils.ExitDialogTip;

public class DerectionOne extends BaseDerectActivity {
	private TextView next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_derect_1);

		next = (TextView) findViewById(R.id.next);
		next.setOnTouchListener(new BtnTouchListener(next));
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextPage();
			}
		});
	}

	@Override
	public void onBackPressed() {
		ExitDialogTip.alertExitTip(this);
	}

	@Override
	public void nextPage() {
		showNextPage(this, DerectionTwo.class);
	}

	@Override
	public void prevPage() {
	}

}
