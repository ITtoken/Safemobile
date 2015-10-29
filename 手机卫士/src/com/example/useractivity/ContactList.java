package com.example.useractivity;

import java.util.List;

import com.example.mobilesafe.R;
import com.example.utils.DBContactsUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ContactList extends Activity {
	private ListView contact_list;
	private List<String> datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacters);

		contact_list = (ListView) findViewById(R.id.contact_list);
		contact_list.setAdapter(new ContectAdapter());
		contact_list.setOnItemClickListener(new ItemListener());

		datas = DBContactsUtils.gteContacts(this);
	}

	class ItemListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent data = new Intent();
			data.putExtra("return_info", datas.get(position));
			setResult(0, data);
			finish();
		}

	}

	class ContectAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (datas != null && datas.size() > 0) {
				return datas.size();
			} else {
				return 1;
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder vh = new ViewHolder();
			if (convertView != null) {
				view = convertView;
				vh = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(ContactList.this, R.layout.item_listview,
						null);
				vh.tv_name = (TextView) view.findViewById(R.id.name);
				vh.phone_num = (TextView) view.findViewById(R.id.phone_num);
				view.setTag(vh);
			}
			String[] content = datas.get(position).split(":");
			vh.tv_name.setText(content[0]);
			vh.phone_num.setText(content[1]);
			return view;
		}

		/**
		 * ViewHolder优化：创建一个ViewHolder内部类（类名随便取，习惯上命名为ViewHolder）
		 * 
		 * @author Administrator
		 *
		 */
		class ViewHolder {
			public TextView phone_num;
			public TextView tv_name;
		}

	}

}
