package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FreightDB implements FreightDBIF {

    @Override
    public void upsertForOrder(int saleOrderId, String method, Double baseCost, Double freeThreshold)
            throws DataAccessException {
        String sql = """
            MERGE Freight AS tgt
            USING (SELECT ? AS saleOrderID_FK) AS src
            ON (tgt.saleOrderID_FK = src.saleOrderID_FK)
            WHEN MATCHED THEN UPDATE SET [method] = ?, baseCost = ?, freeThreshold = ?
            WHEN NOT MATCHED THEN INSERT (saleOrderID_FK, [method], baseCost, freeThreshold)
                                VALUES (?, ?, ?, ?);
        """;
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, saleOrderId);
            ps.setString(2, method);
            if (baseCost == null) ps.setNull(3, java.sql.Types.DECIMAL); else ps.setDouble(3, baseCost);
            if (freeThreshold == null) ps.setNull(4, java.sql.Types.DECIMAL); else ps.setDouble(4, freeThreshold);

            ps.setInt(5, saleOrderId);
            ps.setString(6, method);
            if (baseCost == null) ps.setNull(7, java.sql.Types.DECIMAL); else ps.setDouble(7, baseCost);
            if (freeThreshold == null) ps.setNull(8, java.sql.Types.DECIMAL); else ps.setDouble(8, freeThreshold);

            if (ps.executeUpdate() == 0) throw new SQLException("Freight upsert failed");
        } catch (SQLException e) {
            throw new DataAccessException(0x2040, e);
        }
    }
}
