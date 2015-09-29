package com.example.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.telephony.gsm.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;

public class MUtils {
	private Context context;

	public MUtils(Context context) {
		this.context = context;
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
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_LONG);
		/* 设置显示位置和位置偏移量 */
		toast.setGravity(Gravity.TOP, 0, 30);
		/* 填充Toast布局的背景 */
		View view = View.inflate(context, R.layout.toast_background, null);
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
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(descAddress, null, text, null, null);
	}

}
