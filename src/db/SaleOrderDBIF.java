package db;

import java.time.LocalDate;

public interface SaleOrderDBIF {
	public void createOrder(String customerPhoneNo, LocalDate date, double amount, String deliveryStatus, LocalDate deliveryDate) throws DataAccessException;
	public void addCustomerToOrder(String customerPhoneNo) throws DataAccessException;
}
