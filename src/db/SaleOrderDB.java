package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class SaleOrderDB implements SaleOrderDBIF {

    private Connection conn;

    public SaleOrderDB() {
    	this.conn = DBConnection.getInstance().getConnection();
    }
    
    public void createOrder(String customerPhoneNo, LocalDate date, double amount, String deliveryStatus, LocalDate deliveryDate) throws DataAccessException {
        String sql = "INSERT INTO SaleOrder (customerPhoneNo_FK, date, amount, deliveryStatus, deliveryDate) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;

        try {
            ps = conn.prepareStatement(sql);
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

            	System.out.println("Executing SQL: " + debugSql);

            	
            ps.executeUpdate();
            System.out.println("Sale order created successfully.");

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

}
