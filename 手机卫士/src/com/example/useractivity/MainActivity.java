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
	private String[] strs = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "杀毒软件", "缓存清理", "高级工具", "设置中心" };
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
				case R.drawable.home_safe:// 手机防盗
					alertPasswordDialog();
					break;
				case R.drawable.home_callmsgsafe:// 通讯卫士
					Toast.makeText(MainActivity.this, "此模块还未开放",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_apps:// 软件管理
					Toast.makeText(MainActivity.this, "此模块还未开放",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_taskmanager:// 进程管理
					Toast.makeText(MainActivity.this, "此模块还未开放",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_netmanager:// 流量统计
					Toast.makeText(MainActivity.this, "此模块还未开放",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_trojan:// 杀毒软件
					Toast.makeText(MainActivity.this, "此模块还未开放",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_sysoptimize:// 缓存清理
					Toast.makeText(MainActivity.this, "此模块还未开放",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_tools:// 高级工具
					Toast.makeText(MainActivity.this, "此模块还未开放",
							Toast.LENGTH_SHORT).show();
					break;
				case R.drawable.home_settings:// 设置中心
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
		builder.setTitle("提示");
		builder.setMessage("确定退出本应用吗?");
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 显示设置密码对话框
	 */
	private void alertPasswordDialog() {
		String password = preferences.getString("password", null);
		if (!TextUtils.isEmpty(password)) {
			// 已经设置过密码,弹出"输入密码对话框"
			alertInputPassword(password);
		} else {
			// 还没有设置密码,弹出"设置密码对话框"
			alertSetPassword();
		}

	}

	/**
	 * 弹出输入密码对话框
	 * 
	 * @param password
	 */
	private void alertInputPassword(final String password) {
		// 创建dialog弹窗
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog dialog = builder.create();
		// 设置填充的布局
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
						// 密码正确，跳转到安全设置界面
						
						boolean settingfinish = preferences.getBoolean("settingfinish", false);
						if(settingfinish){
							startActivity(new Intent(MainActivity.this,DerectionFive.class));
						}else{
							startActivity(new Intent(MainActivity.this,DerectionOne.class));
						}
						dialog.dismiss();
					} else {
						Toast.makeText(MainActivity.this, "密码错误",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "密码不能为空",
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
	 * 弹出设置密码对话框
	 */
	private void alertSetPassword() {
		// 创建dialog弹窗
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final AlertDialog alertDialog = builder.create();
		// 设置填充布局
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
						Toast.makeText(MainActivity.this, "密码设置成功",
								Toast.LENGTH_SHORT).show();
						// 密码验证成功,将密码加密并保存到本地(C加密后的字符串)
						preferences
								.edit()
								.putString(
										"password",
										MD5Utils.encodingMD5(firstPass(pass,
												pass.length()))).commit();
						alertDialog.dismiss();
					} else {
						Toast.makeText(MainActivity.this, "两次密码不一致",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "输入框不能为空",
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
	 * 定义本地方法对输入的密码进行底层加密
	 * 
	 * @param password
	 *            要加密的密码
	 * @return
	 */
	public native String firstPass(String password, int length);

}
