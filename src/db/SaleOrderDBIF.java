package db;

import java.time.LocalDate;

public interface SaleOrderDBIF {
	public int createOrder(String customerPhoneNo, LocalDate date, double amount, String deliveryStatus, LocalDate deliveryDate) throws DataAccessException;
	public void addCustomerToOrder(String customerPhoneNo) throws DataAccessException;
	public void addOrderLineItem(int saleOrderID, int productNumber, int quantity) throws DataAccessException;
}
