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
		/* 获取自动捕捉归属地查询的状态 ，如果没有好哒，则默认为不开启 */
		location_stat = pref.getBoolean("location_stat", false);

		/* 通过本地数据库查询需要使用SQLiteDatabase.openDatabase("本地数据库文件路径", "打开模式"); */
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
					/* 当状态为真的时候，确认执行监听行为 */
					String num = et_num.getText().toString();
					String location = getLocation(num);
					if (location != null) {
						tv_result.setText(location);
					} else {
						tv_result.setText("未知号码");
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
				new MUtils(this).printToast("输入框为空");
			}
			if (location != null) {
				tv_result.setText(location);
			} else {
				tv_result.setText("未知号码");
			}
			break;
		}
	}

	/**
	 * 按照输入号码的前七位查询归属地
	 * 
	 * @param num
	 *            号码前七位
	 * @return 归属地
	 */
	public String getLocation(String num) {
		String result = null;
		if (num.matches("^\\d{3,11}$")) {/*保证手机号码的格式正确*/
			if (num.length() < 7) {/*号码长度小于7*/
				if (num.length() == 3) {/*3位号码特殊处理*/
					if (num.equals("110")) {
						result = "报警电话";
					} else if (num.equals("120")) {
						result = "急救电话";
					} else if (num.equals("119")) {
						result = "火警电话";
					} else if (num.equals("113")) {
						result = "国内人工长途挂号";
					} else if (num.equals("114")) {
						result = "号码百事通";
					} else if (num.equals("122")) {
						result = "全国交通报警";
					} else {
						result = "紧急电话";
					}
				} else if (num.matches("^9[5-6]{1}[0-9]{3,6}$")) {/* 95/96开头的号码 */
					result = "银行/社会服务";
				} else if (num.equals("12121")) {
					result = "天气预报";
				} else if (num.equals("12117")) {
					result = "报时服务";
				} else if (num.startsWith("148")) {
					result = "法律服务热线";
				} else {
					result = "未知号码";
				}
			} else {/*大于7位的号码，截取并到数据库中查询*/
				num = et_num.getText().toString().substring(0, 7);
				Cursor cursor = db
						.rawQuery(
								"select location from data2 where id=(select outkey from data1 where id=?);",
								new String[] { num });
				while (cursor.moveToNext()) {
					result = cursor.getString(0);
				}
			}
		} else {/* 格式不正确的号码 */
			result = "不是标准的电话号码";
		}
		return result;
	}
}
