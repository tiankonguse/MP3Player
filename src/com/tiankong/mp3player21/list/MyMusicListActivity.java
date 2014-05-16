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

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ExpandableListActivity;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyMusicListActivity extends ExpandableListActivity{
	

	
	public static Context context ;
	private static MyMusicListAdapter listAdapter;
	private static ImageView addList;
	
	
	private ExpandableListView myMusicListView;
	private List<String> groupList;
	private List<ArrayList<HashMap<String, Object>>> childLists;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_music_list_activity);
		context = this;
		
		myMusicListView = getExpandableListView();
		addList = (ImageView)findViewById(R.id.my_music_list_add);
		
		groupList = MyMusicDB.getMusicTypeList();
		childLists =MyMusicDB.getMyMusicLists();
		
		listAdapter = new MyMusicListAdapter(groupList, childLists, context);
		myMusicListView.setAdapter(listAdapter);
		
		addList.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				addListDialog();				
			}
		});
	}

	public static void updateMyList(){
		listAdapter.notifyDataSetChanged();
	}

	public void addListDialog(){
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.my_music_list_adddialog, (ViewGroup) findViewById(R.id.my_music_list_add_dialog));

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.my_music_list_add_headname);
		builder.setView(layout);
		builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				EditText editText = (EditText)layout.findViewById(R.id.my_music_list_add_listname);
				String typeName = editText.getText().toString();
				ChangeMyMusicDB.addTypeToMyMusic(context, typeName);
				updateMyList();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
			
		});
		builder.show();
	}
		
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		HashMap<String, Object>map = childLists.get(groupPosition).get(childPosition);
		if(MyMusicDB.playStatus == MyMusicDB.status_playing && map.get("name").equals(PlayService.getMusicInfo().getName())){
			Toast.makeText(context, R.string.isplaying, Toast.LENGTH_SHORT).show();
		}else{
			ServiceHelp.trueToPlay(context, map);
		}
				
		return super.onChildClick(parent, v, groupPosition, childPosition, id);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			return ((MainPlayerActivity) MainPlayerActivity.context).onKeyDown(keyCode,event);
		}
		return false;
	}

	@Override
	public void finish() {
		super.finish();
	}


	
	public void backDialog(){
		AlertDialog.Builder builder = new Builder(context);
		builder.setTitle("删除列表");
		builder.setMessage("列表不为空，你确认要删除吗？");
		builder.setPositiveButton("是的，删除", new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();//dismiss()是关闭对话框
			}
		});
		builder.setNegativeButton("哦，按错了", new DialogInterface.OnClickListener() {
			
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		builder.create().show();
	}
	
	class MyMusicListAdapter extends BaseExpandableListAdapter{

		private List<String>groups;
		private List<ArrayList<HashMap<String, Object>>>childs;
		private ChildViewHolder childHolder;
		private GroupViewHolder groupHolder; 
		private Context context;
		
		public MyMusicListAdapter(
				List<String> groups,
				List<ArrayList<HashMap<String, Object>>> childs, 
				Context context) {
			this.groups = groups;
			this.childs = childs;
			this.context = context;
		}

		
		public Object getChild(int groupPosition, int childPosition) {
			return childs.get(groupPosition).get(childPosition);
		}

		
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if(convertView != null){
				childHolder = (ChildViewHolder)convertView.getTag();
			}else{
				childHolder = new ChildViewHolder();
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.my_music_list_item, null);
				childHolder.musicName = (TextView)convertView.findViewById(R.id.my_list_item_name);
				childHolder.musicDelete = (ImageButton)convertView.findViewById(R.id.my_list_item_delete);
				convertView.setTag(childHolder);
			}
			HashMap<String, Object>map = childs.get(groupPosition).get(childPosition);
			if(map != null){
				String name = (String)map.get("name");
				childHolder.musicName.setText(name);
				childHolder.musicDelete.setOnClickListener(new ChildDeleteOnClickListener(groupPosition,childPosition));
			}
			return convertView;
		}

		
		public int getChildrenCount(int groupPosition) {
			return childs.get(groupPosition).size();
		}

		
		public Object getGroup(int groupPosition) {
			return groups.get(groupPosition);
		}

		
		public int getGroupCount() {
			return groups.size();
		}

		
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if(convertView != null){
				groupHolder = (GroupViewHolder)convertView.getTag();
			}else{
				groupHolder = new GroupViewHolder();
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.my_music_list_group, null);
				groupHolder.groupName = (TextView)convertView.findViewById(R.id.my_music_list_group_name);
				groupHolder.groupPlay = (ImageButton)convertView.findViewById(R.id.my_list_group_play);
				groupHolder.groupDelete = (ImageButton)convertView.findViewById(R.id.my_list_group_delet);
				convertView.setTag(groupHolder);
			}
			
			String name = groups.get(groupPosition);
			if(name != null && !name.equals("")){
				groupHolder.groupName.setText(name);
				groupHolder.groupPlay.setOnClickListener(new GroupPlayOnClickListener(groupPosition));
				groupHolder.groupDelete.setOnClickListener(new GroupDeleteOnClickListener(groupPosition));
			}
			return convertView;
		}

		
		public boolean hasStableIds() {
			return false;
		}

		
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
		public void removeChildItem(int groupPosition, int childPosition){
			ChangeMyMusicDB.delMusicFromMyList(getApplicationContext(), childs.get(groupPosition).get(childPosition), groups.get(groupPosition));
			this.notifyDataSetChanged();
		}

		public void removeGroupItem(int groupPosition){
			if(!childs.get(groupPosition).isEmpty()){
				Toast.makeText(getApplicationContext(), "列表不为空，不能删除", Toast.LENGTH_SHORT).show();
			}else{
				ChangeMyMusicDB.delTypeFromMyMusic(getApplicationContext(), groups.get(groupPosition));
			}
			this.notifyDataSetChanged();
		}
		
		private class ChildViewHolder{
			TextView musicName;
			ImageButton musicDelete;
		}
		
		private class GroupViewHolder{
			TextView groupName;
			ImageButton groupDelete;
			ImageButton groupPlay;
		}
		
		private class ChildDeleteOnClickListener implements OnClickListener{
			
			private int groupPosition;
			private int childPosition;
			
			
			
			public ChildDeleteOnClickListener(int groupPosition, int childPosition) {
				this.groupPosition = groupPosition;
				this.childPosition = childPosition;
			}



			
			public void onClick(View v) {
				removeChildItem(groupPosition, childPosition);				
			}
			
		}
		
		public class  GroupPlayOnClickListener implements OnClickListener{

			private int position;
			
			public GroupPlayOnClickListener(int position) {
				this.position = position;
			}

			
			public void onClick(View v) {
					List<HashMap<String, Object>>playList = MyMusicDB.getMyMusicLists().get(position);
					ServiceHelp.turnToPlayList(getApplicationContext(),playList);
			}
			
		}
		
		public class  GroupDeleteOnClickListener implements OnClickListener{

			private int position;

			public GroupDeleteOnClickListener(int position) {
				this.position = position;
			}

			
			public void onClick(View v) {
				removeGroupItem(position);
			}
			
		}
		
	}
	
	
	  //添加菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(0, MyMusicDB.MENU_LIST_MY_ADD, 1, R.string.menu_list_my_add);
    	menu.add(0, MyMusicDB.MENU_EXIT, 2, R.string.menu_exit);
        return true;
    }
 
  //菜单项处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
           if(item.getItemId() == MyMusicDB.MENU_EXIT){
        	   ((MainPlayerActivity)MainPlayerActivity.context).MyExit();
          }else if(item.getItemId()==MyMusicDB.MENU_LIST_MY_ADD){
        	  addListDialog();
          }
           return super .onOptionsItemSelected(item);
   }
}
