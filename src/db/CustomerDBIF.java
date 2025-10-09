package db;

import java.util.List;

import model.Customer;

public interface CustomerDBIF {
	public List<Customer> findAllCustomers() throws DataAccessException;
	public Customer findCustomerByPhone(int phone) throws DataAccessException;
}
