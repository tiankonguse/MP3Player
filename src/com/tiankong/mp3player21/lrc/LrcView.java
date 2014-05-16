package com.tiankong.mp3player21.lrc;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.view.View;


public final class LrcView extends View{

	
	private static SongLrc lrc = null;
	private static long time = 0l;
	private static Paint fontPaint = null;
	private static Paint lrcPaint = null;
	private static int fontColor = Color.GRAY;
	private static int lrcColor = Color.WHITE;
	private static int fontSize = 22;
	private static int lrcSize = 22;
	private static int height = 0;
	
	public LrcView(Context context) {
		super(context);
		height =getHeight();
	}


	public void setLrc(SongLrc lrc) {
		this.lrc = lrc;
	}


	public void setTime(long time) {
		this.time = time;
	}


	
	public void setFontColor(int fontColor) {
		this.fontColor = fontColor;
	}


	
	public void setLrcColor(int lrcColor) {
		this.lrcColor = lrcColor;
	}


	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}


	
	
	
	public Paint getFontPaint() {
		if(fontPaint == null){
			fontPaint = new Paint();
		}
		return fontPaint;
	}


	public Paint getLrcPaint() {
		if(lrcPaint == null){
			lrcPaint = new Paint();
		}
		return lrcPaint;
	}

	
	public void MySetPaint(Paint paint,int color,int size){
		paint.setColor(color);
		paint.setTextSize(size);
	}
	
	private int getStartPosition(int len){
		return (getWidth() - len*fontSize)/2;
	}

	public static void clear(){
		
	} 

	 

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(lrc != null){
			try {
				MySetPaint(getFontPaint(),fontColor,fontSize);
				MySetPaint(getLrcPaint(),lrcColor,lrcSize);
				
				int index = lrc.getIndex(time);
				int height = getHeight()/2 - index*fontSize*3/2 - (int)((fontSize*3/2)*(lrc.getOffset(time)/(float)lrc.getNextTime(time)));
				
				Long[] lrcLongs = lrc.getLrcLongs();
				for(Long now: lrcLongs){
					String nowLrc = lrc.getLrc(now);
					canvas.drawText(nowLrc, getStartPosition(nowLrc.length()), height, lrc.getIndex(now) == index ?lrcPaint:fontPaint);
					
					height += fontSize*3/2;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	
}
