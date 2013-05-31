package com.tiankonguse.gameplatform.tab;

import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tiankonguse.gameplatform.R;
import com.tiankonguse.gameplatform.db.GetGameDB;
import com.tiankonguse.gameplatform.db.SetGameDB;
import com.tiankonguse.gameplatform.listener.InstallListener;
import com.tiankonguse.gameplatform.net.DownloadFile;
import com.tiankonguse.gameplatform.net.FileUtils;

public class game extends Activity {

	public static Context context;
	TextView game_info;
	TextView game_time;
	TextView game_name;
	TextView game_head_name;
	ImageView game_return;
	ImageView game_img;
	RatingBar game_star;
	Button game_install;
	HashMap<String, Object> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		context = this;
		Intent intent = getIntent();
		final String id = intent.getStringExtra("id");
		final String name = intent.getStringExtra("name");

		game_name = (TextView) findViewById(R.id.game_name);
		game_info = (TextView) findViewById(R.id.game_info);
		game_time = (TextView) findViewById(R.id.game_time);
		game_head_name = (TextView) findViewById(R.id.game_head_name);
		game_return = (ImageView) findViewById(R.id.game_return);
		game_img = (ImageView) findViewById(R.id.game_img);
		game_star = (RatingBar) findViewById(R.id.game_star);
		game_install = (Button) findViewById(R.id.game_install);

		game_name.setText(name);
		game_head_name.setText(name);

		map = null;
		
		game_return.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
		
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {

				map = GetGameDB.getGameInfo(getApplicationContext(), id);

				if (map == null || map.get("info") == null) {
					SetGameDB.DownLoadGame(context, id);
				}

				return null;
			}
			protected void onPostExecute(Void result) {
				map = GetGameDB.getGameInfo(getApplicationContext(), id);
				
				
				Log.i("game", map.toString());
				
				if (map != null) {
					final String id = getString("id");
					String name = getString("name");
					final String img = getString("img");
					String star = getString("star");
					String apk = getString("apk");
					String size = getString("size");
					String info = getString("info");
					String time = getString("time");
					
					game_install.setOnClickListener(
							new InstallListener(id, name, context, apk));
					
					final String imgType = img.substring(img.length() - 4);

					if (FileUtils
							.isFileExist("tiankonguse/game/img/", id + imgType)) {
						Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getPath("tiankonguse/game/img/", id + imgType));
						game_img.setImageBitmap(bitmap);
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
									game_img.setImageBitmap(bitmap);
								}
								super.onPostExecute(result);
							}

						}.execute();

						
					}
					
					game_star.setRating((float) Float.parseFloat(star) / 2);
					game_time.setText(time);
					game_info.setText(info);
				}
			}
		}.execute();
		

		
	}
	
	String getString(String s){
		String string = null;
		
		if(map != null && s != null &&  map.get(s) != null){
			string = map.get(s).toString();
		}else{
			string = "";
		}
		
		return string;
	}
	
}
