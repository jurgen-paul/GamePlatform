package com.tiankonguse.gameplatform.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyGameDB {

	/**
	 * tabs的id
	 * 
	 * TAB_RANK at the left TAB_CLASS at the middle TAB_MANGER at the right
	 * 
	 */
	final public static int TAB_RANK = 0;
	final public static int TAB_CLASS = 1;
	final public static int TAB_MANGER = 2;

	/**
	 * 
	 * the string of url is my server on the web.
	 * 
	 **/
	public static final String URL = "http://tiankonguse.com/android/";
	// public static final String URL = "http://125.222.201.19:8080/android/";

	/**
	 * 
	 * DB_NAME is the database name. TABLE_CLASS is the table of class.
	 * TABLE_GAME is the table of game. TABLE_GAME_CLASS is a map betwen game
	 * and class
	 * 
	 **/

	public static final String DB_NAME = "tk_gameplatform";
	public static final String TABLE_CLASS = "class";
	public static final String TABLE_GAME = "game";
	public static final String TABLE_MAP = "map";

	public static final String GAME_CLASS_NAME = "游戏分类";
	public static final String GAME_RANK_NAME = "时下热门";
	public static String GAME_NOWLIST_NAME = "";
	public static String GAME_NOWLIST_ID = "";

	/**
	 * 
	 * gameClassList is a list of game class. it contain class name,class
	 * imgDownloadLink,class imgSDcardLink.
	 * 
	 **/
	private static List<HashMap<String, Object>> gameClassList = new ArrayList<HashMap<String, Object>>();

	/**
	 * 
	 * gameRankList is a list of game rank. it contain game name,game
	 * imgDownloadLink,gameSDcardLink,game star and so on.
	 * 
	 **/
	private static List<HashMap<String, Object>> gameRankList = new ArrayList<HashMap<String, Object>>();

	/**
	 * 
	 * gameNowShowList is a list games that beyound a class. it contain game
	 * name,game imgDownloadLink,gameSDcardLink,game star ,very much.
	 * 
	 **/
	private static List<HashMap<String, Object>> gameNowShowList = new ArrayList<HashMap<String, Object>>();

	public static List<HashMap<String, Object>> getList(String name) {

		if (GAME_CLASS_NAME.equals(name)) {
			return gameClassList;
		}

		if (GAME_RANK_NAME.equals(name)) {
			return gameRankList;
		}

		return gameNowShowList;

	}

	public static void setGameNowName(String s, String id) {
		GAME_NOWLIST_NAME = s;
		GAME_NOWLIST_ID = id;
	}
}
