package com.tiankong.mp3player21.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.tiankong.mp3player21.R;
import com.tiankong.mp3player21.list.MyMusicListActivity;
import com.tiankong.mp3player21.service.ServiceHelp;

import android.R.integer;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


public class ChangeMyMusicDB {

	/**
	 * ��AllMusicList��PlayMusicList�в���Music
	 * @param context
	 * @param map
	 * @param tableName
	 */
	private static void AddMusicToList(Context context,HashMap<String, Object>map,String tableName){
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", map.get("name").toString());
		values.put("path", map.get("path").toString());
		values.put("artist", map.get("artist").toString());
		db.insert(tableName, null, values);
		db.close();
	}
	
	/**
	 * ��AllMusicList��PlayMusicList�в���Music
	 * @param context
	 * @param map
	 * @param tableName
	 */
	public static void addMusicToList(Context context,HashMap<String, Object>map,String tableName){
		
		List<HashMap<String, Object>>toList = MyMusicDB.getList(tableName);
		
		if(toList.contains(map)){
			Toast.makeText(context,R.string.seletelist_music_is_exit, Toast.LENGTH_SHORT).show();
		}else{
			AddMusicToList(context,map,tableName);
			toList.add(map);
		}	
	}
	
	/**
	 * ��AllMusicList��PlayMusicList��ɾ��Music
	 * @param context
	 * @param map
	 * @param tableName
	 */
	private static void DelMusicFromList(Context context,HashMap<String, Object>map,String tableName){
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		String sql = "delete from "+tableName+ " where name='"+map.get("name").toString()+"'";
		db.execSQL(sql);
		db.close();
	}
	
	/**
	 * ��AllMusicList��PlayMusicList��ɾ��Music
	 * @param context
	 * @param map
	 * @param tableName
	 */
	public static void delMusicFromList(Context context,HashMap<String, Object>map,String tableName){
		
		List<HashMap<String, Object>>fromList = MyMusicDB.getList(tableName);
		
		if(fromList.contains(map)){
			fromList.remove(map);
			DelMusicFromList(context,map,tableName);
		}else{
			Toast.makeText(context,R.string.seletelist_music_is_not_exit, Toast.LENGTH_SHORT).show();
		}	

	}

	/**
	 * AllMusicList��PlayMusicList�����Music
	 * @param context
	 * @param tableName
	 */
	private static void ClearMusicList(Context context,String tableName){
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		String sql = "delete from "+tableName;
		db.execSQL(sql);
		db.close();
	}
	
	/**
	 * AllMusicList��PlayMusicList�����Music
	 * @param context
	 * @param tableName
	 */
	public static void clearMusicList(Context context,String tableName){
		List<HashMap<String, Object>>fromList = MyMusicDB.getList(tableName);
		
		if(fromList.isEmpty()){
			
		}else{
			if(MyMusicDB.playStatus != MyMusicDB.status_notplay){
				ServiceHelp.stop(context);
			}
			fromList.clear();
			ClearMusicList(context,tableName);
		}
		
	}
	
	/**
	 * ��music�ӵ�MyLMusicList
	 * @param context
	 * @param map
	 * @param typeName
	 */
	private static void AddMusicToMyList(Context context,HashMap<String, Object>map,String typeName){
		
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", (String)map.get("name"));
		values.put("path", (String)map.get("path"));
		values.put("artist", (String)map.get("artist"));
		values.put("typename", typeName);
		db.insert(MyMusicDB.db_mymusic, null, values);		
	}
	
	/**
	 * ��music�ӵ�MyLMusicList
	 * @param context
	 * @param map
	 * @param typeName
	 */
	public static void addMusicToMyList(Context context,HashMap<String, Object>map,String typeName){
		
		List<HashMap<String, Object>>toList = MyMusicDB.getList(typeName);
		
		if(toList.contains(map)){
			Toast.makeText(context,R.string.seletelist_music_is_exit, Toast.LENGTH_SHORT).show();
		}else{
			AddMusicToMyList(context,map,typeName);
			toList.add(map);
		}	
	}
	
	/**
	 * ��MyMusicList��ɾ��Music
	 * @param context
	 * @param map
	 * @param typeName
	 */
	private static void DelMusicFromMyList(Context context,HashMap<String, Object>map,String typeName){
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		String sql = "delete from "+MyMusicDB.db_mymusic+ " where name='"+map.get("name").toString()+"' and typename = '"+typeName+"'";
		db.execSQL(sql);
		db.close();
	}
	
	/**
	 * ��MyMusicList��ɾ��Music
	 * @param context
	 * @param map
	 * @param typeName
	 */
	public static void delMusicFromMyList(Context context,HashMap<String, Object>map,String typeName){
		
		List<HashMap<String, Object>>toList = MyMusicDB.getList(typeName);
		
		if(toList.contains(map)){
			toList.remove(map);
			DelMusicFromMyList(context,map,typeName);
		}else{
			Toast.makeText(context,R.string.seletelist_music_is_not_exit, Toast.LENGTH_SHORT).show();
		}		
	}
	
	/**
	 * ��type�ӵ�typeMusicList
	 * @param context
	 * @param typeName
	 */
	private static void AddTypeToMyMusic(Context context,String typeName){
		
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("typename", typeName);
		db.insert(MyMusicDB.db_mymusictype, null, values);		
	}
	
	/**
	 * ��type�ӵ�typeMusicList
	 * @param context
	 * @param typeName
	 */
	public static void addTypeToMyMusic(Context context,String typeName){
		
		List<String> myTypeList = MyMusicDB.getMusicTypeList();
		if(myTypeList.contains(typeName)){
			Toast.makeText(context, "�б��Ѵ���", Toast.LENGTH_SHORT).show();
		}else{
			List<ArrayList<HashMap<String, Object>>>myMusicList = MyMusicDB.getMyMusicLists();
			myTypeList.add(typeName);
			myMusicList.add(new ArrayList<HashMap<String,Object>>());
			AddTypeToMyMusic(context, typeName);
		}
		
	}
	
	/**
	 * ��type��typeMusicList��ɾ��
	 * @param context
	 * @param typeName
	 */
	private static void DelTypeFromMyMusic(Context context,String typeName){
		SQLiteDatabase db = new DBHelper(context, MyMusicDB.db_name).getWritableDatabase();
		String sql = "delete from "+MyMusicDB.db_mymusic+ " where typename = '"+typeName+"'";
		db.execSQL(sql);
		
		sql = "delete from "+MyMusicDB.db_mymusictype+ " where typename = '"+typeName+"'";
		db.execSQL(sql);

		db.close();
	}

	/**
	 * ��type��typeMusicList��ɾ��
	 * @param context
	 * @param typeName
	 */
	public static void delTypeFromMyMusic(Context context,String typeName){
		
		List<String>typeList = MyMusicDB.getMusicTypeList();
		List<ArrayList<HashMap<String, Object>>>myMusicList = MyMusicDB.getMyMusicLists();
		if(typeList.contains(typeName)){
			int position = typeList.indexOf(typeName);
			typeList.remove(position);
			myMusicList.remove(position);
			DelTypeFromMyMusic(context,typeName);
		}else{
			Toast.makeText(context,"���б�����", Toast.LENGTH_SHORT).show();
		}		
	}
	
	/**
	 * ����ALLMUSICLIST�б�
	 * @param context
	 */
	public static void updateMyMusicList(ContentResolver resolver,Context context){
		SetMyMusicDB.updateMusicListFromSD(resolver, context);
		GetMyMusicDB.getList(context,MyMusicDB.db_allmusic);
	}
}
