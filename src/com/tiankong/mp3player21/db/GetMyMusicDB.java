package com.tiankong.mp3player21.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GetMyMusicDB {

	/**
	 * 程序启动的预处理
	 * @param context
	 * @param tablename
	 */
	public static void LoadMyMusicDB(ContentResolver resolver,Context context){
		
		//创建数据库
		SetMyMusicDB.CreateTable(context);
		//得到AllMusicList
		getList(context, MyMusicDB.db_allmusic);
		//若得到的AllMusicList为空，则从SD卡中重新刷新数据库
		if(MyMusicDB.getList(MyMusicDB.db_allmusic).isEmpty()){
			SetMyMusicDB.updateMusicListFromSD(resolver, context);
		}
		//得到所有列表
		getList(context);

	}

	
	/**
	 * 将MyMusicList从数据库中加载进来
	 * @param context
	 * @param typename
	 */
	public static void getMyMusicList(Context context,String typename){
		
		List<HashMap<String, Object>> list = MyMusicDB.getList(typename);
		
		String tablename = MyMusicDB.db_mymusic; 
		
		if(!list.isEmpty()){
			list.clear();
		}
		
		String sql = "select name,path,artist from "+tablename + " where typename = '"+typename +"'";
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			HashMap<String, Object>map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("path", path);
			map.put("artist", artist);
			list.add(map);
		}
		cursor.close();
		db.close();	
	}

	
	/**
	 * 将TypeList从数据库中加载进来
	 * @param context
	 */
	public static void getTypeList(Context context){
		List<String> typeList = MyMusicDB.getMusicTypeList();
		List<ArrayList<HashMap<String, Object>>>myMusicLists = MyMusicDB.getMyMusicLists();

		if(!typeList.isEmpty()){
			typeList.clear();
		}
		
		if(!myMusicLists.isEmpty()){
			myMusicLists.clear();
		}

		
		String tablename = MyMusicDB.db_mymusictype;
		String sql = "select typename from "+tablename;
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("typename"));
			typeList.add(name);
			myMusicLists.add(new ArrayList<HashMap<String, Object>>());
		}
		
		cursor.close();
		db.close();
	
	}

	
	/**
	 * 将指定的list从数据库中加载进来
	 * 指定的数据库有：AllMusicList 和 PlayMusicList
	 * @param context
	 * @param tablename
	 */
	public static void getList(Context context,String tablename){
		List<HashMap<String, Object>> list = MyMusicDB.getList(tablename);
		
		if(list == null){
			return ;
		}
		
		if(!list.isEmpty()){
			list.clear();
		}
		
		String sql = "select name,path,artist from "+tablename;
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String path = cursor.getString(cursor.getColumnIndex("path"));
			String artist = cursor.getString(cursor.getColumnIndex("artist"));
			HashMap<String, Object>map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("path", path);
			map.put("artist", artist);
			list.add(map);
			
		}
		
		cursor.close();
		db.close();	
	}
	
	
	/**
	 * 从数据库把数据全部加载到list中
	 * @param context
	 */
	public static void getList(Context context){
		getList(context,MyMusicDB.db_allmusic);
		getList(context,MyMusicDB.db_playmusic);
		getTypeList(context);
		
		List<String> typeList = MyMusicDB.getMusicTypeList();
		for (String name : typeList) {
			getMyMusicList(context, name);
		}
	}
	
	
}
