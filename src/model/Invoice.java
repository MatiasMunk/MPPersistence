package model;

public class Invoice {
	private int invoiceNo;
	private double amount;
	private double vat;
	private double totalAmount;
	
	
	
	public int getInvoiceNo() {
		return invoiceNo;
	}
	
	public void setInvoiceNo(int invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
		}
	 
	public double getVat() {
		return vat;
	}
	
	public void setVat(double vat) {
		this.vat = vat;
		
	}
	
	public double getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	

}
