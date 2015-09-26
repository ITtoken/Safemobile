package com.example.useractivity;

import java.util.List;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dao.DBOperater;
import com.example.mobilesafe.R;
import com.example.utils.MUtils;
import com.example.utils.ShakeUtils;

public class BlackContacterActivity extends Activity implements OnClickListener {
	private ListView lv_bc;
	private Button btn_addtobc;
	private Button chose_contacter;
	private List<String> bc_contacters;
	private EditText et_telenum;
	private DBOperater operater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_callsafe);

		operater = new DBOperater(this);/* ��ʼ�����ݿ� */
		bc_contacters = operater.query();/* ��ʼ����ϵ�˴���б� */

		btn_addtobc = (Button) findViewById(R.id.add_blackcontacter);
		btn_addtobc.setOnClickListener(this);
		chose_contacter = (Button) findViewById(R.id.chose_contacter);
		chose_contacter.setOnClickListener(this);
		et_telenum = (EditText) findViewById(R.id.et_telenum);
		lv_bc = (ListView) findViewById(R.id.lv_bc);
		lv_bc.setAdapter(new BcAdapter());
	}

	class BcAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (bc_contacters.size() > 0) {
				return bc_contacters.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		class ViewHolder {
			Button remove_bc;
			TextView tv;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			ViewHolder vh = new ViewHolder();
			if (convertView != null) {
				view = convertView;
				vh = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(BlackContacterActivity.this,
						R.layout.item_bc, null);
				vh.remove_bc = (Button) view.findViewById(R.id.remove_bc);
				vh.tv = (TextView) view.findViewById(R.id.telnum);
				view.setTag(vh);
			}

			vh.tv.setText(bc_contacters.get(position));
			vh.remove_bc.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/* �Ӻ��������Ƴ� */
					/* ��Ϊ���ݿ�Ĭ�ϴ�0��ʼ��������list�Ǵ�0��ʼ�ģ�����+1 */
					operater.delete(position + 1);
					bc_contacters.remove(position);
					operater.update(bc_contacters);
					new MUtils(BlackContacterActivity.this).printToast("ɾ���ɹ�");
					notifyDataSetChanged();
				}
			});
			return view;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_blackcontacter:
			final String telenum = et_telenum.getText().toString();
			if (!TextUtils.isEmpty(telenum)) {
				boolean exist = operater.queryFromTelnum(telenum);
				if (exist) {
					ShakeUtils.shakeAnim(this, et_telenum);
					ShakeUtils.deviceShake(this);
					new MUtils(BlackContacterActivity.this)
							.printToast("���û��Ѵ��ں�������");
				} else {
					operater.insert(telenum);
					new MUtils(BlackContacterActivity.this).printToast("��ӳɹ�");
					finish();
				}
			} else {
				/* �����Ч�� */
				ShakeUtils.shakeAnim(this, et_telenum);
				ShakeUtils.deviceShake(this);
				new MUtils(this).printToast("�������Ϊ��");
			}
			break;
		case R.id.chose_contacter:
			new MUtils(this).printToast("ѡ����ϵ��");
			break;
		}
	}
}
