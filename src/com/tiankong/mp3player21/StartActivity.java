package com.tiankong.mp3player21;


import com.tiankong.mp3player21.db.GetMyMusicDB;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

public class StartActivity extends Activity {

	Context context = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.start_activity);
 
        context = this;

        
    	new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				
				GetMyMusicDB.LoadMyMusicDB(getContentResolver(), context);
				
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				startActivity(new Intent(context,MainPlayerActivity.class));
				finish();
				super.onPostExecute(result);
			}
			
			
			
		}.execute();
    	
    }


}
