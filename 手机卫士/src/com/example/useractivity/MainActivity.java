package com.example.useractivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.DialogInterface.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.utils.MD5Utils;

public class MainActivity extends Activity {
	static {
		System.loadLibrary("mobilesafe");
	}

	private static final int EXIT_SELECTED = 0;
	private GridView gv_item;
	private SharedPreferences preferences;
	private String[] strs = new String[] { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���",
			"����ͳ��", "ɱ�����", "��������", "�߼�����", "��������" };
	private int[] ids = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferences = getSharedPreferences("appinfo", MODE_PRIVATE);
		gv_item = (GridView) findViewById(R.id.gv_item);
		gv_item.setAdapter(new GvAdapter());
		gv_item.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (ids[position]) {
				case R.drawable.home_safe:// �ֻ�����
					alertPasswordDialog();
					break;
				case R.drawable.home_callmsgsafe:// ͨѶ��ʿ
					Toast.makeText(MainActivity.this, "��ģ�黹δ����",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_apps:// �������
					Toast.makeText(MainActivity.this, "��ģ�黹δ����",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_taskmanager:// ���̹���
					Toast.makeText(MainActivity.this, "��ģ�黹δ����",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_netmanager:// ����ͳ��
					Toast.makeText(MainActivity.this, "��ģ�黹δ����",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_trojan:// ɱ�����
					Toast.makeText(MainActivity.this, "��ģ�黹δ����",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_sysoptimize:// ��������
					Toast.makeText(MainActivity.this, "��ģ�黹δ����",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_tools:// �߼�����
					Toast.makeText(MainActivity.this, "��ģ�黹δ����",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_settings:// ��������
					Intent intent_setting = new Intent(MainActivity.this,
							SettingCenter.class);
					startActivity(intent_setting);
					break;
				}
			}
		});
	}

	class GvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return strs.length;
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(MainActivity.this,
					R.layout.item_gridview, null);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			tv_item.setText(strs[position]);
			iv_item.setBackgroundResource(ids[position]);
			return view;
		}

	}

	private void exitTip() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ʾ");
		builder.setMessage("ȷ���˳���Ӧ����?");
		builder.setPositiveButton("ȷ��", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setNegativeButton("ȡ��", null);
		builder.show();
	}

	/**
	 * ��ʾ��������Ի���
	 */
	private void alertPasswordDialog() {
		String password = preferences.getString("password", null);
		if (!TextUtils.isEmpty(password)) {
			// �Ѿ����ù�����,����"��������Ի���"
			alertInputPassword(password);
		} else {
			// ��û����������,����"��������Ի���"
			alertSetPassword();
		}

	}

	/**
	 * ������������Ի���
	 * 
	 * @param password
	 */
	private void alertInputPassword(final String password) {
		// ����dialog����
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		// �������Ĳ���
		View v = View.inflate(this, R.layout.dialog_inputpassword, null);
		final EditText et_inputpassword = (EditText) v
				.findViewById(R.id.et_inputpassword);
		Button btn_yes = (Button) v.findViewById(R.id.btn_yes);
		Button btn_no = (Button) v.findViewById(R.id.btn_no);
		btn_yes.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String inputpassword = et_inputpassword.getText().toString();
				if (!TextUtils.isEmpty(inputpassword)) {
					if (MD5Utils.encodingMD5(
							firstPass(inputpassword, inputpassword.length()))
							.equals(password)) {
						// ������ȷ����ת����ȫ���ý���
						
						boolean settingfinish = preferences.getBoolean("settingfinish", false);
						if(settingfinish){
							startActivity(new Intent(MainActivity.this,DerectionFive.class));
						}else{
							startActivity(new Intent(MainActivity.this,DerectionOne.class));
						}
						dialog.dismiss();
					} else {
						Toast.makeText(MainActivity.this, "�������",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "���벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		btn_no.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setView(v, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * ������������Ի���
	 */
	private void alertSetPassword() {
		// ����dialog����
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		// ������䲼��
		View view = View.inflate(this, R.layout.dialog_setpassword, null);
		final EditText et_password = (EditText) view
				.findViewById(R.id.et_password);
		final EditText et_confirm_password = (EditText) view
				.findViewById(R.id.et_confirm_password);

		final String pass = et_password.getText().toString();
		final String confirm_pass = et_confirm_password.getText().toString();

		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String pass = et_password.getText().toString();
				String confirm_pass = et_confirm_password.getText().toString();

				if (!TextUtils.isEmpty(pass)
						&& !TextUtils.isEmpty(confirm_pass)) {
					if (pass.equals(confirm_pass)) {
						Toast.makeText(MainActivity.this, "�������óɹ�",
								Toast.LENGTH_SHORT).show();
						// ������֤�ɹ�,��������ܲ����浽����(C���ܺ���ַ���)
						preferences
								.edit()
								.putString(
										"password",
										MD5Utils.encodingMD5(firstPass(pass,
												pass.length()))).commit();
						alertDialog.dismiss();
					} else {
						Toast.makeText(MainActivity.this, "�������벻һ��",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "�������Ϊ��",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
			}
		});
		alertDialog.setView(view, 0, 0, 0, 0);
		alertDialog.show();
	}

	/**
	 * ���屾�ط����������������еײ����
	 * 
	 * @param password
	 *            Ҫ���ܵ�����
	 * @return
	 */
	public native String firstPass(String password, int length);

}
