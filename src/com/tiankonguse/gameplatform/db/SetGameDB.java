package com.tiankonguse.gameplatform.db;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tiankonguse.gameplatform.net.DownLoadList;

/**
 * 
 * store data to database.
 * 
 */
public class SetGameDB {

	/**
	 * 
	 * create database and tables.
	 * 
	 */
	public static void CreateTable(Context context) {

		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getWritableDatabase();

		String createTabehead = "create table if not exists ";
		String createConstTabeend = "";

		/**
		 * create table class id key name: game class name
		 * 
		 */
		createConstTabeend = " (id integer primary key not null, name varchar(50) not null)";

		db.execSQL(createTabehead + MyGameDB.TABLE_CLASS + createConstTabeend);

		/**
		 * create table game
		 * 
		 */
		createConstTabeend = " ( id integer primary key not null"
				+ ", name varchar(50) not null" + ", apk varchar(255) not null"
				+ ", img varchar(255) not null"
				+ ", star varchar(255) not null" + ", size varchar(255) "
				+ ", time varchar(255) " + ", info TEXT" + ")";

		db.execSQL(createTabehead + MyGameDB.TABLE_GAME + createConstTabeend);

		/**
		 * create table map
		 * 
		 */
		createConstTabeend = " (class_id integer not null"
				+ ",game_id integer not null " + ")";

		db.execSQL(createTabehead + MyGameDB.TABLE_MAP + createConstTabeend);

		db.close();

	}

	/**
	 * 
	 * update class from network and store to database.
	 * 
	 * @throws JSONException
	 * 
	 */
	public static void DownLoadClass(Context context) {

		Log.i("SetGameDB", "begin down Class");

		DownLoadList downLoadList = new DownLoadList(MyGameDB.URL
				+ "get_class.php");

		String classString = downLoadList.Begin().getText();

		Log.i("SetGameDB", "classString = " + classString);

		changeStringToClass(classString);

		updateDatabaseGameClass(context);
	}

	/**
	 * 
	 * update rank from network and store to database.
	 * 
	 * 
	 */
	public static void DownLoadRank(Context context) {
		Log.i("SetGameDB", "begin down Rank");

		DownLoadList downLoadList = new DownLoadList(MyGameDB.URL
				+ "get_rank.php");

		String classString = downLoadList.Begin().getText();

		Log.i("SetGameDB", "rankString = " + classString);

		changeStringToRank(classString);

		updateDatabaseGameRank(context);
	}

	/**
	 * 
	 * update game list from network and store to database.
	 * 
	 * 
	 */
	public static void DownLoadList(Context context) {
		Log.i("SetGameDB", "begin down list");

		DownLoadList downLoadList = new DownLoadList(MyGameDB.URL
				+ "get_cid.php?cid=" + MyGameDB.GAME_NOWLIST_ID);
		String listString = downLoadList.Begin().getText();

		Log.i("SetGameDB", "listString = " + listString);

		changeStringToList(listString);

		updateDatabaseGameList(context);
	}

	public static void DownLoadGame(Context context, String id) {
		DownLoadList downLoadList = new DownLoadList(MyGameDB.URL
				+ "get_game.php?id=" + id);

		String gameString = downLoadList.Begin().getText();

		Log.i("SetGameDB", "gameString = " + gameString);

		HashMap<String, Object> map = changeStringToGame(gameString);
		;
		saveGame(context, map);
	}

	/**
	 * 
	 * analyze the string that download from network to class list.
	 * 
	 */
	public static void changeStringToClass(String s) {

		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_CLASS_NAME);

		if (!list.isEmpty()) {
			list.clear();
		}

