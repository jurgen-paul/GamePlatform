package com.tiankonguse.gameplatform.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * a class that help me do something on sqlite very easy. 
 */

public class DBHelper extends SQLiteOpenHelper{

	private static final int VERSION = 1;
	
	public DBHelper(
			Context context, 
			String name, 
			CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public DBHelper(
			Context context, 
			String name, 
			int version) {
		super(context, name, null, version);
	}

	public DBHelper(
			Context context, 
			String name) {
		this(context, name, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
