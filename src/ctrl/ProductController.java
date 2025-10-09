package ctrl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DBConnection;
import db.DataAccessException;
import db.ProductDB;
import db.ProductDBIF;
import model.Product;

public class ProductController {

	ProductDBIF productDB;
	
	public ProductController() {
		if(this.productDB == null)
			this.productDB = new ProductDB();
	}
	
	public Product findProductByNumber(int productNumber) throws DataAccessException {
    	return productDB.findProductByNumber(productNumber);
    }
    
    public void reserveProduct(int productNumber, int quantity) throws DataAccessException {
        productDB.reserveProduct(productNumber, quantity);
    }
}
