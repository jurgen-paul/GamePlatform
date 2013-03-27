package com.tiankonguse.gameplatform.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 *
 *store data to database.
 *
 */
public class SetGameDB {
	
	/**
	 * 
	 * create database and tables.
	 * 
	 */
	public static void CreateTable(Context context){
		
		SQLiteDatabase db = new DBHelper(context, MyGameDB.DB_NAME).getWritableDatabase();
		
		String createTabehead = "create table if not exists ";
		String createConstTabeend = "";
		
		/**
		 * create table class 
		 * id key
		 * name: game class name
		 * 
		 */
		createConstTabeend = " (id integer primary key not null, name varchar(50) not null)";
		
		db.execSQL(createTabehead + MyGameDB.TABLE_CLASS + createConstTabeend);

		/**
		 * create table game
		 * 
		 */
		createConstTabeend = " ("
				+ "id integer primary key not null"
				+ ", name varchar(50) not null"
				+ ", downLink varchar(255) not null"
				+ ", imgLink varchar(255) not null"
				+ ", star varchar(255) not null"
				+ ", size varchar(255) "
				+ ", time varchar(255) "
				+ ", introduction TEXT"
				+ ")";
		
		db.execSQL(createTabehead + MyGameDB.TABLE_GAME + createConstTabeend);

		/**
		 * create table map
		 * 
		 */
		createConstTabeend = " ("
				+ "class integer not null"
				+ ",game integer not null"
				+ ",primary key (class,game)"
				+")";
		
		db.execSQL(createTabehead + MyGameDB.TABLE_GAME_CLASS + createConstTabeend);

		db.close();
		
	}
	
	
}
