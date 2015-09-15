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
 * 不需要界面展示的Activity，所以不需要再manifest文件中注册
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
					 * 当手指滑动屏幕是调用
					 *  e1：手指在屏幕上开始滑动的点的坐标
					 *  e2：手指在屏幕上滑动结束的点的坐标
					 * velocityX：手指的屏幕上的X轴滑动的速度 
					 * velocityY：手指在屏幕上的Y轴话滑动的速度
					 */
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						if (Math.abs(e1.getRawY() - e2.getRawY()) > 150) {
							// 当在Y轴上滑动的距离超过150，不做任何处理
							return true;
						}
						if (e1.getRawX() - e2.getRawX() > 150) {
							// 当手指值在屏幕向左滑动的距离超过150,切换到下一页
							nextPage();
						}

						if (e2.getRawX() - e1.getRawX() > 150) {
							// 当手指值在屏幕向右滑动的距离超过150,切换到下一页
							prevPage();
						}
						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});
	}

	public abstract void nextPage();

	public abstract void prevPage();

	/**
	 * 显示下一页
	 * 
	 * @param packageContext
	 *            当前Context对象
	 * @param cls
	 *            要跳转的Context的字节码对象
	 */
	public void showNextPage(Context packageContext, Class<?> cls) {
		startActivity(new Intent(packageContext, cls));
		overridePendingTransition(R.anim.transeanim_left_coming,
				R.anim.transeanim_left_now);
		finish();
	}

	/**
	 * 显示上一页
	 * 
	 * @param packageContext
	 *            当前Context对象
	 * @param cls
	 *            要跳转的Context的字节码对象
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
