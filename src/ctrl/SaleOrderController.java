package ctrl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DBConnection;


import db.SaleOrderDBIF;
import db.SaleOrderDB;
import db.DataAccessException;

import model.Product;
import model.SaleOrder;

public class SaleOrderController {
    private SaleOrder currentOrder;
    
    private final ProductController productController;
    private final CustomerController customerController;
    
    private final SaleOrderDBIF saleOrderDB;

    private final List<Product> productsInOrder;

    public SaleOrderController() {
        productController = new ProductController();
        customerController = new CustomerController();
        
        saleOrderDB = new SaleOrderDB();
        productsInOrder = new ArrayList<>();
    }
    
    public void startTransaction() throws DataAccessException {
        try {
            DBConnection.getInstance().startTransaction();
            System.out.println("Transaction started in SaleOrderController.");
        } catch (SQLException e) {
            throw new DataAccessException(0x1001, e);
        }
    }

    public void endTransaction() throws DataAccessException {
        try {
            DBConnection.getInstance().commitTransaction();
            System.out.println("Transaction committed in SaleOrderController.");
        } catch (SQLException e) {
            try {
                DBConnection.getInstance().rollbackTransaction();
                System.out.println("Transaction rolled back due to error in SaleOrderController.");
            } catch (SQLException ex) {
                throw new DataAccessException(0x1002, ex);
            }
            throw new DataAccessException(0x1002, e);
        }
    }

    public void rollbackTransaction() throws DataAccessException {
        try {
            DBConnection.getInstance().rollbackTransaction();
            System.out.println("Transaction rolled back manually in SaleOrderController.");
        } catch (SQLException e) {
            throw new DataAccessException(0x1003, e);
        }
    }

	
    public SaleOrder placeOrder() throws DataAccessException {
    	LocalDate orderDate = LocalDate.now();
        LocalDate deliveryDate = orderDate.plusDays(3);
        double amount = 0.0;
        String deliveryStatus = "Pending";

        currentOrder = new SaleOrder(new Date(), 0.0, "Pending", new Date());

        // customerPhoneNo is added later
        saleOrderDB.createOrder(null, orderDate, amount, deliveryStatus, deliveryDate);
        
        return currentOrder;
    }

    public void addProduct(int productNumber, int quantity) throws DataAccessException {
        try {
            Product p = productController.findProductByNumber(productNumber);
            if (p == null) {
                throw new SQLException();
            }

            productController.reserveProduct(productNumber, quantity);
            for (int i = 0; i < quantity; i++) {
                productsInOrder.add(p);
            }

            System.out.println(quantity + " × " + p.getName() + " added to order.");

        } catch (SQLException e) {
            throw new DataAccessException(0x1008, e);
        }
    }

    public void addCustomer(int phoneNumber) throws DataAccessException {
        try {
            // Store in current order
            if (currentOrder == null) {
                throw new IllegalStateException("No current order exists.");
            }

            String phoneStr = String.valueOf(phoneNumber);

            // Update in DB
            saleOrderDB.addCustomerToOrder(phoneStr);

            System.out.println("Customer with phone " + phoneNumber + " added to current order.");

        } catch (Exception e) {
            throw new DataAccessException(0x1010, e);
        }
    }


    // 4.1 createFreight(method)
    public void freightDecision(Boolean isFreight) throws DataAccessException {
    }

    // Optional: apply discount
    public void addDiscount(String type) throws DataAccessException {
    }

    // 5.1 confirmation()
    public void confirmation() throws DataAccessException {
    	//...
    }
}
