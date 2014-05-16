package com.tiankong.mp3player21.widet;

import com.tiankong.mp3player21.PlayerActivity;
import com.tiankong.mp3player21.PlayerActivityHelp;
import com.tiankong.mp3player21.R;
import com.tiankong.mp3player21.db.MyMusicDB;
import com.tiankong.mp3player21.service.PlayService;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;


public class Mp3PlayerWidget extends AppWidgetProvider{

	RemoteViews views = null;

	
	
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		 final int N = appWidgetIds.length;
//		 Log.i("Mp3PlayerWidget", "--->"+context.toString());
//		 Log.i("Mp3PlayerWidget", "--->"+N);
	        for (int i=0; i<N; i++) {
	        	if(i==0){
		            int appWidgetId = appWidgetIds[i];
		            setMyFirstWidget(context,appWidgetManager,appWidgetId);
	        	}
	        }
	}
	
	void setMyFirstWidget(
			Context context, 
			AppWidgetManager appWidgetManager,
			int appWidgetId){
		getWidegetImage(context,appWidgetManager,appWidgetId,R.id.widget_image);
		getWidgetButton(context,appWidgetManager,appWidgetId,MyMusicDB.EXTRA_PLAY_MUSIC,R.id.widget_play);
        getWidgetButton(context,appWidgetManager,appWidgetId,MyMusicDB.EXTRA_STOP_MUSIC,R.id.widget_stop);
        getWidgetButton(context,appWidgetManager,appWidgetId,MyMusicDB.EXTRA_LAST_MUSIC,R.id.widget_last);
        getWidgetButton(context,appWidgetManager,appWidgetId,MyMusicDB.EXTRA_NEXT_MUSIC,R.id.widget_next);
  
	}

	void getWidegetImage(
			Context context, 
			AppWidgetManager appWidgetManager,
			int appWidgetId,
			int id){
		Intent intent = new Intent(context, PlayerActivityHelp.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_music_widget);
        views.setOnClickPendingIntent(id, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
	}
	
	void getWidgetButton(			
			Context context, 
			AppWidgetManager appWidgetManager,
			int appWidgetId,			
			String flag,
			int id){
		Intent intent = new Intent(flag);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_music_widget);
        views.setOnClickPendingIntent(id, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
//		Log.i("Mp3PlayerWidget", "receive");
//		Log.i("Mp3PlayerWidget", "Action-->"+intent.getAction());
//		Log.i("Mp3PlayerWidget", "flag string-->"+intent.getStringExtra("flag"));
//		Log.i("Mp3PlayerWidget", "flag int-->"+intent.getFlags());
//		
		String flag = intent.getAction();
//		Log.i("Mp3PlayerWidget", "flag--->"+flag);
		if(MyMusicDB.EXTRA_IMAGE_MUSIC.equals(flag)){
//			Log.i("Mp3PlayerWidget", "image");
//			Log.i("Mp3PlayerWidget", "NUMBERPLAYER-->"+PlayerActivity.NUMBERPLAYER);
				
			while (PlayerActivity.NUMBERPLAYER >0) {
					((Activity) PlayerActivity.context).finish();
//					Log.i("Mp3PlayerWidget", "NUMBERPLAYER-->"+PlayerActivity.NUMBERPLAYER);
			}
//			Log.i("Mp3PlayerWidget", "NUMBERPLAYER-->"+PlayerActivity.NUMBERPLAYER);

			context.startActivity(new Intent(context,PlayerActivity.class));
		}else if(MyMusicDB.EXTRA_LAST_MUSIC.equals(flag)){
//			Log.i("Mp3PlayerWidget", "last");
			intent=new Intent(MyMusicDB.EXTRA_SERVER_MUSIC);
			intent.setFlags(MyMusicDB.FLAG_LAST_MUSIC);
			context.startService(intent);
			
		}if(MyMusicDB.EXTRA_PLAY_MUSIC.equals(flag)){
//			Log.i("Mp3PlayerWidget", "play");
			intent=new Intent(MyMusicDB.EXTRA_SERVER_MUSIC);
			intent.setFlags(MyMusicDB.FLAG_PLAY_MUSIC);
			context.startService(intent);
			
		}if(MyMusicDB.EXTRA_STOP_MUSIC.equals(flag)){
//			Log.i("Mp3PlayerWidget", "stop");
			intent=new Intent(MyMusicDB.EXTRA_SERVER_MUSIC);
			intent.setFlags(MyMusicDB.FLAG_STOP_MUSIC);
			context.startService(intent);
			
		}if(MyMusicDB.EXTRA_NEXT_MUSIC.equals(flag)){
//			Log.i("Mp3PlayerWidget", "next");
			intent=new Intent(MyMusicDB.EXTRA_SERVER_MUSIC);
			intent.setFlags(MyMusicDB.FLAG_NEXT_MUSIC);
			context.startService(intent);
			
		}else{
			
		}
		
	}
	
	
}
