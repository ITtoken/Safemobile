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
	private String vn;// ���ػ�ȡ�İ汾��
	private int vc;// ���ػ�ȡ�İ汾��
	private String aPPName;// ��ȡӦ������

	// �����ȡ�汾����
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
			case AppConst.NO_UPDATE:// �������°汾��������£�ֱ�ӽ��������棩
				goToMainActivity();
				break;
			case AppConst.UPDATE_DIALOG:// �������¶Ի���
				showUpdateDialog();
				break;
			case AppConst.URL_EXCEPTION:// url�쳣��ʾ
				Toast.makeText(SplashActivity.this, "������Դ������",
						Toast.LENGTH_SHORT).show();
				goToMainActivity();
				break;
			case AppConst.INTERNET_EXCEPTION:// �����쳣��ʾ
				Toast.makeText(SplashActivity.this, "���������쳣",
						Toast.LENGTH_SHORT).show();
				goToMainActivity();
				break;
			case AppConst.JSON_EXCEPTION:// ��ȡjson�����쳣
				Toast.makeText(SplashActivity.this, "��������ʧ��",
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

		loadLocationLib();/* ������������ݿ� */
		pref = FileInstance.getPref(this);
		tv_welcome = (TextView) findViewById(R.id.tv_welcome);
		tv_welcome.setText("�汾�ţ�" + getVersionName());
		tv_downStat = (TextView) findViewById(R.id.tv_downstat);
		rl_anim = (RelativeLayout) findViewById(R.id.rl_anim);
		rl_anim.setBackgroundResource(R.drawable.splashanim);
		ad = (AnimationDrawable) rl_anim.getBackground();
		ad.start();

		boolean stat = pref.getBoolean("stat", true);
		if (stat) {
			getUpdateInfo();// ������APP������
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
	 * ���������ʾ�����Ƿ���
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
	 * ͨ�������ȡ�汾��Ϣ
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

						// ����ȡ������ת����JSOn����JsonObject��
						JSONObject json = new JSONObject(content);
						versionCode = (Integer) json.get("versionCode");// ��ȡ�汾��

						if (versionCode > vc) {
							versionName = (String) json.get("versionName");// ��ȡ�汾��
							description = (String) json.get("description");// ��ȡ�°汾����
							downloadURL = (String) json.get("downloadURL");// ��ȡ�°汾����URL
							aPPName = (String) json.get("APPName");

							msg.what = AppConst.UPDATE_DIALOG;
						} else {
							Thread.sleep(2000);
							msg.what = AppConst.NO_UPDATE;
						}
					}
				} catch (MalformedURLException e1) {
					// url�쳣
					msg.what = AppConst.URL_EXCEPTION;
					e1.printStackTrace();
				} catch (IOException e) {
					// ���������쳣
					msg.what = AppConst.INTERNET_EXCEPTION;
					e.printStackTrace();
				} catch (JSONException e11) {
					// json���ݻ�ȡ�쳣
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
		builder.setIcon(R.drawable.update_tip);
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
		builder.setOnCancelListener(new OnCancelListener() {
			/**
			 * ���û�������ؼ�����ȡ���Ի�����ʾʱ������ô˼����˷���
			 */
			@Override
			public void onCancel(DialogInterface dialog) {
				goToMainActivity();
			}
		});
		builder.show();

	};

	/**
	 * ����APP
	 */
	public void downLoadApp() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/" + aPPName);
			// ��������Ѿ������غõ��ļ�,��ֱ��ʹ�ð�װ
			if (file.exists()) {
				// ������װ����
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				startActivityForResult(intent, 0);
			} else {
				// ����ӷ�������ȡ
				HttpUtils utils = new HttpUtils();
				String target = Environment.getExternalStorageDirectory() + "/"
						+ aPPName;
				utils.download(downloadURL, target, true, true,
						new RequestCallBack<File>() {

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								super.onLoading(total, current, isUploading);

								tv_downStat.setVisibility(View.VISIBLE);// ����Ϊ�ɼ�
								tv_downStat.setText("���ؽ��ȣ�" + current * 100
										/ total + "%");
							}

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								// tv_downStat.setVisibility(View.GONE);//
								// ����Ϊ���ɼ�
								Toast.makeText(SplashActivity.this, "���سɹ�",
										Toast.LENGTH_SHORT).show();
								// ������װ����
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
			Toast.makeText(SplashActivity.this, "SD��δ�ҵ�", Toast.LENGTH_SHORT)
					.show();
			goToMainActivity();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// ����û��ڰ�װ����ȡ����װ�����غ�ֱ�ӽ���������
		goToMainActivity();
	}

	/**
	 * ����������
	 */
	public void goToMainActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * ���ع��������ݿ�
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
