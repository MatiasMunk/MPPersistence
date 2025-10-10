package db;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Customer;

public class CustomerDB implements CustomerDBIF {
    
//	(String name, String address, String zipcode, String city, String phoneNumber, String type) {
	
	public List<Customer> findAllCustomers() throws DataAccessException {
		List<Customer> customers = new ArrayList<>();
	    String sql = "SELECT phoneNo, name, address, zipCity_FK, type FROM Customer";
	    try (Statement stmt = DBConnection.getInstance().getConnection().createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        while (rs.next()) {
	            Customer c = new Customer(
	                rs.getString("name"),
	                rs.getString("address"),
	                rs.getString("zipCity_FK"),
	                rs.getString("phoneNo"),
	                rs.getString("type")
	            );
	            customers.add(c);
	        }
	    } catch (SQLException e) {
	        throw new DataAccessException(0x1010, e);
	    }
	    return customers;
	}
	
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


    @Override
    public void upsert(model.Customer c) throws DataAccessException {
        String sql = """
            MERGE Customer AS tgt
            USING (SELECT ? AS phoneNo) AS src
            ON (tgt.phoneNo = src.phoneNo)
            WHEN MATCHED THEN UPDATE SET
                name = ?, address = ?, zipCity_FK = ?, type = ?
            WHEN NOT MATCHED THEN
                INSERT (phoneNo, name, address, zipCity_FK, type)
                VALUES (?, ?, ?, ?, ?);
        """;
        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getPhoneNo());
            ps.setString(2, c.getName());
            ps.setString(3, c.getAddress());
            ps.setString(4, c.getCity());
            ps.setString(5, c.getType());
            ps.setString(6, c.getPhoneNo());
            ps.setString(7, c.getName());
            ps.setString(8, c.getAddress());
            ps.setString(9, c.getCity());
            ps.setString(10, c.getType());
            if (ps.executeUpdate() == 0) throw new SQLException("Customer upsert failed");
        } catch (SQLException e) {
            throw new DataAccessException(0x2050, e);
        }
    }
}
