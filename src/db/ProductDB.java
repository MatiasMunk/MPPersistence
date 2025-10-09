package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBMessages;
import model.Customer;
import model.Product;

public class ProductDB implements ProductDBIF {
    
	public List<Product> findAllProducts() throws DataAccessException {
		List<Product> products = new ArrayList<>();
	    String sql = "SELECT productNumber, name, minStock FROM Product";
	    try (Statement stmt = DBConnection.getInstance().getConnection().createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        while (rs.next()) {
	            Product p = new Product(
	                rs.getInt("productNumber"),
	                rs.getString("name"),
	                rs.getInt("minStock")
	            );
	            products.add(p);
	        }
	    } catch (SQLException e) {
	        throw new DataAccessException(0x1011, e);
	    }
	    return products;
	}
	
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
			throw new DataAccessException(0x1005, e);
		}
		return product;
    }
    
    public void reserveProduct(int productNumber, int quantity) throws DataAccessException {
        String checkStockSQL = "SELECT availableQty, reservedQty FROM Stock WHERE productNumber_FK = ?;";
        String updateStockSQL = "UPDATE Stock SET availableQty = availableQty - ?, reservedQty = reservedQty + ? WHERE productNumber_FK = ?;";
        
        Connection conn = DBConnection.getInstance().getConnection();
        try (
            PreparedStatement checkStmt = conn.prepareStatement(checkStockSQL);
            PreparedStatement updateStmt = conn.prepareStatement(updateStockSQL)
        ) {
            checkStmt.setInt(1, productNumber);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                throw new SQLException();
            }

            int available = rs.getInt("availableQty");
            if (available < quantity) {
                throw new SQLException();
            }

            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, quantity);
            updateStmt.setInt(3, productNumber);
            updateStmt.executeUpdate();

            System.out.println(quantity + " units of product " + productNumber + " reserved.");
        }
        catch (SQLException e) {
            throw new DataAccessException(0x1006, e);
        }
    }

    
    private Product buildObject(ResultSet rs) throws DataAccessException {
    	Product product;
		try {
			product = new Product(rs.getInt("productNumber"), rs.getString("name"), rs.getInt("minStock"));
		}
		catch (SQLException e) {
			throw new DataAccessException(0x1009, e);
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
			throw new DataAccessException(0x1009, e);
		}
		return products;
	}
}
