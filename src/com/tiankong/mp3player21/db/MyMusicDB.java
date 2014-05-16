package com.tiankong.mp3player21.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.Uri;
import android.provider.MediaStore;

public final class MyMusicDB {

	private static List<HashMap<String, Object>> allMusicList = new  ArrayList<HashMap<String,Object>>();
	private static List<HashMap<String, Object>> playMusicList = new  ArrayList<HashMap<String,Object>>();
	
	private static List<String>myMusicTypeList = new ArrayList<String>();
	private static List<ArrayList<HashMap<String, Object>>> myMusicLists= new ArrayList<ArrayList<HashMap<String, Object>>>();

	public static final String  db_name           = "tiankongmusic";
	public static final String  db_allmusic  = "all_music";
	public static final String  db_playmusic = "play_music";
	public static final String  db_mymusictype    = "mymusictype";
	public static final String  db_mymusic        = "mymusic";
	
	public static Uri    URI       = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	public static String SORTORDER = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
	public static String TITLE     = MediaStore.Audio.Media.TITLE;
	public static String ARTIST    = MediaStore.Audio.Media.ARTIST;
	public static String DATA      = MediaStore.Audio.Media.DATA;
	public static String SIZE      = MediaStore.Audio.Media.SIZE;
	public static String DURATION  = MediaStore.Audio.Media.DURATION;
	
	public static long size = 1024*1024;
	
	public static String[] projection = new String[]{TITLE,ARTIST,DATA};
	
	/**
	 * tabsµÄid
	 */
	final public static int  TAB_MY_MUSIC      =  0;
	final public static int  TAB_ALL_MUSIC     = 1;
	final public static int  TAB_PLAYING_MUSIC = 2;
	
	public static       int  playStatus      = 0;
	public static final int status_notplay  =0;
	public static final int status_playing  =1;
	public static final int status_pause    =2;
	
	public static int musicPosition = 0;
	public static String musicName  = "";
	
	public static final int FLAG_NAME_MUSIC  = 0;
	public static final int FLAG_LAST_MUSIC  = 1;
	public static final int FLAG_PLAY_MUSIC  = 2;
	public static final int FLAG_PAUSE_MUSIC = 3;
	public static final int FLAG_STOP_MUSIC  = 4;
	public static final int FLAG_NEXT_MUSIC  = 5;

	public static final String EXTRA_SERVER_MUSIC = "com.tiankong.mp3player21.SERVER";
	public static final String EXTRA_IMAGE_MUSIC = "com.tiankong.mp3player21.WIDGET_IMAGE";
	public static final String EXTRA_LAST_MUSIC  = "com.tiankong.mp3player21.WIDGET_LAST";
	public static final String EXTRA_PLAY_MUSIC  = "com.tiankong.mp3player21.WIDGET_PLAY";
	public static final String EXTRA_PAUSE_MUSIC = "com.tiankong.mp3player21.WIDGET_PAUSE";
	public static final String EXTRA_STOP_MUSIC  = "com.tiankong.mp3player21.WIDGET_STOP";
	public static final String EXTRA_NEXT_MUSIC  = "com.tiankong.mp3player21.WIDGET_NEXT";
	
	public static final int MENU_EXIT = 0;
	public static final int MENU_LIST_PLAY_DELALL = 101;
	public static final int MENU_LIST_ALL_PLAYALL = 201;
	public static final int MENU_LIST_MY_ADD = 301;
	public static final int MENU_LIST_ALL_UPDATE = 202;

	
	public static List<HashMap<String, Object>>getList(String name){
		
		if(db_allmusic.equals(name)){
			return allMusicList;
		}
		
		if(db_playmusic.equals(name)){
			return playMusicList;
		}
		
		if(myMusicTypeList.contains(name)){
			int position =myMusicTypeList.indexOf(name);
			return myMusicLists.get(position);
		}
		
		return null;
	}
	
	public static List<String> getMusicTypeList(){
		return myMusicTypeList;
	}
	
	public static List<ArrayList<HashMap<String, Object>>> getMyMusicLists(){
		return myMusicLists;
	}
	
	public static String[] getMusicTypeString(){
		String [] items = new String[myMusicTypeList.size()];
		for(int i=0; i < myMusicTypeList.size(); i++){
			items[i]=myMusicTypeList.get(i);
		}
		return items;
	}
}
