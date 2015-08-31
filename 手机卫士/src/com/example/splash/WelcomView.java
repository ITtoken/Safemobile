package com.example.splash;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe.R;
import com.example.useractivity.MainActivity;
import com.example.utils.In2Out;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class WelcomView extends Activity {

	protected static final int UPDATE_DIALOG = 0;
	protected static final int URL_EXCEPTION = 1;
	protected static final int INTERNET_EXCEPTION = 2;
	protected static final int JSON_EXCEPTION = 3;
	protected static final int NO_UPDATE = 4;

	private TextView tv_welcome;
	private String vn;// 本地获取的版本名
	private int vc;// 本地获取的版本号
	private String aPPName;// 获取应用名称

	// 网络获取版本数据
	private String versionName;
	private int versionCode;
	private String description;
	private String downloadURL;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NO_UPDATE:// 已是最新版本，无需更新（直接进入主界面）
				goToMainActivity();
				break;
			case UPDATE_DIALOG:
				// 弹出更新对话框
				showUpdateDialog();
				break;
			case URL_EXCEPTION:// url异常提示
				Toast.makeText(WelcomView.this, "请求资源不存在", Toast.LENGTH_SHORT)
						.show();
				goToMainActivity();
				break;
			case INTERNET_EXCEPTION:// 网络异常提示
				Toast.makeText(WelcomView.this, "网络链接异常", Toast.LENGTH_SHORT)
						.show();
				goToMainActivity();
				break;
			case JSON_EXCEPTION:// 获取json数据异常
				Toast.makeText(WelcomView.this, "数据请求失败", Toast.LENGTH_SHORT)
						.show();
				goToMainActivity();
				break;

			}
		}
	};
	private TextView tv_downStat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		tv_welcome = (TextView) findViewById(R.id.tv_welcome);
		tv_welcome.setText("版本号：" + getVersionName());
		tv_downStat = (TextView) findViewById(R.id.tv_downstat);

		getUpdateInfo();
	}

	/**
	 * 通过网络获取版本信息
	 */
	private void getUpdateInfo() {
		new Thread() {
			private Message msg;
			private HttpURLConnection conn;
			Date date = new Date();

			public void run() {
				try {
					long currentTime = date.getTime();
					msg = handler.obtainMessage();

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
						versionCode = (Integer) json.get("versionCode");// 胡群殴版本号

						if (versionCode > vc) {
							versionName = (String) json.get("versionName");// 获取版本名
							description = (String) json.get("description");// 获取新版本描述
							downloadURL = (String) json.get("downloadURL");// 获取新版本下载URL
							aPPName = (String) json.get("APPName");

							System.out.println("应用名：" + aPPName + "下载地址："
									+ downloadURL);

							msg.what = UPDATE_DIALOG;
						} else {
							long nowTime = date.getTime();
							long time = nowTime - currentTime;
							if (time < 2000) {
								try {
									Thread.sleep(2000 - time);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								msg.what = NO_UPDATE;

							}
						}

					}
				} catch (MalformedURLException e) {
					// url异常
					msg.what = URL_EXCEPTION;
					e.printStackTrace();
				} catch (IOException e) {
					// 网络链接异常
					msg.what = INTERNET_EXCEPTION;
					e.printStackTrace();
				} catch (JSONException e) {
					// json数据获取异常
					msg.what = JSON_EXCEPTION;
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
		builder.setPositiveButton("确认下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downLoadApp();
				goToMainActivity();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToMainActivity();
			}
		});
		builder.show();
	};

	/**
	 * 下载APP
	 */
	public void downLoadApp() {
		if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
			HttpUtils utils = new HttpUtils();
			String target = Environment.getExternalStorageDirectory().getPath()
					+ "/" + aPPName;
			utils.download(downloadURL, target, true, true,
					new RequestCallBack<File>() {

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							super.onLoading(total, current, isUploading);

							tv_downStat.setVisibility(View.VISIBLE);// 设置为可见
							tv_downStat.setText("下载进度：" + current * 100 / total
									+ "%");
						}

						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							// tv_downStat.setVisibility(View.GONE);// 设置为不可见
							Toast.makeText(WelcomView.this, "下载成功",
									Toast.LENGTH_SHORT).show();
							// 启动安装程序
							Intent intent = new Intent(Intent.ACTION_VIEW);
							intent.addCategory(Intent.CATEGORY_DEFAULT);
							intent.setDataAndType(Uri.fromFile(arg0.result),
									"application/vnd.android.package-archive");
							startActivity(intent);
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {

						}
					});
		} else {
			Toast.makeText(WelcomView.this, "SD卡未找到", Toast.LENGTH_SHORT)
					.show();
			goToMainActivity();
		}

	}

	public void goToMainActivity() {
		Intent intent = new Intent(WelcomView.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
