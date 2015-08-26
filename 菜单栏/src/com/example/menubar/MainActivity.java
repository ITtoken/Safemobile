package com.example.menubar;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.utils.AnimationUtisl;

public class MainActivity extends Activity implements OnClickListener {

	boolean islevel1 = true;
	boolean islevel2 = true;
	boolean islevel3 = true;
	private ImageView home;
	private RelativeLayout rl2;
	private ImageView level2_menu;
	private RelativeLayout rl3;
	private RelativeLayout rl1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		listenerview();
	}

	// 系统的物理键被按下的时候调用
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {// 当点击菜单键的时候
			//如果还有动画在进行,则直接返回false,不在进行
			if (AnimationUtisl.animationCount != 0) {
				return false;
			}

			if (AnimationUtisl.showcount != 0) {
				int delaytime = 0;
				if (islevel3) {
					AnimationUtisl.unshow(rl3, delaytime);
					delaytime += 200;
					islevel3 = !islevel3;
				}
				if (islevel2) {
					AnimationUtisl.unshow(rl2, delaytime);
					delaytime += 200;
					islevel2 = !islevel2;
				}

				AnimationUtisl.unshow(rl1, delaytime);
				islevel1 = !islevel1;

			} else {
				int delaytime = 0;
				
				AnimationUtisl.show(rl1, delaytime);
				delaytime += 200;
				islevel1 = !islevel1;
				
				AnimationUtisl.show(rl2, delaytime);
				delaytime += 200;
				islevel2 = !islevel2;
				
				AnimationUtisl.show(rl3, delaytime);
				islevel3 = !islevel3;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	// 控件初始化
	private void initView() {
		home = (ImageView) findViewById(R.id.home);
		rl2 = (RelativeLayout) findViewById(R.id.level2);
		level2_menu = (ImageView) findViewById(R.id.level2_menu);
		rl3 = (RelativeLayout) findViewById(R.id.level3);
		rl1 = (RelativeLayout) findViewById(R.id.level1);
	}

	// 监听初始化方法
	private void listenerview() {
		home.setOnClickListener(this);
		level2_menu.setOnClickListener(this);
	}

	// 点击实现方法
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home:
			if (AnimationUtisl.animationCount != 0) {
				return;
			}
			int offset = 0;
			if (islevel2) {
				if (islevel3) {
					AnimationUtisl.unshow(rl3, offset);
					offset += 200;
					islevel3 = !islevel3;
				}
				AnimationUtisl.unshow(rl2, offset);
			} else {
				AnimationUtisl.show(rl2, 0);
			}
			islevel2 = !islevel2;
			break;

		case R.id.level2_menu:
			if (AnimationUtisl.animationCount != 0) {
				return;
			}
			if (islevel3) {
				AnimationUtisl.unshow(rl3, 0);
			} else {
				AnimationUtisl.show(rl3, 0);
			}
			islevel3 = !islevel3;
			break;
		}
	}

}
