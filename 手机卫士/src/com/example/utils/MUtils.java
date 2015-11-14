package com.example.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;

public class MUtils {
	private Context context;
	private SharedPreferences pref;
	private static long times[] = new long[2];

	public MUtils(Context context) {
		this.context = context;
		pref = FileInstance.getPref(context);
	}

	/**
	 * 弹出吐司提示
	 * 
	 * @param message
	 */
	public void printToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 弹出一个具有自定义样式的Toast
	 * 
	 * @param message
	 *            显示在吐司上的文本内容
	 */
	public void printStyleToast(String message) {
		int whitch = pref.getInt("style_id", 0);
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_LONG);
		/* 设置显示位置和位置偏移量 */
		toast.setGravity(Gravity.TOP, 0, 30);
		/* 填充Toast布局的背景 */
		View view = View.inflate(context, R.layout.toast_background, null);
		LinearLayout toast_ll = (LinearLayout) view.findViewById(R.id.toast_ll);
		toast_ll.setBackgroundResource(AppConst.getId(whitch));
		TextView toast_tv = (TextView) view.findViewById(R.id.toast_tv);
		toast_tv.setText(message);
		toast.setView(view);
		toast.show();
	}

	/**
	 * 
	 * 在更换安全号码的时候验证验证码的弹窗
	 * 
	 * @param titleName
	 * @param message
	 * @param v
	 * @param save_safenum
	 */
	public void printAlert(String titleName, String message, final EditText et,
			final TextView save_safenum) {

		final EditText et_validCode = new EditText(context);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		AlertDialog dialog = builder.create();
		dialog.setTitle(titleName);
		dialog.setMessage(message);
		dialog.setView(et_validCode);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
				new android.content.DialogInterface.OnClickListener() {
					private SharedPreferences pref;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						/* 输入的验证码和发送给安全号码的验证码为同一验证码，否则不能更改安全号码 */

						String input_vc = et_validCode.getText().toString();
						pref = context.getSharedPreferences(AppConst.APPINFO,
								Context.MODE_PRIVATE);
						String validCode = pref.getString("validCode", null);
						if (MD5Utils.encodingMD5(input_vc).equals(validCode)) {
							/* 验证码验证成功 */
							et.setEnabled(true);
							et.setText("");/* 将输入框设置为空 */
							et.setHint("请输入安全号码");
							printToast("验证成功");
							save_safenum.setText("保存安全号码");
							pref.edit().putBoolean("changenum", false).commit();/* 更换安全号码状态变为FALSE */
							pref.edit().remove("validCode").commit();
							pref.edit().remove("safenum").commit();
						} else {
							dialog.dismiss();
							printToast("验证失败");
						}
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	/**
	 * 发送短信
	 * 
	 * @param descAddress
	 * @param text
	 */
	public void sendSMS(String descAddress, String text) {
		/*
		 * android提供了两个SmsManager,gsm包下的是不被推荐使用的,而android.telephony.
		 * SmsManager是现在使用的
		 */
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(descAddress, null, text, null, null);
	}

	/**
	 * 设置默认的文本控件
	 * 
	 * @param text
	 *            控件的额显示内容
	 */
	public TextView myText(String text) {
		TextView tv_item = new TextView(context);
		tv_item.setText(text);
		tv_item.setTextColor(Color.WHITE);
		tv_item.setBackgroundResource(R.drawable.soft_item_bc);
		tv_item.setWidth(LayoutParams.MATCH_PARENT);
		int screen_height = getScreenSize(context).get(AppConst.SCREEN_HEIGHT);
		tv_item.setHeight((int) (screen_height * (30 / 480f)));
		return tv_item;
	}

	/**
	 * 获取屏幕的宽高度
	 */
	public static Map<String, Integer> getScreenSize(Context context) {
		Activity activity = (Activity) context;// 强转获取一个Activity对象
		Map<String, Integer> screenSize = new HashMap<String, Integer>();
		/* 获取屏幕的宽高度 */
		DisplayMetrics mertrix = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(mertrix);
		int window_width = mertrix.widthPixels;
		int window_height = mertrix.heightPixels;
		screenSize.put(AppConst.SCREEN_WIDTH, window_width);
		screenSize.put(AppConst.SCREEN_HEIGHT, window_height);
		return screenSize;
	}

	/**
	 * 双击事件
	 * 
	 * @param gapTime
	 *            间隔时长
	 * @return 是否满足双击要求
	 */
	public static boolean doubleClick(long gapTime) {
		System.arraycopy(times, 1, times, 0, times.length - 1);
		long time1 = System.currentTimeMillis();
		times[times.length - 1] = time1;
		if (times[times.length - 1] - times[0] <= gapTime) {
			return true;
		}
		return false;
	}

	/**
	 * 软件卸载工具方法
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void softUnstall(Context context, String packageName) {
		Intent unstallintent = new Intent();
		unstallintent.setAction(Intent.ACTION_DELETE);
		unstallintent.addCategory(Intent.CATEGORY_DEFAULT);
		unstallintent.setData(Uri.parse("package:" + packageName));/* 需要带有要卸载应用的包名 */
		context.startActivity(unstallintent);
	}

}
