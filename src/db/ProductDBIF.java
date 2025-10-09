package db;

import java.util.List;

import model.Product;

public interface ProductDBIF {
	public List<Product> findAllProducts() throws DataAccessException;
	public Product findProductByNumber(int productNumber) throws DataAccessException;
	public void reserveProduct(int productNumber, int quantity) throws DataAccessException;
	
}
