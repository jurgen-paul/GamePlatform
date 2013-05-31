package com.tiankonguse.gameplatform.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.R.bool;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * 
 * get data from database.
 * 
 */
public class GetGameDB {

	/**
	 * 
	 * software start ,it would be called.
	 * 
	 * this function will create the database and tables.
	 * 
	 */
	public static void LoadGameDB(ContentResolver resolver, Context context) {

		SetGameDB.CreateTable(context);
		getGameRankList(context);
		getGameClassList(context);

	}

	/**
	 * load the list of class class is TABLE_CLASS
	 */
	public static void getGameClassList(Context context) {

		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_CLASS_NAME);

		if (list == null) {
			Log.i("GetGameDB", "GAME_CLASS_NAME list is null");
			return;
		}

		if (!list.isEmpty()) {
			list.clear();
		}

		String sql = "select * from " + MyGameDB.TABLE_CLASS;
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex("id"));
			if ("102".equals(id)) {
				continue;
			}
			String name = cursor.getString(cursor.getColumnIndex("name"));
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("id", id);
			list.add(map);
		}

		Log.i("GetGameDB", "class list's lenght is " + list.size());

		cursor.close();

		db.close();

	}

	/**
	 * load the list of class class is TABLE_CLASS
	 */
	public static void getGameRankList(Context context) {

		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_RANK_NAME);

		if (list == null) {
			Log.i("GetGameDB", "GAME_RANK_NAME list is null");
			return;
		}

		if (!list.isEmpty()) {
			list.clear();
		}

		List<String> gameList = new ArrayList<String>();

		String sql = "select * from " + MyGameDB.TABLE_MAP
				+ " where class_id = '102'";

		Log.i("GetGameDB", "sql :" + sql);

		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			String game_id = cursor.getString(cursor.getColumnIndex("game_id"));
			gameList.add(game_id);
		}
		

		for (String game : gameList) {
			sql = "select * from " + MyGameDB.TABLE_GAME + " where id = '"
					+ game + "'";

			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String apk = cursor.getString(cursor.getColumnIndex("apk"));
				String img = cursor.getString(cursor.getColumnIndex("img"));
				String star = cursor.getString(cursor.getColumnIndex("star"));
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("name", name);
				map.put("apk", apk);
				map.put("img", img);
				map.put("star", star);
				list.add(map);
			}

		}

		Log.i("GetGameDB", "rank list's lenght is " + list.size());

		cursor.close();

		db.close();

		Collections.sort(list, new Comparator<HashMap<String, Object>>() {
			public int compare(HashMap<String, Object> first,
					HashMap<String, Object> second) {
				return Integer.parseInt(second.get("star").toString())
						- Integer.parseInt(first.get("star").toString());
			}
		});

	}

	/**
	 * load the list of class class is game list
	 */
	public static void getGameList(Context context) {
		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_NOWLIST_NAME);

		if (list == null) {
			Log.i("GetGameDB", "GAME_NOW_NAME list is null");
			return;
		}

		if (!list.isEmpty()) {
			list.clear();
		}

		List<String> gameList = new ArrayList<String>();

		String sql = "select * from " + MyGameDB.TABLE_MAP
				+ " where class_id = '" + MyGameDB.GAME_NOWLIST_ID + "'";

		Log.i("GetGameDB", "sql :" + sql);

		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		while (cursor.moveToNext()) {
			String game_id = cursor.getString(cursor.getColumnIndex("game_id"));
			gameList.add(game_id);
		}

		for (String game : gameList) {
			sql = "select * from " + MyGameDB.TABLE_GAME + " where id = '"
					+ game + "'";

			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				String id = cursor.getString(cursor.getColumnIndex("id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String apk = cursor.getString(cursor.getColumnIndex("apk"));
				String img = cursor.getString(cursor.getColumnIndex("img"));
				String star = cursor.getString(cursor.getColumnIndex("star"));
				String time = cursor.getString(cursor.getColumnIndex("time"));
				String info = cursor.getString(cursor.getColumnIndex("info"));
				String size = cursor.getString(cursor.getColumnIndex("size"));

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", id);
				map.put("name", name);
				map.put("apk", apk);
				map.put("img", img);
				map.put("star", star);
				map.put("time", time);
				map.put("info", info);
				map.put("size", size);
				list.add(map);
			}

		}

		Log.i("GetGameDB", "now game list's lenght is " + list.size());

		cursor.close();

		db.close();
	}

	public static HashMap<String, Object> getGameInfo(Context context, String id) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String sql = "select * from " + MyGameDB.TABLE_GAME + " where id = '"
				+ id + "'";
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String apk = cursor.getString(cursor.getColumnIndex("apk"));
			String img = cursor.getString(cursor.getColumnIndex("img"));
			String star = cursor.getString(cursor.getColumnIndex("star"));
			String time = cursor.getString(cursor.getColumnIndex("time"));
			String info = cursor.getString(cursor.getColumnIndex("info"));
			String size = cursor.getString(cursor.getColumnIndex("size"));

			map.put("id", id);
			map.put("name", name);
			map.put("apk", apk);
			map.put("img", img);
			map.put("star", star);
			map.put("time", time);
			map.put("info", info);
			map.put("size", size);
		}
		cursor.close();

		db.close();
		return map;
	}

	public static boolean checkIsGame(Context context, String name) {
		boolean yes = false;
		String sql = "select * from " + MyGameDB.TABLE_GAME
				+ " where name like '%" + name + "%'";
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			yes = true;
		}
		
		cursor.close();

		db.close();
		return yes;
	}

}
