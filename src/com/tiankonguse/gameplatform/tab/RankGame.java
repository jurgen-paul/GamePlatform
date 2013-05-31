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
import android.widget.Toast;

import com.tiankonguse.gameplatform.R;
import com.tiankonguse.gameplatform.db.MyGameDB;
import com.tiankonguse.gameplatform.db.SetGameDB;
import com.tiankonguse.gameplatform.listener.InstallListener;
import com.tiankonguse.gameplatform.net.DownloadFile;
import com.tiankonguse.gameplatform.net.FileUtils;
import com.tiankonguse.gameplatform.net.SystemDownload;

public class RankGame extends Activity {

	public static Context context;
	private static ListView gameRankListView;
	private static GameRankListAdapter gameRankListAdapter;
	private static ProgressBar progressBar;
	String s = null;
	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rank_main);

		context = this;

		progressBar = (ProgressBar) findViewById(R.id.rank_progress);
		textView = (TextView) findViewById(R.id.rank_text);

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				if (MyGameDB.getList(MyGameDB.GAME_RANK_NAME).isEmpty()) {

					SetGameDB.DownLoadRank(context);

					Log.i("RankGame", "class db is Empty");
				} else {
					Log.i("RankGame", "class db is exit");
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (!MyGameDB.getList(MyGameDB.GAME_RANK_NAME).isEmpty()) {
					gameRankListView = (ListView) findViewById(R.id.game_rank_lists);
					gameRankListView.setVisibility(View.VISIBLE);
					progressBar.setVisibility(View.GONE);
					textView.setVisibility(View.GONE);

					gameRankListAdapter = new GameRankListAdapter(context,
							MyGameDB.getList(MyGameDB.GAME_RANK_NAME));
					gameRankListView
							.setAdapter((ListAdapter) gameRankListAdapter);

					gameRankListView
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									HashMap<String, Object> map = MyGameDB
											.getList(MyGameDB.GAME_RANK_NAME)
											.get(position);
									Intent intent = new Intent();
									intent.putExtra("id", map.get("id")
											.toString());
									intent.putExtra("name", map.get("name")
											.toString());
									intent.setClass(context, game.class);
									context.startActivity(intent);
									// Toast.makeText(context,
									// position + " s",
									// Toast.LENGTH_SHORT).show();

								}
							});

				} else {
					Log.i("RankGame", "rank is null");
				}

				super.onPostExecute(result);
			}

		}.execute();

	}

	class GameRankListAdapter extends BaseAdapter {

		private class ButtonViewHolder {
			TextView name;
			ImageView img;
			RatingBar star;
			Button install;
		}

		private ArrayList<HashMap<String, Object>> list;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;

		public GameRankListAdapter(Context context,
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
					MyGameDB.GAME_RANK_NAME).get(position);
			if (map != null) {
				final String id = (String) map.get("id");
				final String name = (String) map.get("name");
				final String img = (String) map.get("img");
				String star = (String) map.get("star");
				final String apk = (String) map.get("apk");

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
//				myHolder.install.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						if(FileUtils.isFileExist("tiankonguse/game/apk/",id+".apk")){
//							 Toast.makeText(context,
//							name + "游戏已下载，是否安装",
//							 Toast.LENGTH_SHORT).show();
//						}else{
//							SystemDownload systemDownload = new SystemDownload(apk,"tiankonguse/game/apk/",id,context,name);
//							systemDownload.download();
//						}
//
//					}
//				});
				myHolder.name.setText(name);
			}

			return convertView;
		}

	}

}
