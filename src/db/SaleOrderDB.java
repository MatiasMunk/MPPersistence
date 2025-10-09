package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SaleOrderDB implements SaleOrderDBIF {


    public SaleOrderDB() {
    }
    
    public int createOrder(String customerPhoneNo, LocalDate date, double amount, String deliveryStatus, LocalDate deliveryDate) throws DataAccessException {
        String sql = "INSERT INTO SaleOrder (customerPhoneNo_FK, date, amount, deliveryStatus, deliveryDate) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;

        Connection conn = DBConnection.getInstance().getConnection();
        try {
            ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, customerPhoneNo);
            ps.setDate(2, Date.valueOf(date));
            ps.setDouble(3, amount);
            ps.setString(4, deliveryStatus);
            ps.setDate(5, Date.valueOf(deliveryDate));

            String debugSql = sql
            	    .replaceFirst("\\?", "'" + customerPhoneNo + "'")
            	    .replaceFirst("\\?", "'" + Date.valueOf(date) + "'")
            	    .replaceFirst("\\?", String.valueOf(amount))
            	    .replaceFirst("\\?", "'" + deliveryStatus + "'")
            	    .replaceFirst("\\?", "'" + Date.valueOf(deliveryDate) + "'");

            	//System.out.println("Executing SQL: " + debugSql);

            System.out.println("Inserting sale order in db...");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedID = rs.getInt(1);
                    System.out.println("Sale order created with ID: " + generatedID);
                    return generatedID;
                } else {
                	System.out.println("No generated key returned");
                    throw new DataAccessException(0x1004, new SQLException());
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException(0x1004, e);
        } finally {
            try {
                if (ps != null) ps.close();
            } catch (SQLException e) {
                throw new DataAccessException(0x1004, e);
            }
        }
    }
    
    public void addCustomerToOrder(String customerPhoneNo) throws DataAccessException {
        String sql = "UPDATE SaleOrder SET customerPhoneNo_FK = ? WHERE saleOrderId = (SELECT MAX(saleOrderId) FROM SaleOrder)";
        
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, customerPhoneNo);
            ps.executeUpdate();
            System.out.println("Customer linked to sale order successfully.");
        } catch (SQLException e) {
            throw new DataAccessException(0x3002, e);
        }
    }
    
    public void addOrderLineItem(int saleOrderID, int productNumber, int quantity) throws DataAccessException {
        String sql = "INSERT INTO OrderLineItem (saleOrderID_FK, productNumber_FK, quantity) VALUES (?, ?, ?)";
        
        String debugSql = sql.replaceFirst("\\?", String.valueOf(saleOrderID))
                             .replaceFirst("\\?", String.valueOf(productNumber))
                             .replaceFirst("\\?", String.valueOf(quantity));
        //System.out.println("Executing SQL: " + debugSql);
        
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleOrderID);
            ps.setInt(2, productNumber);
            ps.setInt(3, quantity);
            ps.executeUpdate();
            System.out.println("OrderLineItem inserted: Order " + saleOrderID + ", Product " + productNumber + ", Qty " + quantity);
        } catch (SQLException e) {
            throw new DataAccessException(0x1012, e);
        }
    }
}
