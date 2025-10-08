package model;

import java.util.Date;

public class SalePrice {
	private Date timestamp;
	private double price;
	
	public SalePrice(Date timestamp, double price) {
		this.timestamp = timestamp;
		this.price = price;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public Date setTimestamp(Date timestamp) {
		return this.timestamp = timestamp;
	}
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	public static SalePrice salePrice( Date timestamp, double price) {
		return new SalePrice(timestamp, price);
	}

}
