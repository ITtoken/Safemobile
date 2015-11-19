package com.tianjiajia.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;

import com.example.brightnesscontrolor.R;

public class ProtEyeService extends Service {
	private WindowManager wm;
	private WindowManager.LayoutParams wmLp;
	private ViewMajor viewMajor;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		viewMajor = new ViewMajor();

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wmLp = new LayoutParams();
		wmLp.width = LayoutParams.MATCH_PARENT;
		wmLp.height = LayoutParams.MATCH_PARENT;
		wmLp.flags = LayoutParams.FLAG_NOT_FOCUSABLE
				| LayoutParams.FLAG_NOT_TOUCHABLE
				| LayoutParams.FLAG_FULLSCREEN;
		wmLp.type = LayoutParams.TYPE_TOAST;
		wmLp.format = PixelFormat.TRANSLUCENT;
		wmLp.setTitle("Toast");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int alpha_val = intent.getIntExtra("alpha", -1);
		int[] rgb = intent.getIntArrayExtra("rgb");

		LinearLayout vtll = viewMajor.viewtrue_ll;
		if (alpha_val == -1) {
			vtll.setBackgroundColor(Color.argb(100, rgb[0], rgb[1], rgb[2]));
		} else {
			vtll.setBackgroundColor(Color.argb(alpha_val, rgb[0], rgb[1],
					rgb[2]));
		}

		wm.addView(viewMajor.viewtrue, wmLp);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		wm.removeView(viewMajor.viewtrue);
	}

	/**
	 * ª∫¥ÊView≤ºæ÷
	 * 
	 * @author Administrator
	 *
	 */
	class ViewMajor {
		public View viewtrue = View.inflate(ProtEyeService.this,
				R.layout.prot_color, null);
		public LinearLayout viewtrue_ll = (LinearLayout) viewtrue
				.findViewById(R.id.prot_bc);
		public View viewfalse = View.inflate(ProtEyeService.this,
				R.layout.prot_color, null);
		public LinearLayout Viewfalse_ll = (LinearLayout) viewfalse
				.findViewById(R.id.prot_bc);
	}
}
