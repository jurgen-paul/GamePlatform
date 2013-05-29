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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tiankonguse.gameplatform.R;
import com.tiankonguse.gameplatform.db.MyGameDB;
import com.tiankonguse.gameplatform.db.SetGameDB;

public class ListGame  extends Activity{
	
	public static Context context ;
	private static ListView gameListView;
	private static gameListAdapter gameListAdapter; 
	private static ProgressBar progressBar;
	String s = null;
	TextView textView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list_main);
		
		context = this;
		
		progressBar = (ProgressBar) findViewById(R.id.list_progress);
		textView = (TextView) findViewById(R.id.list_text);
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if(MyGameDB.getList(MyGameDB.GAME_RANK_NAME).isEmpty()){
					
					SetGameDB.DownLoadRank(context);
					
					Log.i("ListGame", "class db is Empty");
				}else{
					Log.i("ListGame", "class db is exit");
				}

				return null;
			}
			@Override
			protected void onPostExecute(Void result) {
				if(!MyGameDB.getList(MyGameDB.GAME_RANK_NAME).isEmpty()){
					gameListView = (ListView) findViewById(R.id.game_lists);
					gameListView.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					textView.setVisibility(View.GONE);

					gameListAdapter = new gameListAdapter(context, MyGameDB.getList(MyGameDB.GAME_RANK_NAME));
					gameListView.setAdapter((ListAdapter) gameListAdapter);
				}else{
					Log.i("ListGame", "list is null");
				}

				super.onPostExecute(result);
			}
			
		}.execute();
		
	}
	
	class gameListAdapter extends BaseAdapter{
		
		private class ButtonViewHolder{
			TextView    name;
			ImageView img;
			RatingBar star;
			Button install;
		}

		
		private ArrayList<HashMap<String, Object>>list;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;
		
		public gameListAdapter(Context context,List<HashMap<String, Object>> list) {
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
				convertView = myInflater.inflate(R.layout.game_item, null);
				this.myHolder = new ButtonViewHolder();
				this.myHolder.name = (TextView)convertView.findViewById(R.id.game_item_name);
				this.myHolder.img = (ImageView)convertView.findViewById(R.id.game_item_img);
				this.myHolder.install = (Button)convertView.findViewById(R.id.game_item_install);
				this.myHolder.star = (RatingBar)convertView.findViewById(R.id.game_item_star);
				convertView.setTag(myHolder);
			}
			
			HashMap<String, Object>map = MyGameDB.getList(MyGameDB.GAME_RANK_NAME).get(position);
			if(map != null){
				String name =  (String) map.get("name");
				String img =  (String) map.get("img");
				String star =  (String) map.get("star");
				String install =  (String) map.get("install");
//				myHolder.img.set
				myHolder.star.setRating((float) Float.parseFloat(star)/2);
				myHolder.star.setOnRatingBarChangeListener(null);
				myHolder.name.setText(name);
			}
			
			return convertView;
		}

	
	}

}
