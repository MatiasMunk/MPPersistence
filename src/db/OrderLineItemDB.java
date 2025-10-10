package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderLineItemDB implements OrderLineItemDBIF {

    @Override
    public void addOrIncrement(int saleOrderId, int productNumber, int quantity) throws DataAccessException {
        String sql = """
            MERGE OrderLineItem AS tgt
            USING (SELECT ? AS saleOrderID_FK, ? AS productNumber_FK, ? AS quantity) AS src
            ON (tgt.saleOrderID_FK = src.saleOrderID_FK AND tgt.productNumber_FK = src.productNumber_FK)
            WHEN MATCHED THEN
                UPDATE SET quantity = tgt.quantity + src.quantity
            WHEN NOT MATCHED THEN
                INSERT (saleOrderID_FK, productNumber_FK, quantity)
                VALUES (src.saleOrderID_FK, src.productNumber_FK, src.quantity);
        """;
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleOrderId);
            ps.setInt(2, productNumber);
            ps.setInt(3, quantity);
            if (ps.executeUpdate() == 0) throw new SQLException("OrderLineItem upsert failed");
        } catch (SQLException e) {
            throw new DataAccessException(0x3010, e);
        }
    }
}
