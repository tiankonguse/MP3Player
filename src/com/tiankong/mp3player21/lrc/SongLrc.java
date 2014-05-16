package com.tiankong.mp3player21.lrc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.integer;

public final class SongLrc {
	
	private String title = "";
	private String artist = "";
	
	private static long maxTime = 0;
	
	private Map<Long, String>lrcMap = new HashMap<Long, String>();
	private Long[] lrcLongs = null;
	
	private boolean valid = false;
	
	public SongLrc(String url){
		File file = new File(url);
		
		if(file.exists()){
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
				String line = null;
				while((line = br.readLine()) != null){
					dealLine(line);
				}
				mySetLrcLongs();
				valid = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private void MySortLrcLongs(){
		for(int i =0; i < lrcLongs.length-1 ;i++){
			for(int j = i+1; j < lrcLongs.length;j++){
				if(lrcLongs[i] > lrcLongs[j]){
					Long temp = lrcLongs[i];
					lrcLongs[i] = lrcLongs[j];
					lrcLongs[j] = temp;
				}
			}
		}
	}
	
	private void mySetLrcLongs(){
		lrcLongs = new Long[lrcMap.size()];;
		int index = 0;
		for(Long now:lrcMap.keySet()){
			lrcLongs[index++]=now;
		}
		MySortLrcLongs();
	} 
	
	private void dealLine(String line){
		
		if(line != null && !line.equals("")){
			if(line.startsWith("[ti:")){
				title = line.substring(4,line.length()-1);
			}else if(line.startsWith("[ar:")){
				artist = line.substring(4,line.length()-1);
			}else{
				Pattern pattern = Pattern.compile("\\[(\\d{2}:\\d{2}\\.\\d{2})\\]");
				Matcher matcher = pattern.matcher(line);
				while(matcher.find()){
					long time = StringToLong(matcher.group(1));
					String[] s0 = pattern.split(line);
					String lrc = s0.length > 0 ? s0[s0.length - 1]:"";
					lrcMap.put(time, lrc);
					if(maxTime <= time)maxTime = time;
				}
				
			}
		}
	}
	
	
	public static long StringToLong(String timeStr){
		String[] s0 = timeStr.split(":");
		int min = Integer.parseInt(s0[0]);
		String[] s1 = s0[1].split("\\.");
		int sec = Integer.parseInt(s1[0]);
		int mill = Integer.parseInt(s1[1]);
		return min*60*1000 + sec*1000 + mill*10;
	}
	
	public static String LongToString(long now){
		int time = (int)(now/1000);
		int min = time/60;
		int sec = time%60;
		
		StringBuffer sb = new StringBuffer();
		sb.append(min < 10 ? ("0"+min+":"):(min+":"));
		sb.append(sec < 10 ? ("0"+sec):(sec+""));
		
		return sb.toString();
	}

	
	public String getTitle() {
		return title;
	}

	
	public String getArtist() {
		return artist;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public static long getMaxTime() {
		return maxTime;
	}
	
	
	
	public Long[] getLrcLongs() {
		return lrcLongs;
	}

	public static void setMaxTime(long maxtime) {
		maxTime = maxtime;
	}

	
	/**
	 * 得到当前时间需要显示的歌词
	 * @param now
	 * @return
	 */
	public String getLrc(long now){
		Long curr = -1L;
		for (Long newTime : lrcMap.keySet()) {
			if(newTime <= now && newTime >= curr){
				curr = newTime;
			}
		}
		return lrcMap.get(curr);
	}
	
	/**
	 * 获得这行歌词的索引
	 * @return
	 */
	public int getIndex(long now){
		for(int i = 0; i<lrcLongs.length-1 ;i++){
			if(now >= lrcLongs[i] && now < lrcLongs[i+1]){
				return i;
			}
		}
		return lrcLongs.length - 1;
	}
	
	/**
	 * 
	 * @param now 当前歌曲播放的精确时间
	 * @return 当前这一行的歌词已经播放了多长时间
	 */
	public int getOffset(long now){
		
		int index = getIndex(now);

		if(index < lrcLongs.length && index >= 0){
			return (int)(now - lrcLongs[index]);
		}
		
		return 0;
	}
	
	/**
	 * 
	 * @param now 当前歌曲播放的精确时间
	 * @return    当前行的歌词要播放的时间
	 */
	public int getNextTime(long now){
		int index = getIndex(now);
		if(index < lrcLongs.length -1){
			return (int)(lrcLongs[index+1] - lrcLongs[index]);
		}
		return 0;
	}
	
}