		try {
			JSONArray classList = new JSONArray(s);
			int len = classList.length();
			for (int i = 0; i < len; i++) {
				JSONObject gameJsonObject = classList.getJSONObject(i);

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", gameJsonObject.getString("name"));
				map.put("id", gameJsonObject.getInt("id"));

				Log.i("SetGameDB", "name = " + gameJsonObject.getString("name")
						+ " id = " + gameJsonObject.getString("id"));

				list.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * analyze the string that download from network to rank list.
	 * 
	 */
	public static void changeStringToRank(String s) {
		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_RANK_NAME);

		if (!list.isEmpty()) {
			list.clear();
		}

		try {
			JSONArray classList = new JSONArray(s);
			int len = classList.length();
			for (int i = 0; i < len; i++) {
				JSONObject gameJsonObject = classList.getJSONObject(i);

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", gameJsonObject.getString("id"));
				map.put("name", gameJsonObject.getString("name"));
				map.put("img", gameJsonObject.getString("img"));
				map.put("star", gameJsonObject.getString("star"));
				map.put("apk", gameJsonObject.getString("apk"));
				map.put("time", gameJsonObject.getString("time"));
				map.put("info", gameJsonObject.getString("info"));
				map.put("size", gameJsonObject.getString("size"));
				list.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Collections.sort(list,  new Comparator<HashMap<String, Object>>() {
            public int compare(HashMap<String, Object> first, HashMap<String, Object> second) {
                return Integer.parseInt(first.get("star").toString()) - Integer.parseInt(second.get("star").toString());
            }
        });
		
		
	}

	/**
	 * 
	 * analyze the string that download from network to list.
	 * 
	 */
	public static void changeStringToList(String s) {
		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_NOWLIST_NAME);

		if (!list.isEmpty()) {
			list.clear();
		}

		try {
			JSONArray classList = new JSONArray(s);
			int len = classList.length();
			for (int i = 0; i < len; i++) {
				JSONObject gameJsonObject = classList.getJSONObject(i);

				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", gameJsonObject.getString("id"));
				map.put("name", gameJsonObject.getString("name"));
				map.put("img", gameJsonObject.getString("img"));
				map.put("star", gameJsonObject.getString("star"));
				map.put("apk", gameJsonObject.getString("apk"));
				map.put("time", gameJsonObject.getString("time"));
				map.put("info", gameJsonObject.getString("info"));
				map.put("size", gameJsonObject.getString("size"));

				// Log.i("SetGameDB", "name = " +
				// gameJsonObject.getString("name")
				// + " id = " + gameJsonObject.getString("id"));

				list.add(map);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("SetGameDB", "changeStringToList");
	}

	public static HashMap<String, Object> changeStringToGame(String s) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			JSONObject gameJsonObject = new JSONObject(s);
			map.put("id", gameJsonObject.getString("id"));
			map.put("name", gameJsonObject.getString("name"));
			map.put("img", gameJsonObject.getString("img"));
			map.put("star", gameJsonObject.getString("star"));
			map.put("apk", gameJsonObject.getString("apk"));
			map.put("time", gameJsonObject.getString("time"));
			map.put("info", gameJsonObject.getString("info"));
			map.put("size", gameJsonObject.getString("size"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}

	public static void saveGame(Context context,
			HashMap<String, Object> classInfo) {

		String id = classInfo.get("id").toString();
		String name = classInfo.get("name").toString();
		String img = classInfo.get("img").toString();
		String star = classInfo.get("star").toString();
		String apk = classInfo.get("apk").toString();
		String time = classInfo.get("time").toString();
		String info = classInfo.get("info").toString();
		String size = classInfo.get("size").toString();

		String tablename = MyGameDB.TABLE_GAME;

		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getWritableDatabase();

		String sql = "select * from " + tablename + " where id = '" + id + "'";
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToNext()) {
			db.execSQL("update " + tablename + " set " + "name = '" + name
					+ "' ," + "img = '" + img + "' ," + "star = '" + star
					+ "' ," + "apk = '" + apk + "' ," + "time = '" + time
					+ "' ," + "info = '" + info + "' ," + "size = '" + size
					+ "' where  id = '" + id + "'");
		} else {
			ContentValues values = new ContentValues();
			values.put("id", id);
			values.put("name", name);
			values.put("img", img);
			values.put("star", star);
			values.put("apk", apk);
			values.put("time", time);
			values.put("info", info);
			values.put("size", size);
			db.insert(tablename, null, values);
		}
		db.close();

	}

	public static void updateClassList(Context context,
			List<HashMap<String, Object>> list, String class_id) {

		if (!list.isEmpty()) {

			String tablename = MyGameDB.TABLE_MAP;

			SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
					.getWritableDatabase();

			db.execSQL("delete from " + tablename + " where class_id = '"
					+ class_id + "'");

			HashMap<String, Object> classInfo = null;

			for (Iterator<HashMap<String, Object>> iterator = list.iterator(); iterator
					.hasNext();) {
				classInfo = (HashMap<String, Object>) iterator.next();

				saveGame(context, classInfo);

				ContentValues values = new ContentValues();
				values.put("game_id", classInfo.get("id").toString());
				values.put("class_id", class_id);
				db.insert(tablename, null, values);
			}
			db.close();
		}
	}

	/**
	 * 
	 * store rank list to databases.
	 * 
	 */
	public static void updateDatabaseGameRank(Context context) {

		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_RANK_NAME);

		updateClassList(context, list, "102");

		Log.i("SetGameDB", "update rank db");
	}

	public static void updateDatabaseGameList(Context context) {
		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_NOWLIST_NAME);

		// Log.i("SetGameDB", "update list size " + list.size());

		updateClassList(context, list, MyGameDB.GAME_NOWLIST_ID);

		Log.i("SetGameDB", "update list db");
	}

	/**
	 * 
	 * store class list to databases.
	 * 
	 */
	public static void updateDatabaseGameClass(Context context) {

		List<HashMap<String, Object>> list = MyGameDB
				.getList(MyGameDB.GAME_CLASS_NAME);

		String tablename = MyGameDB.TABLE_CLASS;
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME)
				.getWritableDatabase();

		if (!list.isEmpty()) {
			HashMap<String, Object> classInfo = null;
			for (Iterator<HashMap<String, Object>> iterator = list.iterator(); iterator
					.hasNext();) {
				classInfo = (HashMap<String, Object>) iterator.next();

				String id = classInfo.get("id").toString();
				String name = classInfo.get("name").toString();

				String sql = "select * from " + tablename + " where id = '"
						+ id + "'";
				Cursor cursor = db.rawQuery(sql, null);

				if (cursor.moveToNext()) {
					String oldName = cursor.getString(cursor
							.getColumnIndex("name"));
					if (!name.equals(oldName)) {
						db.execSQL("update " + tablename + " set name = '"
								+ name + "' where  id = '" + id + "'");
					}
				} else {
					ContentValues values = new ContentValues();
					values.put("name", classInfo.get("name").toString());
					values.put("id", classInfo.get("id").toString());
					db.insert(tablename, null, values);
				}

			}
		}
		db.close();
		Log.i("SetGameDB", "update class db");
	}
	
	public static void deleteItemFromList(Context context,HashMap<String, Object>map,String tableName){
		List<HashMap<String, Object>>fromList = MyGameDB.getList(tableName);
		if(fromList.contains(map)){
			fromList.remove(map);
		}
	}
	
}
