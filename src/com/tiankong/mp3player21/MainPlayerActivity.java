package com.tiankong.mp3player21;


import java.util.ArrayList;


import com.tiankong.mp3player21.db.MyMusicDB;
import com.tiankong.mp3player21.db.SetMyMusicDB;
import com.tiankong.mp3player21.list.AllMusicListActivity;
import com.tiankong.mp3player21.list.MyMusicListActivity;
import com.tiankong.mp3player21.list.PlayMusicListActivity;
import com.tiankong.mp3player21.service.PlayService;
import com.tiankong.mp3player21.service.ServiceHelp;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

public class MainPlayerActivity extends TabActivity{

	private static long myExitTime = -2001;
	public static Context context;
	public static TextView footer;
	private TabHost tabHost;
	private LocalActivityManager manager;
	private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.main_player_activity);
	    
	    footer = (TextView)findViewById(R.id.footer_text);
	    context = MainPlayerActivity.this;
	    
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
	    
		loadTabHost();
		loadViewPager();
		
		tabHost.setCurrentTab(MyMusicDB.TAB_ALL_MUSIC);
		footer.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				startActivity(new Intent(context,PlayerActivityHelp.class));
			}
		});
	}

	private Intent  MygetIntent(Class<?> cls){
		return new Intent(context,cls);
	}
	
	private void MyAddTab(Class<?>cls , int tabid ,int lableid ){
		TabSpec spec = tabHost.newTabSpec(getString(tabid));
		spec.setIndicator(getString(lableid));
		spec.setContent(MygetIntent(cls));
		tabHost.addTab(spec);		
	}
	
	private void MyAddTab(Class<?>cls , int id){
		MyAddTab(cls , id ,id );
	}
	
	private View MygetView(int id){
		return LayoutInflater.from(context).inflate(id, null);
	}
	
	private View MygetView(int id,Class<?>cls){
		return manager.startActivity(getString(id), MygetIntent(cls)).getDecorView();
	}
	
	private void loadTabHost(){
		tabHost = getTabHost();
		MyAddTab(MyMusicListActivity.class, R.string.main_tabs_mymusic);
		MyAddTab(AllMusicListActivity.class,R.string.main_tabs_allmusic);
		MyAddTab(PlayMusicListActivity.class, R.string.main_tabs_playmusic);
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			
			public void onTabChanged(String tabId) {
				if(tabId.equals(getString(R.string.main_tabs_mymusic))){
					viewPager.setCurrentItem(MyMusicDB.TAB_MY_MUSIC);
				}else if(tabId.equals(getString(R.string.main_tabs_allmusic))){
					viewPager.setCurrentItem(MyMusicDB.TAB_ALL_MUSIC);
				}else if(tabId.equals(getString(R.string.main_tabs_playmusic))){
					viewPager.setCurrentItem(MyMusicDB.TAB_PLAYING_MUSIC);
				} 
			}
		});
		

	}
	
	private void loadViewPager(){
		viewPager = (ViewPager)findViewById(R.id.main_viewpager);
		final ArrayList<View>lists = new ArrayList<View>();
		lists.add(MygetView(R.string.main_tabs_mymusic, MyMusicListActivity.class));
		lists.add(MygetView(R.string.main_tabs_allmusic, AllMusicListActivity.class));
		lists.add(MygetView(R.string.main_tabs_playmusic, PlayMusicListActivity.class));
		
		viewPager.setAdapter(new PagerAdapter() {
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getCount() {
				return lists.size();
			}

			
			@Override
			public void destroyItem(View container, int position, Object object) {
				viewPager.removeView(lists.get(position));
			}

			
			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager)container).addView(lists.get(position));
				return lists.get(position);
			}

			
			@Override
			public void finishUpdate(View container) {

			}

			
			@Override
			public void restoreState(Parcelable state, ClassLoader loader) {

			}

			
			@Override
			public Parcelable saveState() {
				return null;
			}

			
			@Override
			public void startUpdate(View container) {

			}

			
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			
			public void onPageSelected(int arg0) {
				tabHost.setCurrentTab(arg0);
			}
			
			
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
	}

	public  void MyExit(){
//		new AsyncTask<Void, Void, Void>(){
//
//			@Override
//			protected Void doInBackground(Void... params) {
//				SetMyMusicDB.updateList(context);
//				return null;
//			}
//			
//		}.execute();
//		SetMyMusicDB.updateList(context);
		ServiceHelp.release(getApplicationContext());

		((Activity) AllMusicListActivity.context).finish();
		((Activity) MyMusicListActivity.context).finish();
		((Activity) PlayMusicListActivity.context).finish();
		finish();
	} 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			if(System.currentTimeMillis() - myExitTime >2000){
				Toast.makeText(context, R.string.resumeexit, Toast.LENGTH_SHORT).show();
				myExitTime = System.currentTimeMillis();
			}else{
				MyExit();
			}
		}
		return true;
	}
	
	@Override
	protected void onDestroy() {
		context = null;
		super.onDestroy();
	}
	
}
