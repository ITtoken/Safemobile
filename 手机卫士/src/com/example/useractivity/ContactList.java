package com.example.useractivity;

import java.util.List;

import com.example.dao.DBDao;
import com.example.mobilesafe.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

		 DBDao.gteContacts(this);
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
			ViewHolder vh = null;
			if (convertView != null) {
				view = convertView;
				vh = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(ContactList.this, R.layout.item_listview,
						null);
				vh.tv_num = (TextView) view.findViewById(R.id.name);
				view.setTag(vh);
			}

			vh.tv_num = (TextView) view.findViewById(R.id.name);
			vh.tv_num.setText(datas.get(position));
			return view;
		}

		/**
		 * ViewHolder优化：创建一个ViewHolder内部类（类名随便取，习惯上命名为ViewHolder）
		 * 
		 * @author Administrator
		 *
		 */
		class ViewHolder {
			TextView tv_num;
		}

	}
}
