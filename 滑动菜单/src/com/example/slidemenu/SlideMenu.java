package com.example.slidemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SlideMenu extends ViewGroup {

	private View menuView, mainView;
	private  int menuHiewWidth;
	private int downX;


	/**
	 * 想要动态New的时候，调用这个构造函数
	 * 
	 * @param context
	 */
	public SlideMenu(Context context) {
		super(context);
	}

	/**
	 * 要想在xml文件中使用，就必须要实现这个构造函数
	 * 
	 * @param context
	 * @param attrs
	 */
	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 当ViewGroup中所有的子View（一级子View）都加在完毕的时候调用，初始化子view的引用
	 * 
	 * 注意：这里无法获取子view的宽高
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		menuView = getChildAt(0);
		mainView = getChildAt(1);
	}

	/**
	 * 决定子控件在Slidemenu中的绘制大小
	 * 
	 * widthMeasureSpec：自定义控件（SlideMenu）的宽 heightMeasureSpec：定义控件（SlideMenu）的高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		menuHiewWidth = menuView.getLayoutParams().width;
		menuView.measure(menuHiewWidth, heightMeasureSpec);
		mainView.measure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * 决定子控件在SlideMenu中显示位置
	 * 
	 * ↓ 以GroupView（这里是SlideView）的左上角为原点，Y轴向下为正方向的坐标轴为急诊基准
	 * 
	 * l：groupView的左边的坐标（X坐标）：0 t：GroupView上边的左边（Y坐标）：0
	 * r：groupView右边的坐标（X左边）：Group的宽度 b：GroupView下边的坐标（Y坐标）：GroupView的高
	 * 
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		menuView.layout(-menuHiewWidth, 0, 0, b);
		mainView.layout(l, t, r, b);
	}

	int slideX = 0;
	private  ScrollAnimation sa;

	/**
	 * 添加触摸事件侦听
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			int mx = (int) event.getX();
			int moveX = mx - downX;// 移动的距离

			slideX = getScrollX() - moveX;

			if (slideX < -menuHiewWidth) {
				slideX = -menuHiewWidth;
			} else if (slideX > 0) {
				slideX = 0;
			}
			scrollTo(slideX, 0);
			break;
		case MotionEvent.ACTION_UP:
			if (slideX <= -menuHiewWidth / 2) {
				sa = new ScrollAnimation(this, -menuHiewWidth);
				invalidate();
			} else if (slideX > -menuHiewWidth / 2) {
				sa = new ScrollAnimation(this, 0);
				invalidate();
			}
			startAnimation(sa);
			break;
		}
		return true;
	}

	 class ScrollAnimation extends Animation {

		private View view;
		private int targetX;
		private int startX;
		private float totalScroll;

		public ScrollAnimation(View view, int targetX) {
			super();
			this.view = view;
			this.targetX = targetX;

			startX = view.getScrollX();
			totalScroll = this.targetX - startX;

			setDuration(Math.abs(startX));
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			int currentX = (int) (startX + totalScroll * interpolatedTime);
			view.scrollTo(currentX, 0);
		}
	}
}
