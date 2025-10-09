package ctrl;

import java.util.List;

import db.CustomerDB;
import db.CustomerDBIF;
import db.DataAccessException;
import model.Customer;

public class CustomerController {
	
	CustomerDBIF customerDB;
	
	public CustomerController() {
		if(this.customerDB == null)
			this.customerDB = new CustomerDB();
	}
	
	public List<Customer> findAllCustomers() throws DataAccessException {
	    return customerDB.findAllCustomers(); // returns List<Customer>
	}

}
