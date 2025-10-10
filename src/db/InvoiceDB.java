package db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class InvoiceDB implements InvoiceDBIF {

    @Override
    public void createInvoice(int saleOrderId, LocalDate paymentDate, double amount, double vat, double totalAmount)
            throws DataAccessException {
        String sql = """
            INSERT INTO Invoice (saleOrderID_FK, paymentDate, amount, vat, totalAmount)
            VALUES (?, ?, ?, ?, ?);
        """;
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleOrderId);
            ps.setDate(2, paymentDate != null ? Date.valueOf(paymentDate) : null);
            ps.setDouble(3, amount);
            ps.setDouble(4, vat);
            ps.setDouble(5, totalAmount);
            if (ps.executeUpdate() == 0) throw new SQLException("Invoice insert failed");
        } catch (SQLException e) {
            throw new DataAccessException(0x2010, e);
        }
    }
}
