package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Customer;

public class CustomerDB {
    
    public Customer findCustomerByPhone(int phone) throws DataAccessException {
        Customer customer = null;
		String template = "SELECT * FROM Customers WHERE phoneNo = ?;";
		try (PreparedStatement sql = DBConnection.getInstance().getConnection().prepareStatement(template)) {
			sql.setInt(1, phone);
			ResultSet rs = sql.executeQuery();
			if (rs.next()) {
				customer = buildObject(rs);
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(0x1021, e);
		}
		return customer;
    }
    
    private Customer buildObject(ResultSet rs) throws DataAccessException {
		Customer customer;
		try {
			customer = new Customer(rs.getString("phoneNo"), rs.getString("name"), rs.getString("address"), rs.getString("zipCity_FK"), rs.getString("type"));
		}
		catch (SQLException e) {
			throw new DataAccessException(0x10F1, e);
		}
		return customer;
	}

	private List<Customer> buildObjects(ResultSet rs) throws DataAccessException {
		List<Customer> customers = new ArrayList<>();
		try {
			while (rs.next()) {
				Customer customer = buildObject(rs);
				customers.add(customer);
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(0x10F2, e);
		}
		return customers;
	}
}
