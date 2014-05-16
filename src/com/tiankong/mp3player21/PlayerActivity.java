package com.tiankong.mp3player21;

import java.io.File;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.tiankong.mp3player21.db.MyMusicDB;
import com.tiankong.mp3player21.lrc.LrcView;
import com.tiankong.mp3player21.lrc.SongLrc;
import com.tiankong.mp3player21.service.PlayService;
import com.tiankong.mp3player21.widet.Mp3PlayerWidget;


public class PlayerActivity extends Activity{

	public static int NUMBERPLAYER = 0;
	public static Context context;
	private static TextView musicName;
	private static SeekBar seekBar;
	private static Button lastButton;
	private static Button playButton;
	private static Button stopButton;
	private static Button nextButton;
	
	private static String name="";
	private static String path="";
	private static Handler handler;
	private static boolean isChangeSeekbar;
	private static Runnable runnable;
	
	private static SongLrc lrc;
	private static LrcView lrcView;
	private static long time = 0;
	private static LinearLayout lrcLayout;
	private static String lrcPath;
	
	private static TextView nowTime ;
	private static TextView allTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		Log.i("PlayerActivity", "On create");
		super.onCreate(savedInstanceState);
		NUMBERPLAYER++;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.player_activity);
		
		context = this;
		
		getAllId();
		
		lrcView = new LrcView(lrcLayout.getContext());
		lrcLayout.addView(lrcView);
		
		lastButton.setOnClickListener(new ButtonListener());
		playButton.setOnClickListener(new ButtonListener());
		nextButton.setOnClickListener(new ButtonListener());
		stopButton.setOnClickListener(new ButtonListener());
		seekBar.setOnSeekBarChangeListener(new myOnSeekBarListener());

		
		
		handler = new Handler();
		runnable = new Runnable() {
			
			public void run() {
//				Log.i("PlayerActivity", "name-->"+PlayService.getMusicInfo().getName());
//				Log.i("PlayerActivity", "position-->"+MyMusicDB.musicPosition);
				if(MyMusicDB.playStatus == MyMusicDB.status_playing){
					seekBar.setMax(PlayService.getMaxMilliSeconds());
					seekBar.setProgress(PlayService.getCurrentMilliSecond());
					
//					Log.i("PlayerActivity", "SongLrc begin");
					SongLrc.setMaxTime(PlayService.player.getDuration());
					time = PlayService.player.getCurrentPosition();
					if(new File(lrcPath).exists()){
						lrc = new SongLrc(lrcPath);
						lrcView.setVisibility(View.VISIBLE);
						lrcView.setLrc(lrc);
						lrcView.setTime(time);
						lrcView.postInvalidate();
					}else{
						lrcView.setVisibility(View.INVISIBLE);
					}
//					Log.i("PlayerActivity", "SongLrc begin");
					nowTime.setText(""+SongLrc.LongToString((int)time));
					allTime.setText(""+SongLrc.LongToString((int)SongLrc.getMaxTime()));

				}
				if(!name.equals(PlayService.getMusicInfo().getName())){
					myInit();
				}
				handler.postDelayed(this, 50);
			}
		};

	}

	private void getAllId(){

		musicName  = (TextView)findViewById(R.id.player_musicname);
		
		seekBar = (SeekBar)findViewById(R.id.player_seekbar);
		
		lastButton = (Button)findViewById(R.id.player_footer_last);
		playButton = (Button)findViewById(R.id.player_footer_play);
		stopButton = (Button)findViewById(R.id.player_footer_stop);
		nextButton = (Button)findViewById(R.id.player_footer_next);
		
		lrcLayout = (LinearLayout)findViewById(R.id.player_lrc);
		
		nowTime = (TextView)findViewById(R.id.player_time_now);
		allTime = (TextView)findViewById(R.id.player_time_all);
	} 
	
	private static void myInit(){
		name = PlayService.getMusicInfo().getName();
		path = PlayService.getMusicInfo().getPath();
		lrcPath = path.substring(0,path.length()-4)+".lrc";
	}
	
	@Override
	protected void onStart() {
		
//		Log.i("PlayerActivity", "On Start begin");
		if(MyMusicDB.playStatus != MyMusicDB.status_notplay){
			myInit();
			if(MyMusicDB.playStatus == MyMusicDB.status_playing){
				handler.post(runnable);
				musicName.setText(name);
				playButton.setBackgroundResource(R.drawable.player_footer_pause_select);					
			}else if(MyMusicDB.playStatus == MyMusicDB.status_pause){
				playButton.setBackgroundResource(R.drawable.player_footer_play_select);
			}
		}
		super.onResume();
//		Log.i("PlayerActivity", "On Start end");
	}
	
	@Override
	protected void onStop() {
		
		handler.removeCallbacks(runnable);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		NUMBERPLAYER--;
		context = null;
		super.onDestroy();
	}

	public static void pause(){
		if(context != null){
			myInit();
			musicName.setText(name);
			handler.removeCallbacks(runnable);
			playButton.setBackgroundResource(R.drawable.player_footer_play_select);
		}
	}
	
	public static void play(){
		if(context != null){
			myInit();
			musicName.setText(name);
			playButton.setBackgroundResource(R.drawable.player_footer_pause_select);
			handler.post(runnable);
		}

	}
	
	public static void stop(){
		if(context != null){
			lrcView.setVisibility(View.INVISIBLE);
			handler.removeCallbacks(runnable);
			playButton.setBackgroundResource(R.drawable.player_footer_play_select);
			musicName.setText(R.string.player_musicname);
			seekBar.setProgress(0);
			nowTime.setText(R.string.player_time_now);
			allTime.setText(R.string.player_time_all);		
		}
	}
	
	public static void goon(){
		if(context != null){
			myInit();
			musicName.setText(name);
			playButton.setBackgroundResource(R.drawable.player_footer_pause_select);
			handler.post(runnable);
		}
	} 
	
	private class ButtonListener implements OnClickListener{

		
		public void onClick(View v) {
			Intent intent=new Intent(MyMusicDB.EXTRA_SERVER_MUSIC);
			switch (v.getId()) {
			case R.id.player_footer_last:
				intent.setFlags(MyMusicDB.FLAG_LAST_MUSIC);
				break;
			case R.id.player_footer_play:
				intent.setFlags(MyMusicDB.FLAG_PLAY_MUSIC);
				break;
			case R.id.player_footer_next:
				intent.setFlags(MyMusicDB.FLAG_NEXT_MUSIC);
				break;
			case R.id.player_footer_stop:
				intent.setFlags(MyMusicDB.FLAG_STOP_MUSIC);
				break;

			default:
				break;
			}
			context.startService(intent);
		}
		
	}
	
	
	private class myOnSeekBarListener implements OnSeekBarChangeListener{

		
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			
		}

		
		public void onStartTrackingTouch(SeekBar seekBar) {
			if(MyMusicDB.playStatus != MyMusicDB.status_notplay){
				handler.removeCallbacks(runnable);
				isChangeSeekbar = true;
			}
			
		}

		
		public void onStopTrackingTouch(SeekBar seekBar) {
			if(isChangeSeekbar){
				PlayService.player.seekTo(seekBar.getProgress());
				handler.post(runnable);
				isChangeSeekbar = false;				
			}
			
		}
		
	}
}