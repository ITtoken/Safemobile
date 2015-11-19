package com.tianjiajia.main;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brightnesscontrolor.R;
import com.tianjiajia.service.ProtEyeService;

public class MainActivity extends Activity implements OnClickListener {

	private ImageView selt_control;
	private SeekBar seek_bar;
	private Button btn_sub;
	private Button btn_plus;
	private SharedPreferences pref;
	private boolean selfControl_stat;
	private TextView progress_show;
	private ImageView start_prot;
	private boolean prot_stat;
	private SeekBar prot_setting;
	private TextView prot_progress_show;
	private Button btn_color_chose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		pref = getSharedPreferences("appinfo.xml", MODE_PRIVATE);

		initView();
		initListener();

		// 手动控制状态------------------------
		selfControl_stat = pref.getBoolean("selfControl_stat", false);
		if (selfControl_stat) {
			selt_control.setBackground(getResources()
					.getDrawable(R.drawable.vg));
			setEnable(true, seek_bar, btn_plus, btn_sub, progress_show);
		} else {
			selt_control.setBackground(getResources()
					.getDrawable(R.drawable.xg));
			setEnable(false, seek_bar, btn_plus, btn_sub, progress_show);
		}

		// 进度条初始设置------------------------
		int progress = pref.getInt("progress", 0);
		seek_bar.setProgress(progress);
		Settings.System.putInt(getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS, progress);

		// 护眼蒙版状态------------------------
		prot_stat = pref.getBoolean("prot_stat", false);
		if (prot_stat) {
			start_prot.setBackground(getResources().getDrawable(R.drawable.vg));
			serviceStatInit();
		} else {
			start_prot.setBackground(getResources().getDrawable(R.drawable.xg));
			setEnable(false, prot_setting, prot_progress_show, btn_color_chose);
		}

