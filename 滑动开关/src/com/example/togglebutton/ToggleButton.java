package com.example.togglebutton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ToggleButton extends View {

	private ToggleState state;
	private Bitmap bgBmp;
	private Bitmap switchBmp;
	private int currentX;
	private Canvas canvas;
	private int swichBtnposition;
	private onToggleStateChangeListener listener;
	private boolean isSlide = false;

	/**
	 * 如果View是在布局文件（xml）中使用，系统会走这个方法， 因为系统要解析XML文件，所以得有AttributeSet attrs这个参数
	 * 
	 * @param context
	 * @param attrs
	 */
	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}

	/**
	 * 如果view需要在java代码中动态区new出来，走的就是这个构造
	 * 
	 * @param context
	 */
	public ToggleButton(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	public enum ToggleState {
		OPEN, CLOSE
	}

	/**
	 * 设置开关控件的背景图片
	 * 
	 * @param slideBgId
	 *            :背景图片的资源id
	 */
	public void setBacgrounfSource(int slideBgId) {
		bgBmp = BitmapFactory.decodeResource(getResources(), slideBgId);
	}

	/**
	 * 设置开关管控件的开关按钮图片
	 * 
	 * @param switckBtnId
	 *            ：Switch开关按钮的资源id
	 */
	public void setSwitckSource(int switckBtnId) {
		switchBmp = BitmapFactory.decodeResource(getResources(), switckBtnId);
	}

	/**
	 * 设置开关空间的初始状态
	 * 
	 * @param stat
	 *            初始状态值
	 * @category ToggleState.OPEN:开; ToggleState.CLOSE:关
	 */
	public void setDefaltStat(ToggleState stat) {
		state = stat;
	}

	/**
	 * 测量：设置控件显示的宽高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(bgBmp.getWidth(), bgBmp.getHeight());// 设置控件显示到宽高值
	}

	/**
	 * 绘制:设置控件显示在界面上的样子
	 * 在绘制的时候,系统会先去回调onMeasure方法,确定一个给定宽高的矩形(setMeasuredDimension的参数),
	 * 然后是在这个矩形范围内进行绘制的
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;
		// 绘制背景图片
		canvas.drawBitmap(bgBmp, 0, 0, null);
		// 绘制滑动块图片

		if (isSlide) {
			swichBtnposition = currentX - switchBmp.getWidth() / 2;
			if (swichBtnposition > 0
					&& swichBtnposition < bgBmp.getWidth()
							- switchBmp.getWidth()) {
				canvas.drawBitmap(switchBmp, swichBtnposition, 0, null);
			} else if (swichBtnposition <= 0) {
				canvas.drawBitmap(switchBmp, 0, 0, null);
			} else {
				canvas.drawBitmap(switchBmp,
						bgBmp.getWidth() - switchBmp.getWidth(), 0, null);
			}
			

		} else {

			if (state == ToggleState.OPEN) {
				canvas.drawBitmap(switchBmp, bgBmp.getWidth() / 2, 0, null);
			} else {
				canvas.drawBitmap(switchBmp, 0, 0, null);
			}
		}

	}

	/**
	 * 触摸监听事件（View都会有这个方法），触摸View时回调这个方法
	 * 
	 * @param event
	 *            ：触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		currentX = (int) event.getX();// 获取View坐标的x坐标值（以View控件的左上角为原点），注意还有一个event.getRawX();，
										// 这个是获取的屏幕坐标的x坐标值（一屏幕左上角为原点）
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isSlide = true;
			break;
		case MotionEvent.ACTION_MOVE:
			isSlide = true;
			break;
		case MotionEvent.ACTION_UP:
			isSlide = false;
			if (currentX < bgBmp.getWidth() / 2) {
				if(state != ToggleState.CLOSE){
					state = ToggleState.CLOSE;
					if(listener != null){
						listener.ToggleState(state);
					}
				}
			} else {
				if(state != ToggleState.OPEN){
					state = ToggleState.OPEN;
					if(listener != null){
						listener.ToggleState(state);
					}
				}
				
			}
			
		
			break;
		}
		invalidate();// 调用这个会引起重绘，必须调用否则看不到滑动效果
		return true;
	}

	public void setToggleStateChangeListener(onToggleStateChangeListener listener) {
		this.listener = listener;
	}

	public interface onToggleStateChangeListener {
		/**
		 * @param toggleState 开关状态：
		 * 
		 * @category 可取值：ToggleState.OPEN 或 ToggleState.CLOSE
		 */
		public void ToggleState(ToggleState toggleState);
	}

}
