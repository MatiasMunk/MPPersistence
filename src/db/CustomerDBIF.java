package db;

import model.Customer;

public interface CustomerDBIF {
	public Customer findCustomerByPhone(int phone) throws DataAccessException;
}
