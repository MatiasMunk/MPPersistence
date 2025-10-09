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
    
    public void unreserveProduct(int productNumber, int quantity) throws DataAccessException {
        String sql = """
            UPDATE Stock 
            SET reservedQty  = reservedQty - ?
            WHERE productNumber_FK = ?;
        """;

        Connection conn = DBConnection.getInstance().getConnection();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productNumber);

            String debugSql = sql
                .replaceFirst("\\?", String.valueOf(quantity))
                .replaceFirst("\\?", String.valueOf(quantity))
                .replaceFirst("\\?", String.valueOf(productNumber));

            //System.out.println("[DEBUG] Unreserving product: " + debugSql);

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new SQLException();
            }

            System.out.println(quantity + " units of product " + productNumber + " unreserved.");
        } catch (SQLException e) {
            throw new DataAccessException(0x1016, e);
        }
    }
    
    public void resetOrder(int saleOrderId, int productNumber, int quantity) throws DataAccessException {
        String resetStockSQL = """
            UPDATE Stock 
            SET reservedQty  = reservedQty - ?,
            availableQty  = availableQty + ?
            WHERE productNumber_FK = ?;
        """;

        Connection conn = DBConnection.getInstance().getConnection();

        String deleteLineItemSQL = """
                DELETE FROM OrderLineItem
                WHERE saleOrderID_FK = ? AND productNumber_FK = ?;
            """;
        
        String deleteSaleOrderSQL = """
                DELETE FROM SaleOrder
                WHERE saleOrderID = ?;
            """;

            try (
                PreparedStatement psReset = conn.prepareStatement(resetStockSQL);
                PreparedStatement psDelete = conn.prepareStatement(deleteLineItemSQL);
            	PreparedStatement psDeleteOrder = conn.prepareStatement(deleteSaleOrderSQL);
            ) {
                // 1️⃣ Reset reservedQty in Stock
            	psReset.setInt(1, quantity);
            	psReset.setInt(2, quantity);
                psReset.setInt(3, productNumber);
                int affectedStock = psReset.executeUpdate();
                if (affectedStock == 0) {
                    throw new SQLException("No stock updated for product " + productNumber);
                }

             // 2️⃣ Delete the order line item
                psDelete.setInt(1, saleOrderId);
                psDelete.setInt(2, productNumber);
                int affectedLine = psDelete.executeUpdate();
                if (affectedLine == 0) {
                    throw new SQLException("No OrderLineItem deleted for order " + saleOrderId + " and product " + productNumber);
                }
                
                // 3 Delete the sale order
                psDeleteOrder.setInt(1, saleOrderId);
                int affectedOrder = psDeleteOrder.executeUpdate();
                if (affectedOrder == 0) {
                    throw new SQLException("No SaleOrder deleted for order " + saleOrderId);
                }

                System.out.println("Reset " + quantity + " units of product " + productNumber + " and deleted OrderLineItem.");

            } catch (SQLException e) {
                throw new DataAccessException(0x1016, e);
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
