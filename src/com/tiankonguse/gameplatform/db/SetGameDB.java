package com.tiankonguse.gameplatform.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import struct.GameClassStruct;
import android.content.ContentValues;
import android.content.Context;
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
		createConstTabeend = " (" + "id integer primary key not null"
				+ ", name varchar(50) not null"
				+ ", downLink varchar(255) not null"
				+ ", imgLink varchar(255) not null"
				+ ", star varchar(255) not null" + ", size varchar(255) "
				+ ", time varchar(255) " + ", introduction TEXT" + ")";

		db.execSQL(createTabehead + MyGameDB.TABLE_GAME + createConstTabeend);

		/**
		 * create table map
		 * 
		 */
		createConstTabeend = " (" + "class integer not null"
				+ ",game integer not null" + ",primary key (class,game)" + ")";

		db.execSQL(createTabehead + MyGameDB.TABLE_GAME_CLASS
				+ createConstTabeend);

		db.close();

	}
	/**
	 * 
	 * update class from network and store to database.
	 * 
	 */
	public static void DownLoadClass(Context context) {
		DownLoadList downLoadList = new DownLoadList(MyGameDB.URL+"get_class.php");
		
		String classString = downLoadList.Begin().getText();
		
		Log.i("SetGameDB", "classString = "+classString);
		
		changeStringToClass(classString);
		
	}
	
	/**
	 * 
	 * analyze the string that download from network to class list. 
	 * 
	 */
	public static void changeStringToClass(String s){

		s = s.trim();
		
		List<HashMap<String, Object>> list = MyGameDB.getList(MyGameDB.GAME_RANK_NAME);
		
		if(!list.isEmpty()){
			list.clear();
		}
		
		String[] stringArray ;
		
		stringArray = s.split(";");
		
		Log.i("SetGameDB", "stringArray.length" + stringArray.length);
		
		GameClassStruct tmp  = new GameClassStruct();
		
		for (int i = 0; i < stringArray.length; i++) {
			
			Log.i("SetGameDB", "stringArray[" + i + "] = " + stringArray[i]);
			if(tmp.split(stringArray[i])){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", tmp.getName());
				map.put("id", tmp.getId());
				map.put("link", tmp.getLink());
				list.add(map);
			}
		}
		
		Log.i("SetGameDB", "list size " + list.size());

	}
	
	/**
	 * 
	 *store class list to databases. 
	 * 
	 */
	public static void updateDatabaseGameClass(Context context) {
		
		List<HashMap<String, Object>> list = MyGameDB.getList(MyGameDB.GAME_RANK_NAME);

		
		String tablename = MyGameDB.TABLE_CLASS;
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME).getWritableDatabase();
		db.execSQL("delete from " + tablename + "where");
		
		if(!list.isEmpty()){
			HashMap<String, Object> classInfo = null;
			for(Iterator<HashMap<String, Object>> iterator = list.iterator();iterator.hasNext();){
				classInfo = (HashMap<String, Object>)iterator.next();
				ContentValues values = new ContentValues();
				values.put("name", classInfo.get("name").toString());
				values.put("id", classInfo.get("id").toString());
				values.put("link", classInfo.get("link").toString());
				db.insert(tablename, null, values);
			}
		}
		db.close();	
		Log.i("SetGameDB", "update class db");
	}	
	
}
