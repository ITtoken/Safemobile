package com.example.useractivity;

import com.example.mobilesafe.R;
import com.example.utils.FileInstance;
import com.example.utils.MUtils;
import com.example.utils.ShakeUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
		db = SQLiteDatabase.openDatabase(getFilesDir() + "/address.db", null,
				SQLiteDatabase.OPEN_READONLY);

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
					String location = getLocation(num);
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

	private SQLiteDatabase db;
	private SharedPreferences pref;
	private boolean location_stat;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search:
			String num = et_num.getText().toString();
			String location = getLocation(num);
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

	/**
	 * ������������ǰ��λ��ѯ������
	 * 
	 * @param num
	 *            ����ǰ��λ
	 * @return ������
	 */
	public String getLocation(String num) {
		String result = null;
		if (num.matches("^\\d{3,11}$")) {/*��֤�ֻ�����ĸ�ʽ��ȷ*/
			if (num.length() < 7) {/*���볤��С��7*/
				if (num.length() == 3) {/*3λ�������⴦��*/
					if (num.equals("110")) {
						result = "�����绰";
					} else if (num.equals("120")) {
						result = "���ȵ绰";
					} else if (num.equals("119")) {
						result = "�𾯵绰";
					} else if (num.equals("113")) {
						result = "�����˹���;�Һ�";
					} else if (num.equals("114")) {
						result = "�������ͨ";
					} else if (num.equals("122")) {
						result = "ȫ����ͨ����";
					} else {
						result = "�����绰";
					}
				} else if (num.matches("^9[5-6]{1}[0-9]{3,6}$")) {/* 95/96��ͷ�ĺ��� */
					result = "����/������";
				} else if (num.equals("12121")) {
					result = "����Ԥ��";
				} else if (num.equals("12117")) {
					result = "��ʱ����";
				} else if (num.startsWith("148")) {
					result = "���ɷ�������";
				} else {
					result = "δ֪����";
				}
			} else {/*����7λ�ĺ��룬��ȡ�������ݿ��в�ѯ*/
				num = et_num.getText().toString().substring(0, 7);
				Cursor cursor = db
						.rawQuery(
								"select location from data2 where id=(select outkey from data1 where id=?);",
								new String[] { num });
				while (cursor.moveToNext()) {
					result = cursor.getString(0);
				}
			}
		} else {/* ��ʽ����ȷ�ĺ��� */
			result = "���Ǳ�׼�ĵ绰����";
		}
		return result;
	}
}
