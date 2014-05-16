package com.tiankong.mp3player21.service;

import java.util.HashMap;

import com.tiankong.mp3player21.db.MyMusicDB;
import com.tiankong.mp3player21.info.MusicInfo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class PlayService extends Service{

	public static MediaPlayer player = null;
	public static MusicInfo musicInfo ;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	@Override
	public void onCreate() {
		player = new MediaPlayer();
		player.setOnCompletionListener(new OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				ServiceHelp.playNext(getApplicationContext());				
			}
		});
		
		musicInfo = new MusicInfo();
		super.onCreate();
	}


	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
//		Log.i("PlayService", "flag  String -->"+intent.getStringExtra("flag"));
//		Log.i("PlayService", "flag  int -->"+intent.getFlags());
//		Log.i("PlayService", "flags -->"+flags);
		
		ServiceHelp.loadService(getApplicationContext(),getContentResolver());
		
		int flag = intent.getFlags();
//		Log.i("PlayService", "service  name-->"+PlayService.getMusicInfo().getName());
//		Log.i("PlayService", "service  position-->"+MyMusicDB.musicPosition);
//		Log.i("PlayService", "flag-->"+flag);
		

		if(MyMusicDB.getList(MyMusicDB.db_playmusic).isEmpty()){
			Toast.makeText(getApplicationContext(), "播放列表为空，将自动播放所有音乐", Toast.LENGTH_SHORT).show();
			ServiceHelp.turnToPlayList(getApplicationContext(), MyMusicDB.getList(MyMusicDB.db_allmusic));
		}else{
			if(flag >0){
				ServiceHelp.getPosition();
				if(flag == MyMusicDB.FLAG_LAST_MUSIC){
					ServiceHelp.playLast(getApplicationContext());				
				}else if(flag == MyMusicDB.FLAG_NEXT_MUSIC){
					ServiceHelp.playNext(getApplicationContext());
				}else if(flag == MyMusicDB.FLAG_PLAY_MUSIC){
					ServiceHelp.playOrpause(getApplicationContext());
				}else if(flag == MyMusicDB.FLAG_STOP_MUSIC){
					ServiceHelp.stop(getApplicationContext());
				}
			}else {
				musicInfo.setName(intent.getStringExtra("name"));
				musicInfo.setArtist(intent.getStringExtra("artist"));
				musicInfo.setPath(intent.getStringExtra("path"));
				ServiceHelp.getPosition();
				ServiceHelp.play(getApplicationContext());					
			}
		}
		
		

		return super.onStartCommand(intent, flags, startId);
	}


	public static MusicInfo getMusicInfo() {
		return musicInfo;
	}


	public static void setMusicInfo(MusicInfo musicInfo) {
		PlayService.musicInfo = new MusicInfo(musicInfo);
	}
	public static void setMusicInfo(HashMap<String, Object> map) {
		PlayService.musicInfo.setMusicInfo(map);
	}


	@Override
	public void onDestroy() {
		player = null;
		super.onDestroy();
	}


	
	public static int getMaxMilliSeconds(){
		return player.getDuration();
	}
	
	public static int getCurrentMilliSecond(){
		return player.getCurrentPosition();
	}

	
}
