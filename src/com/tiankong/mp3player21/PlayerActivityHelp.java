package com.tiankong.mp3player21;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class PlayerActivityHelp extends Activity{

	private static Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tmp_activity);
        context = this;
		if (PlayerActivity.NUMBERPLAYER >0) {
			((Activity) PlayerActivity.context).finish();
		}
//		Log.i("Mp3PlayerWidget", "NUMBERPLAYER-->"+PlayerActivity.NUMBERPLAYER);

		context.startActivity(new Intent(context,PlayerActivity.class));
        finish();
	}

	

	
	
	
}
