package model;

public class Stock {
	private int availableQty;
	private int reservedQty;
	
	public Stock(int availableQty, int reservedQty) {
		this.availableQty = availableQty;
		this.reservedQty = reservedQty;
	}
	
	public int getAvailableQty() {
		return availableQty;
	}
	public void setAvailableQty(int availableQty) {
		this.availableQty = availableQty;
	}
	public int getReservedQty() {
		return reservedQty;
	}
	public void setReservedQty(int reservedQty) {
		this.reservedQty = reservedQty;
	}
	
	public static Stock stock(int availableQty, int reservedQty) {
		return new Stock(availableQty, reservedQty);
	}
	

}
