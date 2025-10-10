package ctrl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
	
	public List<Product> findAllProducts() throws DataAccessException {
	    return productDB.findAllProducts(); // returns List<Product>
	}
	
	public Product findProductByNumber(int productNumber) throws DataAccessException {
    	return productDB.findProductByNumber(productNumber);
    }
    
    public void reserveProduct(int productNumber, int quantity) throws DataAccessException {
        productDB.reserveProduct(productNumber, quantity);
    }
    
    public void unreserveProduct(int productNumber, int quantity) throws DataAccessException {
        productDB.unreserveProduct(productNumber, quantity);
    }
    
    public void consumeReservation(int productNo, int qty) throws DataAccessException {
        productDB.consumeReservation(productNo, qty);
    }

    public void resetOrder(int saleOrderId, int productNumber, int quantity) throws DataAccessException {
        productDB.resetOrder(saleOrderId, productNumber, quantity);
    }

    
    public Double getCurrentPrice(int productNumber) throws DataAccessException {
        String sql = """
            SELECT TOP 1 price
            FROM SalePrice
            WHERE productNumber_FK = ?
            ORDER BY [timestamp] DESC
        """;
        try (PreparedStatement ps = DBConnection.getInstance().getConnection().prepareStatement(sql)) {
            ps.setInt(1, productNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("price");
            }
            return 0.0;
        } catch (SQLException e) {
            throw new DataAccessException(0x2030, e);
        }
    }
}
