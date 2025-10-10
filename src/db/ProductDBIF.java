package db;

import java.util.List;

import model.Product;

public interface ProductDBIF {
	public List<Product> findAllProducts() throws DataAccessException;
	public Product findProductByNumber(int productNumber) throws DataAccessException;
	public void reserveProduct(int productNumber, int quantity) throws DataAccessException;
	public void unreserveProduct(int productNumber, int quantity) throws DataAccessException;
	public void resetOrder(int saleOrderId, int productNumber, int quantity) throws DataAccessException;
	public void consumeReservation(int productNumber, int quantity) throws DataAccessException;
}
