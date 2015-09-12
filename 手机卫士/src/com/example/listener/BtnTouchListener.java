package com.example.listener;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class BtnTouchListener implements OnTouchListener {

	TextView tv;

	public BtnTouchListener(TextView tv) {
		super();
		this.tv = tv;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			tv.setTextColor(Color.WHITE);
			break;
		case MotionEvent.ACTION_UP:
			tv.setTextColor(0x770000cc);
			break;
		}
		return false;
	}

}
