package com.tiankong.mp3player21.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.tiankong.mp3player21.MainPlayerActivity;
import com.tiankong.mp3player21.PlayerActivity;
import com.tiankong.mp3player21.R;
import com.tiankong.mp3player21.db.ChangeMyMusicDB;
import com.tiankong.mp3player21.db.GetMyMusicDB;
import com.tiankong.mp3player21.db.MyMusicDB;
import com.tiankong.mp3player21.db.SetMyMusicDB;
import com.tiankong.mp3player21.list.MyMusicListActivity;
import com.tiankong.mp3player21.widet.Mp3PlayerWidget;

public class ServiceHelp {

	public static boolean isExitMusic(String path){
		if(new File(path).exists()){
			return true;
		}else{
			return false;
		}
	}
	
	public static void play(Context context){
		if(!isExitMusic(PlayService.getMusicInfo().getPath())){
			Toast.makeText(context, R.string.musitnotexit, Toast.LENGTH_SHORT).show();
			playNext(context);
		}else{
			PlayService.player.reset();
			try {
				PlayService.player.setDataSource(PlayService.getMusicInfo().getPath());
				PlayService.player.prepare();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(MainPlayerActivity.context != null){
				MainPlayerActivity.footer.setText(PlayService.getMusicInfo().getName());
			}
			MyMusicDB.playStatus = MyMusicDB.status_playing;
			loadWidgetPlay(context);
			PlayerActivity.play();
			PlayService.player.start();
		}
	}
	
	
	public static void loadWidgetPause(Context context){
		
	      AppWidgetManager awm = AppWidgetManager.getInstance(context);  
	      RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.my_music_widget);  
	      views.setTextViewText(R.id.widget_music_name, PlayService.getMusicInfo().getName());
	      views.setImageViewResource(R.id.widget_play, R.drawable.player_footer_play_select);
	      awm.updateAppWidget(new ComponentName(context, Mp3PlayerWidget.class), views);  
	}
	
