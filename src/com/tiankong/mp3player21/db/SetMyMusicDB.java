package com.tiankong.mp3player21.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


import com.tiankong.mp3player21.info.MusicInfo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class SetMyMusicDB {


	/**
	 * 创建数据库
	 * @param context
	 */
	public static void CreateTable(Context context){
		
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		String createTabehead = "create table if not exists ";
		String createConstTabeend = "";
		
		/**
		 * 创建所有列表和播放列表
		 */
		createConstTabeend = " (id integer primary key autoincrement not null, name varchar(50) , path varchar(100) , artist varchar(50))";
		db.execSQL(createTabehead + MyMusicDB.db_allmusic + createConstTabeend);
		db.execSQL(createTabehead + MyMusicDB.db_playmusic + createConstTabeend);
		
		
		/**
		 * 创建我的音乐的类别表
		 */
		createConstTabeend =" (id integer primary key autoincrement not null, typename varchar(50) )";
		db.execSQL(createTabehead + (MyMusicDB.db_mymusictype) + createConstTabeend);

		
		/**
		 * 创建我的音乐的数据库
		 */
		
		createConstTabeend =" (id integer primary key autoincrement not null, name varchar(50) , path varchar(100) , artist varchar(50), typename varchar(50))";
		db.execSQL(createTabehead +(MyMusicDB.db_mymusic) + createConstTabeend);

		db.close();
	}

	/**
	 * 重新获得SD卡中的音乐列表,存入数据库
	 * 用于更新列表或列表为空
	 * @param resolver
	 * @param context
	 */
	public static void updateMusicListFromSD(ContentResolver resolver,Context context){
		List<MusicInfo> musicInfos = new ArrayList<MusicInfo>();
		
		Cursor cursor = resolver.query(MyMusicDB.URI, MyMusicDB.projection, MyMusicDB.SIZE +">"+MyMusicDB.size, null, MyMusicDB.SORTORDER);
		
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndexOrThrow(MyMusicDB.TITLE));
			String artist = cursor.getString(cursor.getColumnIndexOrThrow(MyMusicDB.ARTIST));
			String path = cursor.getString(cursor.getColumnIndexOrThrow(MyMusicDB.DATA));
			musicInfos .add(new MusicInfo(name,path,artist));
		}
		Log.i("number", "-->"+musicInfos.size());
		String tablename = MyMusicDB.db_allmusic;
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		db.execSQL("delete from "+tablename);
		
		if(!musicInfos.isEmpty()){
			for(Iterator<MusicInfo> iterator = musicInfos.iterator();iterator.hasNext();){
				MusicInfo musicInfo = (MusicInfo)iterator.next();
				ContentValues values = new ContentValues();
				values.put("name", musicInfo.getName());
				values.put("path", musicInfo.getPath());
				values.put("artist", musicInfo.getArtist());
				db.insert(tablename, null, values);
			}
		}
		db.close();
	}
	
	
	/**
	 * 将一个列表存入数据库
	 * 只用于Allmusiclist和palymusiclist
	 * @param context
	 * @param tablename
	 */
	public static void updateMusicList(Context context,String tablename){

		List<HashMap<String, Object>> list = MyMusicDB.getList(tablename);
		updateMusicList(context,tablename,list);

	}

	/**
	 * 将一个列表存入数据库,列表是自定义列表
	 * @param context
	 * @param tablename
	 */
	public static void updateMusicList(Context context,String tablename,List<HashMap<String, Object>> list){
		
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		db.execSQL("delete from "+tablename);
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='"+tablename+"'");
		
		for (HashMap<String, Object> map : list) {
			ContentValues values = new ContentValues();
			values.put("name", (String)map.get("name"));
			values.put("path", (String)map.get("path"));
			values.put("artist", (String)map.get("artist"));
			db.insert(tablename, null, values);
		}
		db.close();
		
	}
	
	
	/**
	 * 将Mymusictypelist存入数据库
	 * @param context
	 */
	public static void updateMyTypeList(Context context){
		String tablename = MyMusicDB.db_mymusictype;
		List<String> types = MyMusicDB.getMusicTypeList();

		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		
		db.execSQL("delete from "+tablename);
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='"+tablename+"'");
		
		for (String type : types) {
			ContentValues values = new ContentValues();
			values.put("typename", type);
			db.insert(tablename, null, values);
		}
		db.close();
		
	}

	/**
	 * 将Mymusiclist存入数据库
	 * @param context
	 */
	public static void updateMyList(Context context){
		
		updateMyTypeList(context);
		List<String> types = MyMusicDB.getMusicTypeList();
		String tablename = MyMusicDB.db_mymusic;
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		db.execSQL("delete from "+tablename);
		db.execSQL("update sqlite_sequence SET seq = 0 where name ='"+tablename+"'");
		
		for (String type : types) {
			List<HashMap<String, Object>> list = MyMusicDB.getList(type);
			for (HashMap<String, Object> map : list) {
				ContentValues values = new ContentValues();
				values.put("name", (String)map.get("name"));
				values.put("path", (String)map.get("path"));
				values.put("artist", (String)map.get("artist"));
				values.put("typename", type);
				db.insert(tablename, null, values);
			}
		}
	}
	
	/**
	 * 更新全部列表的数据库
	 * @param context
	 */
	public static void updateList(Context context){
		updateMusicList(context,MyMusicDB.db_allmusic);
		updateMusicList(context,MyMusicDB.db_playmusic);
		updateMyList(context);
	}
	

}
