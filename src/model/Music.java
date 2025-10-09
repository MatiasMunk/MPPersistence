package model;

public class Music {
	private String format;
	private String artist;
	
	public Music(int productNumber, String name, int minStock, String format, String artist) {
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
	
	public static Music music(int productNumber, String name, int minStock, String format, String artist) {
		return new Music(productNumber, name, minStock, format, artist);
	}	
}
