package db;

import model.Product;

public interface ProductDBIF {
	public Product findProductByNumber(int productNumber) throws DataAccessException;
	public void reserveProduct(int productNumber, int quantity) throws DataAccessException;
}
