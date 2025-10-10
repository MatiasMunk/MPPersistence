package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrderLineItemDB implements OrderLineItemDBIF {

    @Override
    public void addOrIncrement(int saleOrderID, int productNumber, int quantity) throws DataAccessException {
        String sql = "MERGE OrderLineItem AS target " +
                     "USING (SELECT ? AS saleOrderID, ? AS productNo, ? AS qty) AS src " +
                     "ON (target.saleOrderID_FK = src.saleOrderID AND target.productNumber_FK = src.productNo) " +
                     "WHEN MATCHED THEN " +
                     "    UPDATE SET quantity = target.quantity + src.qty " +
                     "WHEN NOT MATCHED THEN " +
                     "    INSERT (saleOrderID_FK, productNumber_FK, quantity) " +
                     "    VALUES (src.saleOrderID, src.productNo, src.qty);";

        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, saleOrderID);
            ps.setInt(2, productNumber);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(0x1012, e);
        }
    }

}
