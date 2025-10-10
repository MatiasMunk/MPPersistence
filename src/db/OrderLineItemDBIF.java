package db;

public interface OrderLineItemDBIF {
    void addOrIncrement(int saleOrderId, int productNumber, int quantity) throws DataAccessException;
}
