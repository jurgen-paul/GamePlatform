package com.tiankonguse.gameplatform.tab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.tiankonguse.gameplatform.db.SetGameDB;
import com.tiankonguse.gameplatform.manger.Unstall;
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
				GetGameDB.UpdateInstallGameList(context);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				
				List<HashMap<String, Object>> list = MyGameDB
						.getList(MyGameDB.GAME_INSTALL_LIST);
				
				if (!list.isEmpty()) {
					ListView = (ListView) findViewById(R.id.game_manger_lists);
					ProgressBar manger_progress = (ProgressBar) findViewById(R.id.manger_progress);
					ListView.setVisibility(View.VISIBLE);
					manger_progress.setVisibility(View.GONE);
					listAdapter = new ListAdapter(context, list);
					ListView.setAdapter((ListAdapter) listAdapter);
				}
			}
		}.execute();

	}

	@Override
	protected void onResume() {
		GetGameDB.UpdateInstallGameList(context);
		updateMyList();
		super.onResume();
	}
	
	
	


	@Override
	protected void onRestart() {
		GetGameDB.UpdateInstallGameList(context);
		updateMyList();
		super.onRestart();
	}





	class ListAdapter extends BaseAdapter {
		private class ButtonViewHolder {
			TextView name;
			TextView pak;
			ImageView icon;
			Button unstall;
			int state;
		}

		private List<HashMap<String, Object>> list;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;

		public ListAdapter(Context context, List<HashMap<String, Object>> list) {
			this.list = (List<HashMap<String, Object>>) list;
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
		
		public void removeItem(int position){
			SetGameDB.deleteItemFromList(
					getApplicationContext()
					,MyGameDB.getList(MyGameDB.GAME_INSTALL_LIST).get(position)
					,MyGameDB.GAME_INSTALL_LIST
					);
			this.notifyDataSetChanged();
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
			
			HashMap<String, Object>map = MyGameDB
					.getList(MyGameDB.GAME_INSTALL_LIST).get(position);
			
			if (map != null) {
				final int _position = position;
				final String packageName = (String) map.get("packageName");
				 myHolder.icon.setImageDrawable((Drawable) map.get("icon"));
				 myHolder.name.setText((String) map.get("name"));
				 myHolder.pak.setText("更新日期:"+getDate(map.get("lastUpdateTime").toString()));
				 myHolder.unstall.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Unstall.unstall(context, packageName);	
						removeItem(_position);
					}
				});
			}
			return convertView;
		}
	}
	public static void updateMyList(){
		listAdapter.notifyDataSetChanged();
	}
	String getDate(String time){
		Date date = new Date(Long.parseLong(time));
		int year  = date.getYear() + 1900;
		int month = date.getMonth() ;
		int day = date.getDay() + 1;
				
		return year + "-" + month + "-" + day + " ";
	}
	
}
