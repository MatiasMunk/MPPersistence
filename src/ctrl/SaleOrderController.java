package ctrl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DBConnection;

import db.SaleOrderDBIF;
import dk.raptus.KeyboardReader;
import db.SaleOrderDB;
import db.DataAccessException;

import model.Product;
import model.SaleOrder;

public class SaleOrderController {
	private KeyboardReader kr;
	
	private SaleOrder currentOrder;
    
    private final ProductController productController;
    private final CustomerController customerController;
    
    private final SaleOrderDBIF saleOrderDB;

    private final List<Product> productsInOrder;

    public SaleOrderController() {
    	kr = KeyboardReader.getInstance();
    	
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

        System.out.println("Creating sale order...");
        currentOrder = new SaleOrder(new Date(), 0.0, "Pending", new Date());

        // customerPhoneNo is added later
        int generatedID = saleOrderDB.createOrder(null, orderDate, amount, deliveryStatus, deliveryDate);
        currentOrder.setId(generatedID);
        
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
            	System.out.println("Product: " + productNumber + " added to order");
                productsInOrder.add(p);
            }
            
            saleOrderDB.addOrderLineItem(currentOrder.getId(), productNumber, quantity);

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


    public void freightDecision(Boolean isFreight) throws DataAccessException {
    }

    public void addDiscount(String type) throws DataAccessException {
    }

    public void confirmation() throws DataAccessException {
        String confirm = kr.readString("Confirm order? (y/n): ");

        System.out.println("Order cancelled — rolling back stock reservations...");
        
        if (confirm.equalsIgnoreCase("y")) {
	        // Unreserve products since order goes through
	        Map<Integer, Integer> productQuantities = new HashMap<>();
	        for (Product p : productsInOrder) {
	        	System.out.println("Product in order: " + p.getName());
	            productQuantities.put(
	                p.getProductNumber(),
	                productQuantities.getOrDefault(p.getProductNumber(), 0) + 1 // Quantity of product in order
	            );
	        }
	
	        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
	        	System.out.println("Unreserving product");
	            productController.unreserveProduct(entry.getKey(), entry.getValue());
	        }
	        
	        System.out.println("Order successfully placed!");
        } else if (confirm.equalsIgnoreCase("n")) {
        	// Reset Order changes in database
	        Map<Integer, Integer> productQuantities = new HashMap<>();
	        for (Product p : productsInOrder) {
	        	System.out.println("Product in order: " + p.getName());
	            productQuantities.put(
	                p.getProductNumber(),
	                productQuantities.getOrDefault(p.getProductNumber(), 0) + 1 // Quantity of product in order
	            );
	        }
	
	        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
	        	System.out.println("Unreserving product");
	            productController.resetOrder(currentOrder.getId(), entry.getKey(), entry.getValue());
	        }
	        
	        System.out.println("Order successfully cancelled!");
        }
    }

}
