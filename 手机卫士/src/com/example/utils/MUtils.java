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
	 * ������˾��ʾ
	 * 
	 * @param message
	 */
	public void printToast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ����һ�������Զ�����ʽ��Toast
	 * 
	 * @param message
	 *            ��ʾ����˾�ϵ��ı�����
	 */
	public void printStyleToast(String message) {
		int whitch = pref.getInt("style_id", 0);
		Toast toast = new Toast(context);
		toast.setDuration(Toast.LENGTH_LONG);
		/* ������ʾλ�ú�λ��ƫ���� */
		toast.setGravity(Gravity.TOP, 0, 30);
		/* ���Toast���ֵı��� */
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
	 * �ڸ�����ȫ�����ʱ����֤��֤��ĵ���
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
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "ȷ��",
				new android.content.DialogInterface.OnClickListener() {
					private SharedPreferences pref;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						/* �������֤��ͷ��͸���ȫ�������֤��Ϊͬһ��֤�룬�����ܸ��İ�ȫ���� */

						String input_vc = et_validCode.getText().toString();
						pref = context.getSharedPreferences(AppConst.APPINFO,
								Context.MODE_PRIVATE);
						String validCode = pref.getString("validCode", null);
						if (MD5Utils.encodingMD5(input_vc).equals(validCode)) {
							/* ��֤����֤�ɹ� */
							et.setEnabled(true);
							et.setText("");/* �����������Ϊ�� */
							et.setHint("�����밲ȫ����");
							printToast("��֤�ɹ�");
							save_safenum.setText("���氲ȫ����");
							pref.edit().putBoolean("changenum", false).commit();/* ������ȫ����״̬��ΪFALSE */
							pref.edit().remove("validCode").commit();
							pref.edit().remove("safenum").commit();
						} else {
							dialog.dismiss();
							printToast("��֤ʧ��");
						}
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "ȡ��",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog.show();
	}

	/**
	 * ���Ͷ���
	 * 
	 * @param descAddress
	 * @param text
	 */
	public void sendSMS(String descAddress, String text) {
		/*
		 * android�ṩ������SmsManager,gsm���µ��ǲ����Ƽ�ʹ�õ�,��android.telephony.
		 * SmsManager������ʹ�õ�
		 */
		SmsManager sm = SmsManager.getDefault();
		sm.sendTextMessage(descAddress, null, text, null, null);
	}

	/**
	 * ����Ĭ�ϵ��ı��ؼ�
	 * 
	 * @param text
	 *            �ؼ��Ķ���ʾ����
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
	 * ��ȡ��Ļ�Ŀ�߶�
	 */
	public static Map<String, Integer> getScreenSize(Context context) {
		Activity activity = (Activity) context;// ǿת��ȡһ��Activity����
		Map<String, Integer> screenSize = new HashMap<String, Integer>();
		/* ��ȡ��Ļ�Ŀ�߶� */
		DisplayMetrics mertrix = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(mertrix);
		int window_width = mertrix.widthPixels;
		int window_height = mertrix.heightPixels;
		screenSize.put(AppConst.SCREEN_WIDTH, window_width);
		screenSize.put(AppConst.SCREEN_HEIGHT, window_height);
		return screenSize;
	}

	/**
	 * ˫���¼�
	 * 
	 * @param gapTime
	 *            ���ʱ��
	 * @return �Ƿ�����˫��Ҫ��
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
	 * ���ж�ع��߷���
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void softUnstall(Context context, String packageName) {
		Intent unstallintent = new Intent();
		unstallintent.setAction(Intent.ACTION_DELETE);
		unstallintent.addCategory(Intent.CATEGORY_DEFAULT);
		unstallintent.setData(Uri.parse("package:" + packageName));/* ��Ҫ����Ҫж��Ӧ�õİ��� */
		context.startActivity(unstallintent);
	}

}
