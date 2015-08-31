package com.example.splash;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
	private String vn;// ���ػ�ȡ�İ汾��
	private int vc;// ���ػ�ȡ�İ汾��

	// �����ȡ�汾����
	private String versionName;
	private int versionCode;
	private String description;
	private String downloadURL;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NO_UPDATE:// �������°汾��������£�ֱ�ӽ��������棩
				goToMainActivity();
				break;
			case UPDATE_DIALOG:
				// �������¶Ի���
				showUpdateDialog();
				break;
			case URL_EXCEPTION:// url�쳣��ʾ
				Toast.makeText(WelcomView.this, "������Դ������", Toast.LENGTH_SHORT)
						.show();
				break;
			case INTERNET_EXCEPTION:// �����쳣��ʾ
				Toast.makeText(WelcomView.this, "���������쳣", Toast.LENGTH_SHORT)
						.show();
				break;
			case JSON_EXCEPTION:// ��ȡjson�����쳣
				Toast.makeText(WelcomView.this, "��������ʧ��", Toast.LENGTH_SHORT)
						.show();
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
		tv_welcome.setText("�汾�ţ�" + getVersionName());
		tv_downStat = (TextView) findViewById(R.id.tv_downstat);

		getUpdateInfo();
	}

	/**
	 * ͨ�������ȡ�汾��Ϣ
	 */
	private void getUpdateInfo() {
		new Thread() {
			private Message msg;
			private HttpURLConnection conn;

			public void run() {
				try {
					msg = handler.obtainMessage();

					URL url = new URL("http://10.100.0.173/update.json");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					conn.setReadTimeout(5000);

					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();

						String content = In2Out.getInputstreamInfo(in);
						// ����ȡ������ת����JSOn����JsonObject��
						JSONObject json = new JSONObject(content);
						versionCode = (Integer) json.get("versionCode");// ��ȺŹ�汾��

						if (versionCode > vc) {
							versionName = (String) json.get("versionName");// ��ȡ�汾��
							description = (String) json.get("description");// ��ȡ�°汾����
							downloadURL = (String) json.get("downloadURL");// ��ȡ�°汾����URL
							System.out.println("�汾��:" + versionName + ",�汾��:"
									+ versionCode + ",����:" + description
									+ ",��������:" + downloadURL);

							msg.what = UPDATE_DIALOG;
						} else {
							msg.what = NO_UPDATE;
						}

					}
				} catch (MalformedURLException e) {
					// url�쳣
					msg.what = URL_EXCEPTION;
					e.printStackTrace();
				} catch (IOException e) {
					// ���������쳣
					msg.what = INTERNET_EXCEPTION;
					e.printStackTrace();
				} catch (JSONException e) {
					// json���ݻ�ȡ�쳣
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
	 * ��ȡ�汾��
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
	 * ����������ʾ�Ի���
	 */
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("�°汾������ʾ(" + versionName + ")");
		builder.setMessage(description);
		builder.setPositiveButton("ȷ������", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downLoadApp();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				goToMainActivity();
			}
		});
		builder.show();
	};

	/**
	 * ����APP
	 */
	public void downLoadApp() {
		if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
			HttpUtils utils = new HttpUtils();
			String target = Environment.getExternalStorageDirectory()
					+ "/MobileSafe.apk";
			utils.download("http://10.100.0.173/MobileSafe.apk", target, true,
					new RequestCallBack<File>() {

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							super.onLoading(total, current, isUploading);

							tv_downStat.setVisibility(View.VISIBLE);// ����Ϊ�ɼ�
							tv_downStat.setText("���ؽ��ȣ�" + current * 100 / total
									+ "%");
						}

						@Override
						public void onSuccess(ResponseInfo<File> arg0) {
							// tv_downStat.setVisibility(View.GONE);// ����Ϊ���ɼ�
							Toast.makeText(WelcomView.this, "���سɹ�",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {

						}
					});
		} else {
			Toast.makeText(WelcomView.this, "SD��δ�ҵ�", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void goToMainActivity() {
		Intent intent = new Intent(WelcomView.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
