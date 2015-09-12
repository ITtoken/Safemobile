package com.example.splash;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

public class SettingRelativeLayout extends RelativeLayout {

	private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private View view;
	private TextView tv_name;
	private TextView tv_desc;
	private CheckBox cb;
	private String title;
	private String desc_on;
	private String desc_off;

	public SettingRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SettingRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		title = attrs.getAttributeValue(NAMESPACE, "title");
		desc_on = attrs.getAttributeValue(NAMESPACE, "desc_on");
		desc_off = attrs.getAttributeValue(NAMESPACE, "desc_off");
		init();
		tv_name.setText(title);
	}

	public SettingRelativeLayout(Context context) {
		super(context);
		init();
	}

	/**
	 * 初始化布局填充
	 */
	private void init() {
		view = View
				.inflate(getContext(), R.layout.item_set_checkbox_stat, this/* 指定要将对应布局填充到哪个父窗口 */);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_desc = (TextView) view.findViewById(R.id.desc);
		cb = (CheckBox) view.findViewById(R.id.cb);
	}

	public void setDefaultStat(boolean stat) {
		cb.setChecked(stat);
		if (stat) {
			tv_desc.setText(desc_on);
		} else {
			tv_desc.setText(desc_off);
		}
	}

}
