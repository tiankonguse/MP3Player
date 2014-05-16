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
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

public class AllMusicListActivity extends Activity{
	
	public  static Context context;
	private static MyListAdapter mylistAdapter;
	private static ListView allMusicListView ;
	
	private List<HashMap<String, Object>>allMusicList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_music_list_activity);
		
		context = this;
		
		allMusicListView = (ListView)findViewById(R.id.all_musiclists);
		
		allMusicList = MyMusicDB.getList(MyMusicDB.db_allmusic);
		mylistAdapter = new MyListAdapter(context, allMusicList);

		allMusicListView.setAdapter(mylistAdapter);
		allMusicListView.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, Object>map = MyMusicDB.getList(MyMusicDB.db_allmusic).get(arg2);
				if(MyMusicDB.playStatus == MyMusicDB.status_playing && map.get("name").equals(PlayService.getMusicInfo().getName())){
					Toast.makeText(context, R.string.isplaying, Toast.LENGTH_SHORT).show();
				}else{
					ServiceHelp.trueToPlay(context, map);
				}
						
			}

			
			
			
		
		});

	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return ((MainPlayerActivity) MainPlayerActivity.context).onKeyDown(keyCode,event);
		}
		return false;
	}

	public static void updateAllList(){
		mylistAdapter.notifyDataSetChanged();
	}

	private class MyListAdapter extends BaseAdapter{
		
		private class ButtonViewHolder{
			TextView musicName;
			ImageButton add;
			ImageButton like;
			ImageButton del;
		}
		
		private List<HashMap<String, Object>>musicList;
		private LayoutInflater myInflater;
		private ButtonViewHolder myHolder;

		public MyListAdapter(Context myContext,List<HashMap<String, Object>> musicList) {
			this.musicList = (ArrayList<HashMap<String,Object>>)musicList;
			this.myInflater = (LayoutInflater)(myContext).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			ChangeMyMusicDB.delMusicFromList(getApplicationContext(), musicList.get(position), MyMusicDB.db_allmusic);
			this.notifyDataSetChanged();
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			
			if(convertView != null){
				myHolder =(ButtonViewHolder)convertView.getTag();
			}else{
				convertView = myInflater.inflate(R.layout.all_music_list_item, null);
				myHolder = new ButtonViewHolder();
				myHolder.musicName = (TextView)convertView.findViewById(R.id.all_item_name);
				myHolder.add = (ImageButton)convertView.findViewById(R.id.all_item_add);
				myHolder.like = (ImageButton)convertView.findViewById(R.id.all_item_like);
				myHolder.del = (ImageButton)convertView.findViewById(R.id.all_item_del);
				convertView.setTag(myHolder);				
			}
			
			HashMap<String, Object>itemInfo = musicList.get(position);
			
			if(itemInfo != null){
				String name = (String)itemInfo.get("name");
				myHolder.musicName.setText(name);
				myHolder.add.setOnClickListener(new AddOnClickListence(position));
				myHolder.like.setOnClickListener(new LikeOnClickListener(position));
				myHolder.del.setOnClickListener(new DelOnClickListence(position));
			}
			
			return convertView;
		}
		
		class LikeOnClickListener implements OnClickListener{
			
			int position;
			
			public LikeOnClickListener(int position){
				this.position = position;
			}
			
			public void onClick(View v) {
				Builder builder = new AlertDialog.Builder(context);
				builder.setIcon(R.drawable.alert_like);
				builder.setTitle(getString(R.string.seletelist));
				String[] items = MyMusicDB.getMusicTypeString();
				
				builder.setItems(items, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						
						HashMap<String, Object>map = MyMusicDB.getList(MyMusicDB.db_allmusic).get(position);
						String mytype = MyMusicDB.getMusicTypeList().get(which);
						ChangeMyMusicDB.addMusicToMyList(getApplicationContext(), map, mytype);
						MyMusicListActivity.updateMyList();
					}
				});
				builder.create().show();
			}
			
		}
		
		class AddOnClickListence implements OnClickListener{
			
			private int position ;
			
			public AddOnClickListence(int position){
				this.position = position;
			}
			
			public void onClick(View v) {
				HashMap<String, Object>map = MyMusicDB.getList(MyMusicDB.db_allmusic).get(position);
				ChangeMyMusicDB.addMusicToList(context, map, MyMusicDB.db_playmusic);
				PlayMusicListActivity.updatePlayList();
			}
			
		}
		
		class DelOnClickListence implements OnClickListener{
			
			private int position ;
			
			public DelOnClickListence(int position){
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
    	menu.add(0, MyMusicDB.MENU_LIST_ALL_PLAYALL, 1, R.string.menu_list_all_playall);
    	menu.add(0, MyMusicDB.MENU_LIST_ALL_UPDATE, 2, R.string.menu_list_all_update);
    	menu.add(0, MyMusicDB.MENU_EXIT, 3, R.string.menu_exit);
        return true;
    }
 
  //菜单项处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           if(item.getItemId() == MyMusicDB.MENU_EXIT){
        	  ((MainPlayerActivity)MainPlayerActivity.context).MyExit();
          }else if(item.getItemId()==MyMusicDB.MENU_LIST_ALL_PLAYALL){
        	  ServiceHelp.turnToPlayList(getApplicationContext(), allMusicList);
          }else if(item.getItemId() == MyMusicDB.MENU_LIST_ALL_UPDATE){
        	  ChangeMyMusicDB.updateMyMusicList(getContentResolver(), getApplicationContext());
        	  updateAllList();
          }
           return super .onOptionsItemSelected(item);
   }
}
