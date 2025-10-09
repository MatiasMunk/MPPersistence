package ctrl;

import java.util.List;

import db.CustomerDB;
import db.CustomerDBIF;
import db.DataAccessException;
import model.Customer;

public class CustomerController {

    CustomerDBIF customerDB;

    public CustomerController() {
        if (this.customerDB == null)
            this.customerDB = new CustomerDB();
    }

    public List<Customer> findAllCustomers() throws DataAccessException {
        return customerDB.findAllCustomers();
    }

    public Customer findCustomerByPhone(int phoneNumber) throws DataAccessException {
        String phone = String.valueOf(phoneNumber);
        for (Customer c : findAllCustomers()) {
            //Assumes Customer has getPhoneNo() returning String
            if (phone.equals(c.getPhoneNo())) {
                return c;
            }
        }
        return null;
    }
}
