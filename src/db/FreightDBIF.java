package db;

public interface FreightDBIF {
    void upsertForOrder(int saleOrderId, String method, Double baseCost, Double freeThreshold) throws DataAccessException;
}
