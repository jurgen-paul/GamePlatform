package com.tiankonguse.gameplatform.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 *
 *get data from database.
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
	public static void LoadGameDB(ContentResolver resolver,Context context){
		
		SetGameDB.CreateTable(context);
		getGameClassList(context);
		getGameRankList(context);
		
	}
	
	/**
	 * load the list of class
	 * class is TABLE_CLASS
	 */
	public static void getGameClassList(Context context){
		
		List<HashMap<String, Object>> list = MyGameDB.getList(MyGameDB.GAME_CLASS_NAME);
		
		if(list == null){
			Log.i("warning", "GAME_CLASS_NAME list is null");
			return ;
		}
		
		if(!list.isEmpty()){
			list.clear();
		}
		
		String sql = "select * from "+MyGameDB.TABLE_CLASS;
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String id = cursor.getString(cursor.getColumnIndex("id"));
			HashMap<String, Object>map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("id", id);
			list.add(map);
		}
		
		Log.i("warning", "class list's lenght is "+list.size());
		
		cursor.close();
		
		db.close();	
		
	}	
	
	/**
	 * load the list of class
	 * class is TABLE_CLASS
	 */
	public static void getGameRankList(Context context){
		
		List<HashMap<String, Object>> list = MyGameDB.getList(MyGameDB.GAME_RANK_NAME);
		
		if(list == null){
			Log.i("warning", "GAME_RANK_NAME list is null");
			return ;
		}
		
		if(!list.isEmpty()){
			list.clear();
		}
		
		List<String> gameList = new ArrayList<String>();
		
		String sql = "select game from "+MyGameDB.TABLE_GAME_CLASS + " where class = 102";
		
		Log.i("warning", "sql :" + sql);
		
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("game"));
			gameList.add(name);
		}
		
		for (String game : gameList) {
			sql = "select id, name, dowmLink, imgLink, star from "+MyGameDB.TABLE_GAME + " where id = '" + game +"'";
			cursor = db.rawQuery(sql, null);
			String id = cursor.getString(cursor.getColumnIndex("id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String dowmLink = cursor.getString(cursor.getColumnIndex("dowmLink"));
			String imgLink = cursor.getString(cursor.getColumnIndex("imgLink"));
			String star = cursor.getString(cursor.getColumnIndex("star"));
			HashMap<String, Object>map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("name", name);
			map.put("dowmLink", dowmLink);
			map.put("imgLink", imgLink);
			map.put("star", star);
			list.add(map);
		}
		

		
		Log.i("warning", "rank list's lenght is "+list.size());
		
		cursor.close();
		
		db.close();	
	}
}
