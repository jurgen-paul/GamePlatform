package com.tiankonguse.gameplatform.tab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tiankonguse.gameplatform.R;
import com.tiankonguse.gameplatform.db.GetGameDB;
import com.tiankonguse.gameplatform.db.MyGameDB;
import com.tiankonguse.gameplatform.db.SetGameDB;
import com.tiankonguse.gameplatform.listener.InstallListener;
import com.tiankonguse.gameplatform.net.DownloadFile;
import com.tiankonguse.gameplatform.net.FileUtils;


public class ListGame extends Activity {

	public static Context context;
	private static ListView gameListView;
	private static gameListAdapter gameListAdapter;
	private static ProgressBar progressBar;
	String s = null;
	TextView textView;
	TextView class_name;
	ImageView list_return;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_main);

		context = this;

		progressBar = (ProgressBar) findViewById(R.id.list_progress);
		textView = (TextView) findViewById(R.id.list_text);
		class_name = (TextView) findViewById(R.id.class_name);
		list_return = (ImageView) findViewById(R.id.list_return);

		Intent intent = getIntent();
		final String id = intent.getStringExtra("id");
		final String name = intent.getStringExtra("name");

		class_name.setText(name);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				MyGameDB.setGameNowName(name, id);

				GetGameDB.getGameList(getApplicationContext());

				if (MyGameDB.getList(MyGameDB.GAME_NOWLIST_NAME).isEmpty()) {

					SetGameDB.DownLoadList(context);

					Log.i("ListGame", "list db is Empty");
				} else {
					Log.i("ListGame", "list db is exit");
				}
				
				Log.i("ListGame", "id = " + id + " name = " + name);

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (!MyGameDB.getList(MyGameDB.GAME_NOWLIST_NAME).isEmpty()) {
					gameListView = (ListView) findViewById(R.id.game_lists);
					gameListView.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					textView.setVisibility(View.GONE);

					gameListAdapter = new gameListAdapter(context,
							MyGameDB.getList(MyGameDB.GAME_NOWLIST_NAME));
					
					gameListView.setAdapter((ListAdapter) gameListAdapter);
					
					gameListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							HashMap<String, Object> map = MyGameDB
									.getList(MyGameDB.GAME_NOWLIST_NAME)
									.get(position);
							Intent intent = new Intent();
							intent.putExtra("id", map.get("id").toString());
							intent.putExtra("name", map.get("name").toString());
							intent.setClass(context,
									game.class);
							context.startActivity(intent);
//							Toast.makeText(context,
//									map.get("name").toString(),
//									Toast.LENGTH_SHORT).show();
						}
					});
					
				} else {
					Log.i("ListGame", "list is null");
				}

				super.onPostExecute(result);
			}

		}.execute();

		list_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
				// onKeyDown(KeyEvent.KEYCODE_BACK, null);
			}
		});

	}

	class gameListAdapter extends BaseAdapter {

		private class ButtonViewHolder {
			TextView name;
			ImageView img;
			RatingBar star;
			Button install;
		}

		private ArrayList<HashMap<String, Object>> list;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;

		public gameListAdapter(Context context,
				List<HashMap<String, Object>> list) {
			this.list = (ArrayList<HashMap<String, Object>>) list;
			this.myInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			if (convertView != null) {
				this.myHolder = (ButtonViewHolder) convertView.getTag();
			} else {
				convertView = myInflater.inflate(R.layout.list_game_item, null);
				this.myHolder = new ButtonViewHolder();
				this.myHolder.name = (TextView) convertView
						.findViewById(R.id.list_game_item_name);
				this.myHolder.img = (ImageView) convertView
						.findViewById(R.id.list_game_item_img);
				this.myHolder.install = (Button) convertView
						.findViewById(R.id.list_game_item_install);
				this.myHolder.star = (RatingBar) convertView
						.findViewById(R.id.list_game_item_star);
				convertView.setTag(myHolder);
			}

			HashMap<String, Object> map = MyGameDB.getList(
					MyGameDB.GAME_NOWLIST_NAME).get(position);
			if (map != null) {
				final String id = (String) map.get("id");
				String name = (String) map.get("name");
				final String img = (String) map.get("img");
				String star = (String) map.get("star");
				String apk = (String) map.get("apk");
				
				final String imgType = img.substring(img.length() - 4);

				if (FileUtils
						.isFileExist("tiankonguse/game/img/", id + imgType)) {
					Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getPath("tiankonguse/game/img/", id + imgType));
					this.myHolder.img.setImageBitmap(bitmap);
				} else {					
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							
							DownloadFile downloadFile = new DownloadFile();
							try {
								downloadFile.down_file(img,
										"tiankonguse/game/img/", "" + id
												+ imgType);
							} catch (IOException e) {
								e.printStackTrace();
							}
							return null;
						}

						protected void onPostExecute(Void result) {
							if (FileUtils.isFileExist("tiankonguse/game/img/",
									id + imgType)) {
								Log.i("RankGame", id + " " + imgType);
								Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getPath("tiankonguse/game/img/", id + imgType));
								myHolder.img.setImageBitmap(bitmap);
							}
							super.onPostExecute(result);
						}

					}.execute();

					
				}
				
				// myHolder.img.set
				myHolder.star.setRating((float) Float.parseFloat(star) / 2);
				myHolder.star.setOnRatingBarChangeListener(null);
				myHolder.install.setOnClickListener(
						new InstallListener(id, name, context, apk));
				myHolder.name.setText(name);
			}

			return convertView;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

}
