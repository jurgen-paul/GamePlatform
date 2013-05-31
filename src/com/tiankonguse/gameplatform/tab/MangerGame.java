package com.tiankonguse.gameplatform.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tiankonguse.gameplatform.R;
import com.tiankonguse.gameplatform.db.GetGameDB;
import com.tiankonguse.gameplatform.db.MyGameDB;
import com.tiankonguse.gameplatform.struct.AppInfo;

public class MangerGame extends Activity {

	public static Context context;
	private static ListView ListView;
	List<AppInfo> items = new ArrayList<AppInfo>();
	private static ListAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.manger_main);

		context = this;
		Log.i("MangerGame", "0");
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				PackageManager pm = getPackageManager();
				Log.i("MangerGame", "1");
				// 得到PackageManager对象
				List<PackageInfo> packs = pm.getInstalledPackages(0);
				// 得到系统 安装的所有程序包的PackageInfo对象
				Log.i("MangerGame", "2");
				for (PackageInfo pi : packs) {
					String appName = pi.applicationInfo.loadLabel(pm)
							.toString();
					if (GetGameDB.checkIsGame(context, appName)) {

						AppInfo appInfo = new AppInfo();
						// 应用名
						appInfo.setAppname(appName);

						appInfo.setPackagename(pi.packageName);

						appInfo.setVersionCode(pi.versionCode);

						appInfo.setAppicon(pi.applicationInfo.loadIcon(pm));

						items.add(appInfo);

					}

				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (!items.isEmpty()) {
					ListView = (ListView) findViewById(R.id.game_manger_lists);
					ProgressBar manger_progress = (ProgressBar) findViewById(R.id.manger_progress);
					ListView.setVisibility(View.VISIBLE);
					manger_progress.setVisibility(View.GONE);

					listAdapter = new ListAdapter(context, items);

					ListView.setAdapter((ListAdapter) listAdapter);

				}
			}
		}.execute();

	}

	class ListAdapter extends BaseAdapter {
		private class ButtonViewHolder {
			TextView name;
			TextView pak;
			ImageView icon;
			Button unstall;
			int state;
		}

		private ArrayList<AppInfo> list;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;

		public ListAdapter(Context context, List<AppInfo> list) {
			this.list = (ArrayList<AppInfo>) list;
			this.myInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView != null) {
				this.myHolder = (ButtonViewHolder) convertView.getTag();
			} else {
				convertView = myInflater.inflate(R.layout.manger_item, null);
				this.myHolder = new ButtonViewHolder();
				this.myHolder.name = (TextView) convertView
						.findViewById(R.id.manger_item_name);
				this.myHolder.icon = (ImageView) convertView
						.findViewById(R.id.manger_item_img);
				this.myHolder.pak = (TextView) convertView
						.findViewById(R.id.manger_item_pak);
				this.myHolder.unstall = (Button) convertView
						.findViewById(R.id.manger_item_unstall);

				convertView.setTag(myHolder);
			}
			
			AppInfo map = items.get(position);
			if (map != null) {
				 myHolder.icon.setImageDrawable(map.getAppicon());
				 myHolder.name.setText(map.getAppname());
				 myHolder.pak.setText(map.getPackagename());
			}
			return convertView;
		}

	}

}