	public static void loadWidgetPlay(Context context){
	      AppWidgetManager awm = AppWidgetManager.getInstance(context);  
	      RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.my_music_widget);  
	      views.setTextViewText(R.id.widget_music_name, PlayService.getMusicInfo().getName());
	      views.setImageViewResource(R.id.widget_play, R.drawable.player_footer_pause_select);
	      awm.updateAppWidget(new ComponentName(context, Mp3PlayerWidget.class), views);  

	}
	
	public static void loadWidgetStop(Context context){
//		 Log.i("ServiceHelp", "--->"+context.toString());
	      AppWidgetManager awm = AppWidgetManager.getInstance(context);  
	      RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.my_music_widget);  
	      views.setTextViewText(R.id.widget_music_name, context.getString(R.string.footer_text));
	      views.setImageViewResource(R.id.widget_play, R.drawable.player_footer_play_select);
	      
	      awm.updateAppWidget(new ComponentName(context, Mp3PlayerWidget.class), views);  
	}
	
	private  static void playAdd(Context context,int add){
		List<HashMap<String, Object>> list = MyMusicDB.getList(MyMusicDB.db_playmusic);
		if(list.isEmpty()){
			Toast.makeText(context, R.string.musitnotexit, Toast.LENGTH_SHORT).show();
		}else{
			MyMusicDB.musicPosition = (MyMusicDB.musicPosition + add + list.size())%list.size();
			PlayService.setMusicInfo(list.get(MyMusicDB.musicPosition));
			stop(context);
			play(context);
		}
	}
	
	public static void playNext(Context context){

		playAdd(context,1);
		
	}
	
	public static void playLast(Context context){
		playAdd(context,-1);
	}
	
	public static void loadService(Context context,ContentResolver resolver){
		List<HashMap<String, Object>> list = MyMusicDB.getList(MyMusicDB.db_playmusic);

		if(list.isEmpty()){
			GetMyMusicDB.LoadMyMusicDB(resolver, context);
		}

		
	}
	
	public static void getPosition(){

		String name = PlayService.getMusicInfo().getName();
		List<HashMap<String, Object>> list = MyMusicDB.getList(MyMusicDB.db_playmusic);

		HashMap<String, Object> map ;
		if(name == null || name.equals("")){
			MyMusicDB.musicPosition = 0;
			map = list.get(MyMusicDB.musicPosition);
			PlayService.setMusicInfo(map);
		}else{
			
			if(MyMusicDB.musicPosition < list.size() && name.equals(list.get(MyMusicDB.musicPosition).get("name"))){
				
			}else{
				boolean ok = true;
				if(MyMusicDB.musicPosition >= list.size() || !name.equals(list.get(MyMusicDB.musicPosition).get("name"))){
					for(int i = 0 ;i < list.size();i++){
						if(name.equals(list.get(i).get("name"))){
							MyMusicDB.musicPosition = i;
							map = list.get(MyMusicDB.musicPosition);
							PlayService.setMusicInfo(map);
							ok = false;
							break;
						}
					}

				}					
				if(ok){
					MyMusicDB.musicPosition = 0;
					map = list.get(MyMusicDB.musicPosition);
					PlayService.setMusicInfo(map);
				}
			}
			

		}

	}

	public static void Play(Context context,HashMap<String, Object>map){
		
		String name = (String)map.get("name");
		String path = (String)map.get("path");
		String artist = (String)map.get("artist");

		Intent intent = new Intent(context,PlayService.class);
		intent.putExtra("name", name);
		intent.putExtra("path", path);
		intent.putExtra("artist", artist);
		context.startService(intent);

	}
	
	public static void turnToPlayList(Context context,List<HashMap<String, Object>>playList){
		
		if(playList.isEmpty()){
			Toast.makeText(context, "这个列表为空，不能播放", Toast.LENGTH_SHORT).show();
		}else{
			stop(context);
			SetMyMusicDB.updateMusicList(context, MyMusicDB.db_playmusic, playList);
			GetMyMusicDB.getList(context, MyMusicDB.db_playmusic);
			MyMusicListActivity.updateMyList();
			stop(context);
			Play(context,playList.get(0));
		}
		
	}

	
	public static void playOrpause(Context context){
		

		if(MyMusicDB.playStatus == MyMusicDB.status_notplay){
			getPosition();
			play(context);
		}else if(MyMusicDB.playStatus == MyMusicDB.status_pause){
			goon(context);
		}else if(MyMusicDB.playStatus == MyMusicDB.status_playing){
			pause(context);
		}

	} 
	
	
	public static void trueToPlay(Context context,HashMap<String, Object>map){
		if(MyMusicDB.playStatus == MyMusicDB.status_playing && map.get("name").equals(PlayService.getMusicInfo().getName())){
			Toast.makeText(context, R.string.isplaying, Toast.LENGTH_SHORT).show();
		}else{
			ChangeMyMusicDB.addMusicToList(context, map, MyMusicDB.db_playmusic);
			stop(context);			
			Play(context,map);
		}
		
	}

	public static void stop(Context context){
		if(PlayService.player != null){
			PlayService.player.stop();
		}
		PlayerActivity.stop();
		loadWidgetStop(context);
		MyMusicDB.playStatus =MyMusicDB.status_notplay;
		MainPlayerActivity.footer.setText(R.string.player_musicname);
	}
	
	public static void release(Context context){
		
		if(PlayService.player != null){
			if(MyMusicDB.playStatus != MyMusicDB.status_notplay){
				stop(context);
			}
			PlayService.player.release();
			context.stopService(new Intent(context,PlayService.class));
			
		}
	}
	
	private static void goon(Context context){
		MyMusicDB.playStatus = MyMusicDB.status_playing;
		PlayService.player.start();
		loadWidgetPlay(context);
		PlayerActivity.goon();
	}

	public static void pause(Context context){
		PlayService.player.pause();
		loadWidgetPause(context);
		MyMusicDB.playStatus = MyMusicDB.status_pause;
		PlayerActivity.pause();
	}
	
	
	
}