		// 控制条显示位置初始
		int prot_progress = pref.getInt("prot_progress", 0);
		prot_setting.setProgress(prot_progress);
	}

	// 判断Service是否开启
	private void serviceStatInit() {
		String get = null;
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = am.getRunningServices(20);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String serviceName = runningServiceInfo.process;
			if ("com.example.brightnesscontrolor".equals(serviceName)) {
				get = serviceName;
				break;
			}
		}

		if (TextUtils.isEmpty(get)) {
			int[] rgb = getRGB();
			int prot_progress = pref.getInt("prot_progress", 0);

			Intent intent = new Intent(MainActivity.this, ProtEyeService.class);
			intent.putExtra("alpha", prot_progress);
			intent.putExtra("rgb", rgb);
			startService(intent);
		}

	}

	/**
	 * 视图初始化
	 */
	private void initView() {
		selt_control = (ImageView) findViewById(R.id.selt_control);
		seek_bar = (SeekBar) findViewById(R.id.seek_bar);
		seek_bar.setMax(255);
		btn_sub = (Button) findViewById(R.id.btn_brightsub);
		btn_plus = (Button) findViewById(R.id.btn_brightplus);
		progress_show = (TextView) findViewById(R.id.progress_show);
		start_prot = (ImageView) findViewById(R.id.start_prot);
		// ------------------------
		prot_setting = (SeekBar) findViewById(R.id.prot_setting);
		prot_setting.setMax(255);
		prot_progress_show = (TextView) findViewById(R.id.prot_progress_show);
		btn_color_chose = (Button) findViewById(R.id.btn_color_chose);
	}

	/**
	 * 监听器初始化
	 */
	private void initListener() {
		selt_control.setOnClickListener(this);
		btn_plus.setOnClickListener(this);
		btn_sub.setOnClickListener(this);
		seek_bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			private int progress;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Settings.System.putInt(getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS, progress);
				pref.edit().putInt("progress", progress).commit();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.progress = progress;
				progress_show.setText((progress * 100 / 255) + "%");
			}
		});

		start_prot.setOnClickListener(this);
		btn_color_chose.setOnClickListener(this);
		prot_setting.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress;

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int[] rgb = getRGB();
				stopService(new Intent(MainActivity.this, ProtEyeService.class));

				Intent intent = new Intent(MainActivity.this,
						ProtEyeService.class);
				intent.putExtra("alpha", progress);
				intent.putExtra("rgb", rgb);
				startService(intent);
				pref.edit().putInt("prot_progress", progress).commit();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				this.progress = progress;
				prot_progress_show.setText((progress * 100 / 255) + "%");
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.selt_control:
			selfControl_stat = pref.getBoolean("selfControl_stat", false);
			if (selfControl_stat) {
				selt_control.setBackground(getResources().getDrawable(
						R.drawable.xg));

				// 设置为自动调节模式-----------------------------------------
				Settings.System.putInt(getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS_MODE,
						Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

				setEnable(false, seek_bar, btn_plus, btn_sub, progress_show);

				pref.edit().putBoolean("selfControl_stat", false).commit();
			} else {
				selt_control.setBackground(getResources().getDrawable(
						R.drawable.vg));
				// 设置为手动调节模式------------------------------------------
				Settings.System.putInt(getContentResolver(),
						Settings.System.SCREEN_BRIGHTNESS_MODE,
						Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

				setEnable(true, seek_bar, btn_plus, btn_sub, progress_show);

				pref.edit().putBoolean("selfControl_stat", true).commit();
			}
			break;
		case R.id.btn_brightsub:
			int pro_sub = pref.getInt("progress", 0);

			pro_sub -= 10;// 每次减少10
			if (pro_sub <= 0) {
				pro_sub = 0;
			}

			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, pro_sub);
			seek_bar.setProgress(pro_sub);

			pref.edit().putInt("progress", pro_sub).commit();
			break;
		case R.id.btn_brightplus:
			int pro_plus = pref.getInt("progress", 0);

			pro_plus += 10;// 每次增加10
			if (pro_plus >= 255) {
				pro_plus = 255;
			}

			Settings.System.putInt(getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, pro_plus);
			seek_bar.setProgress(pro_plus);

			pref.edit().putInt("progress", pro_plus).commit();
			break;

		case R.id.start_prot:
			prot_stat = pref.getBoolean("prot_stat", false);
			int[] rgb = getRGB();
			int prot_progress = pref.getInt("prot_progress", -1);
			if (prot_stat) {
				start_prot.setBackground(getResources().getDrawable(
						R.drawable.xg));

				Intent intent = new Intent(this, ProtEyeService.class);
				stopService(intent);

				setEnable(false, prot_setting, prot_progress_show,
						btn_color_chose);
				pref.edit().putBoolean("prot_stat", false).commit();
			} else {
				start_prot.setBackground(getResources().getDrawable(
						R.drawable.vg));

				Intent intent = new Intent(this, ProtEyeService.class);
				intent.putExtra("alpha", prot_progress);
				intent.putExtra("rgb", rgb);
				startService(intent);

				setEnable(true, prot_setting, prot_progress_show,
						btn_color_chose);
				pref.edit().putBoolean("prot_stat", true).commit();
			}
			break;
		case R.id.btn_color_chose:
			final int whitch_selected = pref.getInt("whitch_select", 0);
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("颜色选择");
			dialog.setSingleChoiceItems(new String[] { "皮肤(默认)", "浅绿", "暗灰" },
					whitch_selected, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (whitch_selected == which) {
								Toast.makeText(MainActivity.this, "此样式正在使用哦",
										Toast.LENGTH_SHORT).show();
								return;
							}

							switch (which) {
							case 0:// 皮肤默认
								int[] rgb = { 255, 233, 210 };
								changeColorMode(0, rgb);

								break;
							case 1:// 浅绿
								int[] rgb2 = { 199, 237, 204 };
								changeColorMode(1, rgb2);

								break;
							case 2:// 暗灰
								int[] rgb3 = { 0, 0, 0 };
								changeColorMode(2, rgb3);

								break;
							}
							dialog.dismiss();
						}
					});
			dialog.setNegativeButton("取消", null);
			dialog.show();
			break;
		}
	}

	/* 在处于自动/手动状态时，改变下面所有控件的状态 */
	public void setEnable(Boolean enable_stat, View... views) {
		for (View view : views) {
			view.setEnabled(enable_stat);
		}
	}

	/**
	 * 获取pref中的RGB值的数组
	 * 
	 * @return
	 */
	public int[] getRGB() {
		// 255, 233, 210
		int r = pref.getInt("r", 255);
		int g = pref.getInt("g", 233);
		int b = pref.getInt("b", 210);
		int[] rgb = { r, g, b };
		return rgb;
	}

	/**
	 * 保存rgb的值
	 * 
	 * @param rgb
	 */
	public void putRGB(int... rgb) {
		pref.edit().putInt("r", rgb[0]).commit();
		pref.edit().putInt("g", rgb[1]).commit();
		pref.edit().putInt("b", rgb[2]).commit();
	}

	/**
	 * 改变护眼颜色模式
	 * 
	 * @param whitch
	 * @param rgb
	 */
	public void changeColorMode(int whitch, int... rgb) {
		stopService(new Intent(MainActivity.this, ProtEyeService.class));

		Intent intent = new Intent(MainActivity.this, ProtEyeService.class);
		intent.putExtra("alpha", 100);
		intent.putExtra("rgb", rgb);
		startService(intent);
		pref.edit().putInt("whitch_select", whitch).commit();
		putRGB(rgb);
		prot_setting.setProgress(100);
	}
}
