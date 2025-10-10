package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DiscountDB implements DiscountDBIF {

    @Override
    public void upsertForOrder(int saleOrderId, String type, double amount) throws DataAccessException {
        String sql = """
            MERGE Discount AS tgt
            USING (SELECT ? AS saleOrderID_FK, ? AS [type]) AS src
            ON (tgt.saleOrderID_FK = src.saleOrderID_FK AND tgt.[type] = src.[type])
            WHEN MATCHED THEN
                UPDATE SET amount = ?
            WHEN NOT MATCHED THEN
                INSERT (saleOrderID_FK, [type], amount) VALUES (?, ?, ?);
        """;
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleOrderId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setInt(4, saleOrderId);
            ps.setString(5, type);
            ps.setDouble(6, amount);
            if (ps.executeUpdate() == 0) throw new SQLException("Discount upsert failed");
        } catch (SQLException e) {
            throw new DataAccessException(0x2020, e);
        }
    }
}
