package model;

public class Discount {
    private double amount;
    private double discountThreshold;
    
    public Discount(String type, double amount, double discountThreshold) {
		this.amount = amount;
		this.discountThreshold = discountThreshold;
	}
    
    public double getAmount() {
		return amount;
	}
    
    
    public void setAmount(double amount) {
    	this.amount = amount;
	}
    
    public double getDiscountThreshold() {
    	return discountThreshold;
    }
    
    public void setDiscountThreshold(double discountThreshold) {
    	this.discountThreshold = discountThreshold; 
    }
    
    public static Discount discount(String type, double amount, double discountThreshold) {
		return new Discount(type, amount, discountThreshold);
	}
}
