package db;

public interface DiscountDBIF {
    void upsertForOrder(int saleOrderId, String type, double amount) throws DataAccessException;
}
