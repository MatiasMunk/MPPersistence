package model;

public class Freight {
	private double baseCost;
	private double freeThreshold;
	
	public Freight(String method, double baseCost, double freeThreshold) {
		this.baseCost = baseCost;
		this.freeThreshold = freeThreshold;
	}
	public double getBaseCost() {
		return baseCost;
	}
	
	public void setBaseCost(double baseCost ) {
		this.baseCost = baseCost;
	}
	
	public double getFreeThreshold() {
		return freeThreshold;
	}
	
	public void setFreeThreshold(double freeThreshold) {
		this.freeThreshold = freeThreshold;
	}
	
	public static Freight freight(String method, double baseCost, double freeThreshold) {
		return new Freight(method, baseCost, freeThreshold);
	}
}
