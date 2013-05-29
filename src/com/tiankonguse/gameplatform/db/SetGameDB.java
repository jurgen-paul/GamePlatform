package com.tiankonguse.gameplatform.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tiankonguse.gameplatform.net.DownLoadList;
import com.tiankonguse.gameplatform.struct.GameClassStruct;

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
				+ ",game_id integer not null "
				+ ")";

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

		Log.i("SetGameDB", "begin down");

		DownLoadList downLoadList = new DownLoadList(MyGameDB.URL
				+ "get_class.php");

		String classString = downLoadList.Begin().getText();

		Log.i("SetGameDB", "classString = " + classString);

		changeStringToClass(classString);

		updateDatabaseGameClass(context);
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
				
				String id  = classInfo.get("id").toString();
				String name = classInfo.get("name").toString();
				
				String sql = "select * from " + tablename + " where id = '"+id +"'";
				Cursor cursor = db.rawQuery(sql, null);
				
				if(cursor.moveToNext()){
					String oldName = cursor.getString(cursor.getColumnIndex("name"));
					if(!name.equals(oldName)){
						db.execSQL("update "+tablename+" set name = '"+name+"' where  id = '"+id+"'");
					}
				}else{
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

}
