package com.example.utils;

import com.example.mobilesafe.R;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ShakeUtils {
	/**
	 * Ϊ�ؼ������������
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param v
	 *            �ؼ�����
	 */
	public static void shakeAnim(Context context, View v) {
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
		v.startAnimation(shake);
	}

	/**
	 * ����ֻ���Ч��
	 */
	public static void deviceShake(Context context) {
		Vibrator vb = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vb.vibrate(300);
	}
}
