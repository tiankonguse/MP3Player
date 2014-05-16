package com.tiankong.mp3player21.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tiankong.mp3player21.MainPlayerActivity;
import com.tiankong.mp3player21.R;
import com.tiankong.mp3player21.db.ChangeMyMusicDB;
import com.tiankong.mp3player21.db.MyMusicDB;
import com.tiankong.mp3player21.service.PlayService;
import com.tiankong.mp3player21.service.ServiceHelp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PlayMusicListActivity extends Activity{

	public static Context context ;
	private static ListView playMusicListView;
	private static MyListAdapter myListAdapter; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_music_list_activity);
		
		context = this;
		
		playMusicListView = (ListView)findViewById(R.id.play_musiclists);
		myListAdapter = new MyListAdapter(context, MyMusicDB.getList(MyMusicDB.db_playmusic));
		
		playMusicListView.setAdapter(myListAdapter);
		playMusicListView.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object>map = MyMusicDB.getList(MyMusicDB.db_playmusic).get(arg2);
				if(MyMusicDB.playStatus == MyMusicDB.status_playing && map.get("name").equals(PlayService.getMusicInfo().getName())){
					Toast.makeText(context, R.string.isplaying, Toast.LENGTH_SHORT).show();
				}else{
					ServiceHelp.trueToPlay(context, map);
				}
			}
		});
		
	}

	public static void updatePlayList(){
		myListAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onRestart() {
		super.onRestart();
	}

	
	@Override
	public void finish() {
		super.finish();
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return ((MainPlayerActivity) MainPlayerActivity.context).onKeyDown(keyCode,event);
		}
		return false;
	}

	private class MyListAdapter extends BaseAdapter{
		
		private class ButtonViewHolder{
			public TextView    musicName;
			public ImageButton delete;
		}
		
		private ArrayList<HashMap<String, Object>>musicList;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;
		
		public MyListAdapter(Context context,List<HashMap<String, Object>> musicList) {
			this.musicList = (ArrayList<HashMap<String, Object>>)musicList;
			this.myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		
		public int getCount() {
			return musicList.size();
		}

		
		public Object getItem(int arg0) {
			return musicList.get(arg0);
		}

		
		public long getItemId(int position) {
			return position;
		}

		public void removeItem(int position){
			if(MyMusicDB.playStatus != MyMusicDB.status_notplay && musicList.get(position).get("name").equals(PlayService.getMusicInfo().getName())){
				PlayService.player.stop();
				MainPlayerActivity.footer.setText(R.string.footer_text);
			}
			ChangeMyMusicDB.delMusicFromList(getApplicationContext(), musicList.get(position), MyMusicDB.db_playmusic);
			this.notifyDataSetChanged();
		}
		
		
		public View getView(int position, View convertView, ViewGroup parent) {

			if(convertView != null){
				this.myHolder = (ButtonViewHolder)convertView.getTag();
			}else{
				convertView = myInflater.inflate(R.layout.play_music_list_item, null);
				this.myHolder = new ButtonViewHolder();
				this.myHolder.musicName = (TextView)convertView.findViewById(R.id.play_list_item_name);
				this.myHolder.delete = (ImageButton)convertView.findViewById(R.id.play_list_item_delete);
				convertView.setTag(myHolder);
			}
			
			HashMap<String, Object>map = MyMusicDB.getList(MyMusicDB.db_playmusic).get(position);
			if(map != null){
				String name = (String)map.get("name");
				myHolder.musicName.setText(name);
				myHolder.delete.setOnClickListener(new deleOnClickListener(position));
			}
			
			return convertView;
		}
		
		class deleOnClickListener implements OnClickListener{

			private int position;
			
			public deleOnClickListener(int position){
				this.position = position;
			}
			
			
			public void onClick(View v) {
				removeItem(position);
				
			}
			
		}
		
	}

	  //添加菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MyMusicDB.MENU_LIST_PLAY_DELALL, 1, R.string.menu_list_play_del);
    	menu.add(0, MyMusicDB.MENU_EXIT, 2, R.string.menu_exit);
        return true;
    }
 
  //菜单项处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           if(item.getItemId() == MyMusicDB.MENU_EXIT){
        	   ((MainPlayerActivity)MainPlayerActivity.context).MyExit();
          }else if(item.getItemId()==MyMusicDB.MENU_LIST_PLAY_DELALL){
        	 ChangeMyMusicDB.clearMusicList(getApplicationContext(), MyMusicDB.db_playmusic);
        	 updatePlayList();
          }
           return super .onOptionsItemSelected(item);
   }
	
	
}
