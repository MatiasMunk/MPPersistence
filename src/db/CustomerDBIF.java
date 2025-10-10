package db;

import java.util.List;
import model.Customer;

public interface CustomerDBIF {
    List<Customer> findAllCustomers() throws DataAccessException;
    Customer findCustomerByPhone(int phone) throws DataAccessException;
    void upsert(Customer c) throws DataAccessException;
}
