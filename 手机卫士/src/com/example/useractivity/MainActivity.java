package com.example.useractivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.utils.FileInstance;
import com.example.utils.MD5Utils;

public class MainActivity extends Activity {
	static {
		System.loadLibrary("mobilesafe");
	}

	private static final int EXIT_SELECTED = 0;
	private static final int exitMenu = 1;
	private GridView gv_item;
	private SharedPreferences preferences;
	private String[] strs = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "杀毒软件", "缓存清理" };
	private int[] ids = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private Button menu_bar;
	private TextView tv_titlwbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferences = FileInstance.getPref(this);
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
					Intent intent_callsafe = new Intent(MainActivity.this,
							BlackContacterActivity.class);
					startActivity(intent_callsafe);
					break;
				case R.drawable.home_apps:// 软件管理
					startActivity(new Intent(MainActivity.this,
							SoftManagerActivity.class));
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
				}
			}
		});

		menu_bar = (Button) findViewById(R.id.menu_bar);
		menu_bar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View contentView = View.inflate(MainActivity.this,
						R.layout.window_main_menu, null);

				final PopupWindow window = new PopupWindow(contentView,
						menu_bar.getWidth() * 3, menu_bar.getHeight() * 5 / 2);
				window.setFocusable(true);
				window.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.popup_win_bc));
				window.setOutsideTouchable(true);
				window.setAnimationStyle(R.style.PopupWindowAnim);// 设置动画效果
				window.update();// 更新显示
				window.showAsDropDown(menu_bar, 0, 0);

				/* 设置menubar的条目 */
				TextView adv_tools = (TextView) contentView
						.findViewById(R.id.adv_tools);
				TextView setting = (TextView) contentView
						.findViewById(R.id.setting);
				adv_tools.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this,
								ToolsActivity.class));
						window.dismiss();
					}
				});

				setting.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent_setting = new Intent(MainActivity.this,
								SettingCenter.class);
						startActivity(intent_setting);
						window.dismiss();
					}
				});

			}
		});

		tv_titlwbar = (TextView) findViewById(R.id.tv_titlebar);
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
			View view = null;
			ViewHolder vh = new ViewHolder();
			if (convertView != null) {
				view = convertView;
				vh = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(MainActivity.this, R.layout.item_gridview,
						null);
				vh.tv_item = (TextView) view.findViewById(R.id.tv_item);
				vh.iv_item = (ImageView) view.findViewById(R.id.iv_item);
				view.setTag(vh);
			}

			vh.tv_item.setText(strs[position]);
			vh.iv_item.setBackgroundResource(ids[position]);
			return view;
		}

	}

	/* ViewHolder初始化 */
	class ViewHolder {
		public TextView tv_item;
		public ImageView iv_item;
	}

	private void exitTip() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("确定退出本应用吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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

						boolean settingfinish = preferences.getBoolean(
								"settingfinish", false);
						if (settingfinish) {
							startActivity(new Intent(MainActivity.this,
									DerectionFive.class));
						} else {
							startActivity(new Intent(MainActivity.this,
									DerectionOne.class));
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

	// 退出按钮
	@Override
	public void onBackPressed() {
		showExitDialog();
	}

	/**
	 * 显示退出提示对话框
	 */
	private void showExitDialog() {
		new AlertDialog.Builder(this)
				.setTitle("提示")
				.setIcon(R.drawable.update_tip)
				.setMessage("确定退出吗？")
				.setPositiveButton("确定退出",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.this.finish();
							}
						})
				.setNegativeButton("再等一下",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();

	}
}
