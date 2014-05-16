package com.tiankong.mp3player21.info;

import java.io.Serializable;
import java.util.HashMap;

public class MusicInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String name = "";
	private String path = "";
	private String artist = "";
	
	public MusicInfo(String name, String path, String artist) {
		super();
		this.name = name;
		this.path = path;
		this.artist = artist;
	}
	public MusicInfo(MusicInfo musicInfo) {
		super();
		this.name = musicInfo.getName();
		this.path = musicInfo.getPath();
		this.artist = musicInfo.getArtist();
	}
	public void setMusicInfo(HashMap<String, Object> map) {
		this.name = (String) map.get("name");
		this.path = (String) map.get("path");
		this.artist = (String) map.get("artist");
	}
	
	public MusicInfo(){
		super();
		this.name = "";
		this.path = "";
		this.artist = "";
	}
	


	@Override
	public String toString() {
		return "MusicInfo [name=" + name + ", path=" + path + ", artist="
				+ artist + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	
	
	
}
