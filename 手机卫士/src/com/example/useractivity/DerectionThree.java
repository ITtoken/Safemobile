package com.example.useractivity;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilesafe.R;
import com.example.utils.ExitDialogTip;
import com.example.utils.MD5Utils;
import com.example.utils.MUtils;
import com.example.utils.ShakeUtils;

public class DerectionThree extends BaseDerectActivity implements
		OnClickListener {
	private TextView perior_three;
	private TextView next_three;
	private TextView chose_contacter;
	private EditText et_safeNum;
	private TextView save_safenum;
	private String safeNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_derect_3);

		perior_three = (TextView) findViewById(R.id.perior_three);
		next_three = (TextView) findViewById(R.id.next_three);
		chose_contacter = (TextView) findViewById(R.id.chose_contanceter);
		et_safeNum = (EditText) findViewById(R.id.safe_num);
		save_safenum = (TextView) findViewById(R.id.save_safenum);

		perior_three.setOnClickListener(this);
		next_three.setOnClickListener(this);
		chose_contacter.setOnClickListener(this);
		save_safenum.setOnClickListener(this);

		safeNum = prefer.getString("safenum", null);
		if (!TextUtils.isEmpty(safeNum)) {
			et_safeNum.setHint("��ȫ����������(" + safeNum + ")");
			et_safeNum.setEnabled(false);
			save_safenum.setText("������ȫ����");
		}
	}

	@Override
	public void onBackPressed() {
		ExitDialogTip.alertExitTip(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.perior_three:
			showPrevPage(this, DerectionTwo.class);
			break;
		case R.id.next_three:
			String safeNum = prefer.getString("safenum", null);
			if (!TextUtils.isEmpty(safeNum)) {
				showNextPage(this, DerectionFour.class);
			} else {
				/* �����Ч�� */
				ShakeUtils.shakeAnim(this, et_safeNum);
				ShakeUtils.deviceShake(this);
				new MUtils(this).printToast("��ȫ����δ����");
			}

			break;
		case R.id.chose_contanceter:
			/* ������ϵ���б� */
			Intent intent = new Intent(this, ContactList.class);
			startActivityForResult(intent, 0);
			overridePendingTransition(R.anim.transeanim_left_coming,
					R.anim.transeanim_left_now);
			break;
		case R.id.save_safenum:
			/* �ı䰲ȫ����״̬����ΪFALSE����ʾΪ���ð�ȫ����״̬ */
			boolean changenum = prefer.getBoolean("changenum", false);
			String safenum = prefer.getString("safenum", null);
			if (changenum) {
				Random rand = new Random();
				StringBuffer sb = new StringBuffer();
				/* �������6������Ϊ��֤�뷢�͵���ȫ���룬ȷ�ϳɹ��ſ��Ը������� */
				for (int i = 0; i < 6; i++) {
					sb.append(rand.nextInt(10));
				}

				String validCode = sb.toString();
				new MUtils(this).sendSMS(safenum, "��֤�룺" + validCode);

				prefer.edit()
						.putString("validCode", MD5Utils.encodingMD5(validCode))
						.commit();

				new MUtils(this).printToast("��֤���ѷ��͵���ȫ���룬��ȷ����֤��");
				new MUtils(this).printAlert("��������֤��", "�����뷢�͵���ȫ�����ϵ���֤��",
						et_safeNum, save_safenum);
			} else {
				String etSafeNum = et_safeNum.getText().toString();
				if (!TextUtils.isEmpty(etSafeNum)) {
					if (!etSafeNum.matches("^\\d+$")) {
						/* �������ʽ����ȷʱ,��ʾ'��ʽ����' */
						ShakeUtils.shakeAnim(this, et_safeNum);
						ShakeUtils.deviceShake(this);
						new MUtils(this).printToast("��ȫ�����ʽ����");
					} else {
						prefer.edit().putString("safenum", etSafeNum).commit();
						et_safeNum.setEnabled(false);
						save_safenum.setText("������ȫ����");
						prefer.edit().putBoolean("changenum", true).commit();/* ״̬��Ϊ������ȫ���� */
						new MUtils(this).printToast("��ȫ�������óɹ�");
					}
				} else {
					ShakeUtils.shakeAnim(this, et_safeNum);
					ShakeUtils.deviceShake(this);
					new MUtils(this).printToast("�����밲ȫ����");
				}
			}
			break;
		}
	}

	@Override
	public void nextPage() {
		showNextPage(this, DerectionFour.class);
	}

	@Override
	public void prevPage() {
		showPrevPage(this, DerectionTwo.class);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String safeNum = prefer.getString("safenum", null);
		/* �����ȫ����������,����ʾ�������ظ����� */
		if (!TextUtils.isEmpty(safeNum)) {
			new MUtils(this).printToast("��ȫ����������,�����ظ�����");
		} else {
			if (data != null) {
				// �����ص������еĺ����ֶ�ȡ�������õ�EditText��
				String return_info = data.getStringExtra("return_info");
				String num = return_info
						.substring(return_info.indexOf(":") + 1);
				et_safeNum.setText(num);
			}

		}

	}
}
