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
			et_safeNum.setHint("安全号码已设置(" + safeNum + ")");
			et_safeNum.setEnabled(false);
			save_safenum.setText("更换安全号码");
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
				/* 添加震动效果 */
				ShakeUtils.shakeAnim(this, et_safeNum);
				ShakeUtils.deviceShake(this);
				new MUtils(this).printToast("安全号码未设置");
			}

			break;
		case R.id.chose_contanceter:
			/* 启动联系人列表 */
			Intent intent = new Intent(this, ContactList.class);
			startActivityForResult(intent, 0);
			overridePendingTransition(R.anim.transeanim_left_coming,
					R.anim.transeanim_left_now);
			break;
		case R.id.save_safenum:
			/* 改变安全号码状态，若为FALSE，表示为设置安全号码状态 */
			boolean changenum = prefer.getBoolean("changenum", false);
			String safenum = prefer.getString("safenum", null);
			if (changenum) {
				Random rand = new Random();
				StringBuffer sb = new StringBuffer();
				/* 随机生成6个数作为验证码发送到安全号码，确认成功才可以更换号码 */
				for (int i = 0; i < 6; i++) {
					sb.append(rand.nextInt(10));
				}

				String validCode = sb.toString();
				new MUtils(this).sendSMS(safenum, "验证码：" + validCode);

				prefer.edit()
						.putString("validCode", MD5Utils.encodingMD5(validCode))
						.commit();

				new MUtils(this).printToast("验证码已发送到安全号码，请确认验证码");
				new MUtils(this).printAlert("请输入验证码", "请输入发送到安全号码上的验证码",
						et_safeNum, save_safenum);
			} else {
				String etSafeNum = et_safeNum.getText().toString();
				if (!TextUtils.isEmpty(etSafeNum)) {
					if (!etSafeNum.matches("^\\d+$")) {
						/* 当号码格式不正确时,提示'格式错误' */
						ShakeUtils.shakeAnim(this, et_safeNum);
						ShakeUtils.deviceShake(this);
						new MUtils(this).printToast("安全号码格式错误");
					} else {
						prefer.edit().putString("safenum", etSafeNum).commit();
						et_safeNum.setEnabled(false);
						save_safenum.setText("更换安全号码");
						prefer.edit().putBoolean("changenum", true).commit();/* 状态变为更换安全号码 */
						new MUtils(this).printToast("安全号码设置成功");
					}
				} else {
					ShakeUtils.shakeAnim(this, et_safeNum);
					ShakeUtils.deviceShake(this);
					new MUtils(this).printToast("请输入安全号码");
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
		/* 如果安全号码已设置,则提示不能在重复设置 */
		if (!TextUtils.isEmpty(safeNum)) {
			new MUtils(this).printToast("安全号码已设置,不能重复设置");
		} else {
			if (data != null) {
				// 将返回的数据中的号码字段取出并设置到EditText中
				String return_info = data.getStringExtra("return_info");
				String num = return_info
						.substring(return_info.indexOf(":") + 1);
				et_safeNum.setText(num);
			}

		}

	}
}
