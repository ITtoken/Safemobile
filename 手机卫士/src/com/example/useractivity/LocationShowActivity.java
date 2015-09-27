package com.example.useractivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.utils.FileInstance;
import com.example.utils.LocationSearchUtils;
import com.example.utils.MUtils;
import com.example.utils.ShakeUtils;

public class LocationShowActivity extends Activity implements OnClickListener {
	private Button btn_search;
	private EditText et_num;
	private TextView tv_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_locationshow);
		pref = FileInstance.getPref(this);
		/* ��ȡ�Զ���׽�����ز�ѯ��״̬ �����û�к��գ���Ĭ��Ϊ������ */
		location_stat = pref.getBoolean("location_stat", false);

		/* ͨ���������ݿ��ѯ��Ҫʹ��SQLiteDatabase.openDatabase("�������ݿ��ļ�·��", "��ģʽ"); */
		LocationSearchUtils.iniDataBases(this);

		et_num = (EditText) findViewById(R.id.et_num);
		btn_search = (Button) findViewById(R.id.search);
		tv_result = (TextView) findViewById(R.id.tv_result);
		btn_search.setOnClickListener(this);

		et_num.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (location_stat) {
					/* ��״̬Ϊ���ʱ��ȷ��ִ�м�����Ϊ */
					String num = et_num.getText().toString();
					String location = LocationSearchUtils.getLocation(et_num,
							num);
					if (location != null) {
						tv_result.setText(location);
					} else {
						tv_result.setText("δ֪����");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private SharedPreferences pref;
	private boolean location_stat;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			String num = et_num.getText().toString();
			String location = LocationSearchUtils.getLocation(et_num, num);
			if (TextUtils.isEmpty(num)) {
				ShakeUtils.shakeAnim(this, et_num);
				ShakeUtils.deviceShake(this);
				new MUtils(this).printToast("�����Ϊ��");
			}
			if (location != null) {
				tv_result.setText(location);
			} else {
				tv_result.setText("δ֪����");
			}
			break;
		}
	}
}
