package com.example.adslidebar;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.example.entity.News;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ViewPager vp;
	private List<News> list = new ArrayList<News>();
	private TextView tv;
	private LinearLayout ll;
	private View view;
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			vp.setCurrentItem(vp.getCurrentItem()+1, true);
		};
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initList();
		inintListener();
		initDot();
	}

	// 初始化索引点
	private void initDot() {
		for (int i = 0; i < list.size(); i++) {
			view = new View(this);
			LayoutParams params = new LayoutParams(8, 8);// 为控件设置宽高参数
			if (i != 0) {
				params.leftMargin = 5;// 设置布局参数的左边距
			}
			view.setLayoutParams(params);// 将设置的布局参数应用到view中
			view.setBackgroundResource(R.drawable.selector_dot);
			view.setEnabled(i == vp.getCurrentItem()%list.size());

			ll.addView(view);
		}
	}

	private void initList() {
		list.add(new News(R.drawable.a, "巩俐不低俗,我就不能低俗"));
		list.add(new News(R.drawable.b, "朴树又回来了，在唱经典老歌引万人合唱"));
		list.add(new News(R.drawable.c, "北京国际电影节在京开幕"));
		list.add(new News(R.drawable.d, "乐视TV版送大礼"));
		list.add(new News(R.drawable.e, "热血屌丝的反击"));

		vp.setAdapter(new MyPagerAdapter());
		
		int item = vp.getCurrentItem();// 获取当前正在显示的Page在ViewPager中的索引(位置)
		tv.setText(list.get(item%list.size()).getNewsTitle());
		
		//设置左滑动效果
		int offset = Integer.MAX_VALUE/2%list.size();
		vp.setCurrentItem(Integer.MAX_VALUE/2-offset);
		
		//定义一个计时器，实现自动滑动效果
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 5000,5000);

	}
	

	/**
	 * 设置ViewPager的Adapter（和ListView有很大的相似点）
	 * @author Administrator
	 *
	 */
	class MyPagerAdapter extends PagerAdapter{


		/**
		 * 销毁超出保存数目的view时调用的方法,ViewPager会在内存中保存3个视图的缓存控件(已经划出的,正在播放的,将要划入的),
		 * 而其他的(超出3个)的,会调用这个方法销毁垓视图,并添加新的内容到缓存中.
		 * 
		 * container:当前的PageView对象 position:当前需啊哟销毁第几个page
		 * object:当前需要销毁的view对象
		 */
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {
			container.removeView((View) object);// 一般这样写就行了
		}

		/**
		 * 条目中要显示的内容视图,将要填充的内容视图返回即可
		 * 
		 * container:当前的ViewPager对象 position:当前pager在ViewPager中的位置
		 */
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(MainActivity.this,
					R.layout.viewpager_item, null);

			ImageView iv = (ImageView) view.findViewById(R.id.iv_item);
			News news = list.get(position%list.size());
			iv.setImageResource(news.getSourceId());
			container.addView(view);
			return view;
		}

		/**
		 * 获取要显示的条目数(横向),不过内存中最多保存3个(已经划出的,正在播放的,将要划入的),其他的都会调用destroy方法销毁
		 * ViewPager在初始化时会先调用这个方法获取条目数
		 */
		@Override
		public int getCount() {
			// TODO 自动生成的方法存根
			return Integer.MAX_VALUE;
		}

		/**
		 * view:正在滑出去的view object：将要划入的view
		 * 返回：true：表示要划出去的view和将要划入的view是同一个view，那么使用缓存，不新创建
		 * false：表示要划出的view和将要划入的view不是同一个view，那么新创建一个view对象。
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO 自动生成的方法存根
			return view == object;// 直接这样写就可以(这也是谷歌的推荐写法):当将要划入的
									// view和将要划出的视图是同一个(画到一半返回来),返回true,
									// 否则返回false(划出一把并直接跳到下一个)
		}
	}
	
	private void initView() {
		vp = (ViewPager) findViewById(R.id.vp);
		tv = (TextView) findViewById(R.id.tv);
		ll = (LinearLayout) findViewById(R.id.ll_dot);
	}

	@SuppressWarnings("deprecation")
	private void inintListener() {
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			/**
			 * Page被选中的时候调用
			 * 
			 * 参数：position：被选中的Page在ViewPager中的索引
			 */
			@Override
			public void onPageSelected(int position) {
				tv.setText(list.get(position%list
						.size()).getNewsTitle());
				ll.removeAllViews();
				initDot();
			}
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}
			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

	}

}
