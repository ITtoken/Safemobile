package com.example.downdrag;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.ETC1;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private ImageView iv;
	private ListView lv;
	private MyAdapter ma;
	private RelativeLayout rl_main;
	private EditText et;
	private PopupWindow pw;
	private List<String> list;
	private int PopupWindowheight=200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initArray();
		listViewInit();
		initListener();
	}

	private void initArray() {
		list = new ArrayList<String>();
		for (int i = 0; i <10; i++) {
			list.add("90010-"+i);
		}
	}

	private void listViewInit() {
		lv = new ListView(this);
		lv.setBackgroundColor(Color.WHITE);
		lv.setVerticalScrollBarEnabled(false);
		ma = new MyAdapter();
		lv.setAdapter(ma);
	}

	private void initListener() {
		iv.setOnClickListener(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String codes = list.get(position);
				et.setText(codes);
				pw.dismiss();
			}
		});

	}

	private void initView() {
		iv = (ImageView) findViewById(R.id.iv_drag);
		rl_main = (RelativeLayout) findViewById(R.id.rl_main);
		et = (EditText) findViewById(R.id.et);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_drag:
			if (pw == null) {
				pw = new PopupWindow(lv, rl_main.getWidth(), PopupWindowheight);
			}
			
			pw.setFocusable(true);
			pw.setBackgroundDrawable(new BitmapDrawable());
			pw.setOutsideTouchable(true);
			pw.showAsDropDown(et, 0, 3);
			break;
		}
	}

	class MyAdapter extends BaseAdapter {

		private View view;

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				view = View.inflate(MainActivity.this, R.layout.lv_item, null);
			} else {
				view = convertView;
			}
			final TextView tv = (TextView) view.findViewById(R.id.tv);
			tv.setText(list.get(position));
			final ImageView iv_delete = (ImageView) view.findViewById(R.id.delete_btn);
			iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					list.remove(position);
					notifyDataSetChanged();
					
					pw.update(view.getWidth(),list.size()*tv.getHeight()<=PopupWindowheight? 
							list.size()*tv.getHeight():PopupWindowheight);
					
					
					if(list.size()==0)
					{
						pw.dismiss();
						iv.setVisibility(View.GONE);
					}
				}
			});
			return view;
		}

	}

}
