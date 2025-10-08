package model;

public class GunReplica {
	private String caliber;
	private String material;
    
	public GunReplica(String caliber, String material) {
		this.caliber = caliber;
		this.material = material;
	}
	
	public String getCaliber() {
		return caliber;
	}
	
	public void setCaliber(String caliber) {
		this.caliber = caliber;
	}
	
	public String getMaterial() {
		return material;
	}
	
	public void setMaterial(String material) {
		this.material = material;
		
	}
	
	public static GunReplica gunReplica(String caliber, String material) {
		return new GunReplica(caliber, material);
	}
	
}
