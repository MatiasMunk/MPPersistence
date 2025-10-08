package model;

public class Music {
	private String format;
	private String artist;
	
	public Music(String format, String artist) {
		this.format = format;
		this.artist = artist;
	}
	
	public String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String artist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public static Music music(String format, String artist) {
		return new Music(format, artist);
	}

}
