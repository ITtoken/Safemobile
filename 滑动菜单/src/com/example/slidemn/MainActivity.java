package com.example.slidemn;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.fragment.NetDiskFragment;
import com.example.fragment.TableThreadFragment;

public class MainActivity extends Activity implements OnClickListener {

	private TextView tv_main;
	private TextView tv_read;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去除原来的系统Title
		setContentView(R.layout.activity_main);
		initView();
		initListner();

		NetDiskFragment fragment = new NetDiskFragment();
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.main_frame, fragment);
		transaction.commit();
	}

	private void initView() {
		tv_main = (TextView) findViewById(R.id.main);
		tv_read = (TextView) findViewById(R.id.read);
	}

	private void initListner() {
		tv_main.setOnClickListener(this);
		tv_read.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		FragmentManager fm;
		FragmentTransaction transaction = null;
		switch (v.getId()) {
		case R.id.main:
			NetDiskFragment fragment = new NetDiskFragment();
			fm = getFragmentManager();
			transaction = fm.beginTransaction();
			transaction.replace(R.id.main_frame, fragment);
			break;
		case R.id.read:
			TableThreadFragment ttf = new TableThreadFragment();
			fm = getFragmentManager();
			transaction = fm.beginTransaction();
			transaction.replace(R.id.main_frame, ttf);
			break;
		}
		transaction.commit();
	}

}
