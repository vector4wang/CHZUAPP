package com.chzu.app.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chzu.app.bean.NewsTitle;
import com.chzu.app.util.DBHelper;
import com.chzu.app.util.L;

/**
 *  
 * @author wangxingchao    
 * @version 1.0  
 * @created 2015-5-26 下午4:03:15
 */
public class NewsTitleDao {
	private DBHelper dbHelper;
	
	public NewsTitleDao(Context context){
		dbHelper = new DBHelper(context);
	}
	
	/**
	 * id，title，date，urlLink,newsType
	 * @param newsTitle
	 */
	public void add(NewsTitle newsTitle){
		L.e("增加新闻","add new newsType"+newsTitle.getNewsType());
		String sql = "insert into t_newsTitle (title,date,urlLink,newsType) values(?,?,?,?)";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql, new Object[] {newsTitle.getTitle(), newsTitle.getpDate(), newsTitle.getLink(), newsTitle.getNewsType()});
		db.close();
	}
	
	public void deleteAll(int newsType){
		String sql = "delete from t_newsTitle where newsType = ?";
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.execSQL(sql, new Object[]{newsType});
		db.close();
	}
	
	public void add(List<NewsTitle> newsTitles){
		for(NewsTitle newsTitle : newsTitles){
			add(newsTitle);
		}
	}
	
	public List<NewsTitle> list(int newsType){
		L.e("新闻类型",newsType + "newsType");
		List<NewsTitle> newsTitles	 = new ArrayList<NewsTitle>();
		try{
			
		
		String sql = "select title,date,urlLink,newsType from t_newsTitle where newsType = ?";
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.rawQuery(sql, new String []{newsType+ "" });
		NewsTitle newsTitle = null;
		while(c.moveToNext()){
			newsTitle = new NewsTitle();
			
			String title = c.getString(0);
			String date = c.getString(1);
			String urlLink = c.getString(2);
			Integer newstype = c.getInt(3);
			
			newsTitle.setTitle(title);
			newsTitle.setpDate(date);
			newsTitle.setLink(urlLink);
			newsTitle.setNewsType(newstype);
			
			newsTitles.add(newsTitle);
		}
		c.close();
		db.close();
		L.e("新闻个数",newsTitles.size() + "newsItems.size()");
		}catch(Exception e){
			e.printStackTrace();
		}
		return newsTitles;
	}
}
