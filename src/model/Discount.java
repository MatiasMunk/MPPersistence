package model;

public class Discount {
    private double amount;
    private double discountThreshold;
    
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
}
