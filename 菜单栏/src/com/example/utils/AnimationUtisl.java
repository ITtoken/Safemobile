package com.example.utils;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;

public class AnimationUtisl {

	public static int animationCount = 0;//正在进行动画的控件的数量
	public static int showcount=3;//显示在界面上的控件的数量

	/**
	 * 隐藏控件动画方法
	 * 
	 * @param rl
	 *            要执行隐藏动画的相对布局文件
	 * @param offset
	 */
	public static void unshow(RelativeLayout rl, int offset) {
		for (int i = 0; i < rl.getChildCount(); i++) {
			rl.getChildAt(i).setEnabled(false);
		}
		RotateAnimation ra = new RotateAnimation(0, -180,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
		ra.setDuration(500);
		ra.setStartOffset(offset);
		ra.setFillAfter(true);
		ra.setAnimationListener(new MyAnimationListener());
		rl.startAnimation(ra);
		
		showcount--;

	}

	/**
	 * 显示控件动画方法
	 * 
	 * @param rl
	 *            要执行显示动画的相对布局文件
	 */
	public static void show(RelativeLayout rl, int offset) {
		for (int i = 0; i < rl.getChildCount(); i++) {
			rl.getChildAt(i).setEnabled(true);
		}
		RotateAnimation ra = new RotateAnimation(-180, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
		ra.setDuration(500);
		ra.setStartOffset(offset);
		ra.setFillAfter(true);
		ra.setAnimationListener(new MyAnimationListener());
		rl.startAnimation(ra);
		
		showcount++;
	}

	static class MyAnimationListener implements AnimationListener {
		// 动画开始时调用
		@Override
		public void onAnimationStart(Animation animation) {
			animationCount++;
		}

		// 动画结束时调用
		@Override
		public void onAnimationEnd(Animation animation) {
			animationCount--;
		}

		// 重复动时调用
		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

}
