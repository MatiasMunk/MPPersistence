package model;

public class Equipment {
	private String material;
	private String style;
	
	
	public Equipment(int productNumber, String name, int minStock, String material, String style) {
		this.material = material;
		this.style = style;
		
		
	public String getMaterial() {
		return material;
		
	public void setMaterial(String material) {
		this.material = material;
		
		
	public String getStyle() {
		return style;
	}
	
	public void setStyle(String style) {
		this.style = style;
	}
	
	public static Equipment equipment(int productNumber, String name, int minStock, String material, String style) {
		return new Equipment(productNumber, name, minStock, material, style);
	}
	
		
		
	
	

}
