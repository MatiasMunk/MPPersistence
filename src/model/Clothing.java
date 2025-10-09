package model;

public class Clothing {
	private String size;
	private String colour;
	
	
	public Clothing(int productNumber, String name, int minStock, String format, String artist, String colour, String size) {
		this.size = size;
		this.colour = colour;
	}
	
	public String getSize() {
		return size;
	}
	
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getColour() {
		return colour;
	}
	
	public void setColour(String colour) {
		this.colour = colour;
	}
	
    public static Clothing clothing(int productNumber, String name, int minStock, String format, String artist, String colour, String size) {
		return new Clothing(productNumber, name, minStock, format, artist, colour, size);
	}

}
