package db;

import java.sql.Connection;
import java.sql.SQLException;

public class SaleOrderDB implements SaleOrderDBIF {

    private Connection conn;

    public SaleOrderDB() {
    	this.conn = DBConnection.getInstance().getConnection();
    }

}
