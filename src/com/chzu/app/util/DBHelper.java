package com.chzu.app.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBHelper extends SQLiteOpenHelper{
	
	private static final String DB_Name = "chzu_app_client";
	
	public DBHelper(Context context){
		super(context, DB_Name, null , 1);
	}
	
	/**
	 * 创建数据库
	 * 属性:id，title，date，urlLink,newsType
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table t_newsTitle(_id integer primary key autoincrement ," +
				"title text, urlLink text, date text, newsType integer)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
