package com.example.useractivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.mobilesafe.R;
import com.example.utils.AppConst;

/**
 * ����Ҫ����չʾ��Activity�����Բ���Ҫ��manifest�ļ���ע��
 * 
 * @author Administrator
 *
 */
public abstract class BaseDerectActivity extends Activity {

	private GestureDetector gd;
	public SharedPreferences prefer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		prefer = getSharedPreferences(AppConst.APPINFO, MODE_PRIVATE);

		gd = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					/**
					 * ����ָ������Ļ�ǵ���
					 *  e1����ָ����Ļ�Ͽ�ʼ�����ĵ������
					 *  e2����ָ����Ļ�ϻ��������ĵ������
					 * velocityX����ָ����Ļ�ϵ�X�Ử�����ٶ� 
					 * velocityY����ָ����Ļ�ϵ�Y�Ự�������ٶ�
					 */
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 150) {
							// ����Y���ϻ����ľ��볬��150�������κδ���
							return true;
						}
						if (e1.getRawX() - e2.getRawX() > 150) {
							// ����ֵָ����Ļ���󻬶��ľ��볬��150,�л�����һҳ
							nextPage();
						}

						if (e2.getRawX() - e1.getRawX() > 150) {
							// ����ֵָ����Ļ���һ����ľ��볬��150,�л�����һҳ
							prevPage();
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	public abstract void nextPage();

	public abstract void prevPage();

	/**
	 * ��ʾ��һҳ
	 * 
	 * @param packageContext
	 *            ��ǰContext����
	 * @param cls
	 *            Ҫ��ת��Context���ֽ������
	 */
	public void showNextPage(Context packageContext, Class<?> cls) {
		startActivity(new Intent(packageContext, cls));
		overridePendingTransition(R.anim.transeanim_left_coming,
				R.anim.transeanim_left_now);
		finish();
	}

	/**
	 * ��ʾ��һҳ
	 * 
	 * @param packageContext
	 *            ��ǰContext����
	 * @param cls
	 *            Ҫ��ת��Context���ֽ������
	 */
	public void showPrevPage(Context packageContext, Class<?> cls) {
		startActivity(new Intent(packageContext, cls));
		overridePendingTransition(R.anim.transeanim_right_coming,
				R.anim.transeanim_right_now);
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gd.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
}
