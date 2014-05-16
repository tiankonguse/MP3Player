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
	 * ����������Ԥ����
	 * @param context
	 * @param tablename
	 */
	public static void LoadMyMusicDB(ContentResolver resolver,Context context){
		
		//�������ݿ�
		SetMyMusicDB.CreateTable(context);
		//�õ�AllMusicList
		getList(context, MyMusicDB.db_allmusic);
		//���õ���AllMusicListΪ�գ����SD��������ˢ�����ݿ�
		if(MyMusicDB.getList(MyMusicDB.db_allmusic).isEmpty()){
			SetMyMusicDB.updateMusicListFromSD(resolver, context);
		}
		//�õ������б�
		getList(context);

	}

	
	/**
	 * ��MyMusicList�����ݿ��м��ؽ���
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
	 * ��TypeList�����ݿ��м��ؽ���
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
	 * ��ָ����list�����ݿ��м��ؽ���
	 * ָ�������ݿ��У�AllMusicList �� PlayMusicList
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
	 * �����ݿ������ȫ�����ص�list��
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
