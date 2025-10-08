package db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBMessages;
import model.Customer;
import model.Product;

public class ProductDB implements ProductDBIF {
    

    public Product findProductByNumber(int productNumber) throws DataAccessException {
    	Product product = null;
		String template = "SELECT * FROM Product WHERE productNumber = ?;";
		try (PreparedStatement sql = DBConnection.getInstance().getConnection().prepareStatement(template)) {
			sql.setInt(1, productNumber);
			ResultSet rs = sql.executeQuery();
			if (rs.next()) {
				product = buildObject(rs);
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(0x1021, e);
		}
		return product;
    }
    
    private Product buildObject(ResultSet rs) throws DataAccessException {
    	Product product;
		try {
			product = new Product(rs.getInt("productNumber"), rs.getString("name"), rs.getInt("minStock"));
		}
		catch (SQLException e) {
			throw new DataAccessException(0x10F1, e);
		}
		return product;
	}

	private List<Product> buildObjects(ResultSet rs) throws DataAccessException {
		List<Product> products = new ArrayList<>();
		try {
			while (rs.next()) {
				Product product = buildObject(rs);
				products.add(product);
			}
		}
		catch (SQLException e) {
			throw new DataAccessException(0x10F2, e);
		}
		return products;
	}
}
