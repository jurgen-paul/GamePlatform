package com.tiankonguse.gameplatform.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tiankonguse.gameplatform.R;
import com.tiankonguse.gameplatform.db.MyGameDB;
import com.tiankonguse.gameplatform.db.SetGameDB;

//ExpandableList
public class ClassGame extends Activity{
	
	public static Context context ;
	private static ListView gameClassListView;
	private static GameClassListAdapter gameClassListAdapter; 
	private static ProgressBar progressBar;
	String s = null;
	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.class_main);
		
		context = this;
		
		progressBar = (ProgressBar) findViewById(R.id.class_progress);
		textView = (TextView) findViewById(R.id.class_text);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
				
				if(MyGameDB.getList(MyGameDB.GAME_CLASS_NAME).isEmpty()){
					
					SetGameDB.DownLoadClass(context);
					
					Log.i("ClassGame", "class db is Empty");
				}else{
					Log.i("ClassGame", "class db is exit");
				}

				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				
				if(!MyGameDB.getList(MyGameDB.GAME_CLASS_NAME).isEmpty()){
					gameClassListView = (ListView) findViewById(R.id.game_class_lists);
					gameClassListView.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					textView.setVisibility(View.GONE);

					gameClassListAdapter = new GameClassListAdapter(context, MyGameDB.getList(MyGameDB.GAME_CLASS_NAME));
					gameClassListView.setAdapter((ListAdapter) gameClassListAdapter);
				}else{
					Log.i("ClassGame", "class is null");
				}

				super.onPostExecute(result);
			}
			
		}.execute();
	}
	
	class GameClassListAdapter extends BaseAdapter{
		
		private class ButtonViewHolder{
			public TextView    gameClassName;
		}

		
		private ArrayList<HashMap<String, Object>>list;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;
		
		public GameClassListAdapter(Context context,List<HashMap<String, Object>> list) {
			this.list = (ArrayList<HashMap<String, Object>>)list;
			this.myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView != null){
				this.myHolder = (ButtonViewHolder)convertView.getTag();
			}else{
				convertView = myInflater.inflate(R.layout.class_main_item, null);
				this.myHolder = new ButtonViewHolder();
				this.myHolder.gameClassName = (TextView)convertView.findViewById(R.id.game_class_list_item);
				convertView.setTag(myHolder);
			}
			
			HashMap<String, Object>map = MyGameDB.getList(MyGameDB.GAME_CLASS_NAME).get(position);
			if(map != null){
				String name =  (String) map.get("name");
				
				myHolder.gameClassName.setText(name);
			}
			
			return convertView;
		}

	
	}
	

	
}
