package com.example.splash;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.service.CommingShowService;
import com.example.useractivity.MainActivity;
import com.example.utils.AppConst;
import com.example.utils.FileInstance;
import com.example.utils.In2Out;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class SplashActivity extends Activity {
	private TextView tv_welcome;
	private TextView tv_downStat;
	private String vn;// 本地获取的版本名
	private int vc;// 本地获取的版本号
	private String aPPName;// 获取应用名称

	// 网络获取版本数据
	private String versionName;
	private int versionCode;
	private String description;
	private String downloadURL;
	private SharedPreferences pref;
	private RelativeLayout rl_anim;
	private AnimationDrawable ad;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case AppConst.NO_UPDATE:// 已是最新版本，无需更新（直接进入主界面）
				goToMainActivity();
				break;
			case AppConst.UPDATE_DIALOG:// 弹出更新对话框
				showUpdateDialog();
				break;
			case AppConst.URL_EXCEPTION:// url异常提示
				Toast.makeText(SplashActivity.this, "请求资源不存在",
						Toast.LENGTH_SHORT).show();
				goToMainActivity();
				break;
			case AppConst.INTERNET_EXCEPTION:// 网络异常提示
				Toast.makeText(SplashActivity.this, "网络链接异常",
						Toast.LENGTH_SHORT).show();
				goToMainActivity();
				break;
			case AppConst.JSON_EXCEPTION:// 获取json数据异常
				Toast.makeText(SplashActivity.this, "数据请求失败",
						Toast.LENGTH_SHORT).show();
				goToMainActivity();
				break;

			}
		}
	};
	private boolean commingshow_stat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		loadLocationLib();/* 导入归属地数据库 */
		pref = FileInstance.getPref(this);
		tv_welcome = (TextView) findViewById(R.id.tv_welcome);
		tv_welcome.setText("版本号：" + getVersionName());
		tv_downStat = (TextView) findViewById(R.id.tv_downstat);
		rl_anim = (RelativeLayout) findViewById(R.id.rl_anim);
		rl_anim.setBackgroundResource(R.drawable.splashanim);
		ad = (AnimationDrawable) rl_anim.getBackground();
		ad.start();

		boolean stat = pref.getBoolean("stat", true);
		if (stat) {
			getUpdateInfo();// 检查更新APP并更新
		} else {
			goToMainActivity();
		}

		commingshow_stat = pref.getBoolean("commingshow_stat", false);
		if (commingshow_stat) {
			if (!checkServiceStat()) {
				startService(new Intent(this, CommingShowService.class));
			}
		}
	}

	/**
	 * 检查来电显示服务是否开启
	 * 
	 * @param commingshow_stat
	 * @return
	 */
	private boolean checkServiceStat() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(100);
		Iterator<RunningServiceInfo> it = list.iterator();
		String serviceTempName = null;
		while (it.hasNext()) {
			RunningServiceInfo next = it.next();
			String serviceName = next.service.getShortClassName();
			if (serviceName.equals("com.example.service.CommingShowService")) {
				serviceTempName = serviceName;
				break;
			}
		}

		if (serviceTempName == null) {
			return false;
		}

		return true;

	}

	/**
	 * 通过网络获取版本信息
	 */
	private void getUpdateInfo() {
		new Thread() {
			private HttpURLConnection conn;
			private Message msg;

			public void run() {
				msg = handler.obtainMessage();
				try {
					URL url = new URL("http://10.100.0.173/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);

					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();
						String content = In2Out.getInputstreamInfo(in);

						// 将获取的数据转换成JSOn对象：JsonObject类
						JSONObject json = new JSONObject(content);
						versionCode = (Integer) json.get("versionCode");// 获取版本号

						if (versionCode > vc) {
							versionName = (String) json.get("versionName");// 获取版本名
							description = (String) json.get("description");// 获取新版本描述
							downloadURL = (String) json.get("downloadURL");// 获取新版本下载URL
							aPPName = (String) json.get("APPName");

							msg.what = AppConst.UPDATE_DIALOG;
						} else {
							Thread.sleep(2000);
							msg.what = AppConst.NO_UPDATE;
						}
					}
				} catch (MalformedURLException e1) {
					// url异常
					msg.what = AppConst.URL_EXCEPTION;
					e1.printStackTrace();
				} catch (IOException e) {
					// 网络链接异常
					msg.what = AppConst.INTERNET_EXCEPTION;
					e.printStackTrace();
				} catch (JSONException e11) {
					// json数据获取异常
					msg.what = AppConst.JSON_EXCEPTION;
					e11.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					handler.sendMessage(msg);
					conn.disconnect();
				}
			};
		}.start();
	}

	/**
	 * 获取版本名
	 * 
	 * @return
	 */
	public String getVersionName() {
		try {
			PackageManager pm = getPackageManager();
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			vn = info.versionName;
			vc = info.versionCode;
			return vn;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 弹出更新提示对话框
	 */
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("新版本更新提示(" + versionName + ")");
		builder.setMessage(description);
		builder.setIcon(R.drawable.update_tip);
		builder.setPositiveButton("确认下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downLoadApp();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToMainActivity();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			/**
			 * 当用户点击返回键或者取消对话框显示时，会调用此监听此方法
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				goToMainActivity();
			}
		});
		builder.show();

	};

	/**
	 * 下载APP
	 */
	public void downLoadApp() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/" + aPPName);
			// 如果本地已经有下载好的文件,则直接使用安装
			if (file.exists()) {
				// 启动安装程序
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				startActivityForResult(intent, 0);
			} else {
				// 否则从服务器获取
				HttpUtils utils = new HttpUtils();
				String target = Environment.getExternalStorageDirectory() + "/"
						+ aPPName;
				utils.download(downloadURL, target, true, true,
						new RequestCallBack<File>() {

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								super.onLoading(total, current, isUploading);

								tv_downStat.setVisibility(View.VISIBLE);// 设置为可见
								tv_downStat.setText("下载进度：" + current * 100
										/ total + "%");
							}

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								// tv_downStat.setVisibility(View.GONE);//
								// 设置为不可见
								Toast.makeText(SplashActivity.this, "下载成功",
										Toast.LENGTH_SHORT).show();
								// 启动安装程序
								Intent intent = new Intent(Intent.ACTION_VIEW);
								intent.addCategory(Intent.CATEGORY_DEFAULT);
								intent.setDataAndType(
										Uri.fromFile(arg0.result),
										"application/vnd.android.package-archive");
								startActivityForResult(intent, 0);
							}

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
							}
						});
			}
		} else {
			Toast.makeText(SplashActivity.this, "SD卡未找到", Toast.LENGTH_SHORT)
					.show();
			goToMainActivity();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 如果用户在安装界面取消安装，返回后直接进入主界面
		goToMainActivity();
	}

	/**
	 * 进入主界面
	 */
	public void goToMainActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 加载归属地数据库
	 */
	private void loadLocationLib() {
		try {
			File file = new File(getFilesDir(), "address.db");
			if (!file.exists()) {
				In2Out.fileCopy(getAssets().open("address.db"), getFilesDir()
						+ "/address.db");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
