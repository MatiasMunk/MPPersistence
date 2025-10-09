package tui;

import java.util.List;

import db.DataAccessException;
import db.DBConnection;
import dk.raptus.KeyboardReader; // Provided by KeyboardReader.jar (in /lib)
import ctrl.CustomerController;
import ctrl.ProductController;
import ctrl.SaleOrderController;
import model.Customer;
import model.Product;
import model.SaleOrder;

public class Scenario {
	private KeyboardReader kr;
	private SaleOrderController saleOrderController;
	private CustomerController customerController;
	private ProductController productController;
	
	public Scenario() {
		kr = KeyboardReader.getInstance();
		saleOrderController = new SaleOrderController();
		customerController = new CustomerController();
		productController = new ProductController();
	}
	
	private void placeOrder() {
	    try {
	        System.out.println("\n=== PLACE NEW ORDER ===");
	        
	        // Start transaction
	        saleOrderController.startTransaction();
	        
	        // Step 1: Create the order
	        SaleOrder order = saleOrderController.placeOrder(); // 1.1 in diagram
	        
	        boolean addingProducts = true;
	        while (addingProducts) {
	            int productNumber = kr.readInt("Enter product number: ", "You must type in an integer: ");
	            int quantity = kr.readInt("Enter quantity: ", "You must type in an integer: ");
	            saleOrderController.addProduct(productNumber, quantity); // 2.1
	            
	            String more = kr.readString("Add more products? (y/n): ");
	            addingProducts = more.equalsIgnoreCase("y");
	        }

	        // Step 2: Add customer
	        int phoneNumber = kr.readInt("Enter customer phone number: ", "You must type in an integer: ");
	        saleOrderController.addCustomer(phoneNumber); // 3.1

	        // Step 3: Freight
	        int method = kr.readInt("Enter freight method (0 = no freight/1 = freight): ", "You must type in an integer: ");
	        saleOrderController.freightDecision(method == 0 ? false : true); // 4.1

	        // Step 4: Confirmation
	        saleOrderController.confirmation(); // 5.1
	        
	        // Commit transaction
	        saleOrderController.endTransaction();
	        System.out.println("Order successfully placed!");

	    } catch (DataAccessException e) {
	        System.out.println("Database error: " + e.getMessage());
	        try {
	            saleOrderController.rollbackTransaction();
	        } catch (DataAccessException rollbackEx) {
	            System.out.println("Rollback failed: " + rollbackEx.getMessage());
	        }
	    } catch (Exception e) {
	        System.out.println("Error placing order: " + e.getMessage());
	        try {
	            saleOrderController.rollbackTransaction();
	        } catch (DataAccessException rollbackEx) {
	            System.out.println("Rollback failed: " + rollbackEx.getMessage());
	        }
	    }
	}

	private void findAllCustomers() {
	    try {
	        // Get all customers from the controller
	        List<Customer> customers = customerController.findAllCustomers();

	        System.out.println("\n=== ALL CUSTOMERS ===");
	        for (Customer c : customers) {
	        	System.out.println(c);
	        }
	    } catch (DataAccessException e) {
	        System.out.println("Error fetching customers: " + e.getMessage());
	    }
	}

	private void findAllProducts() {
	    try {
	        // Get all products from the controller
	        List<Product> products = productController.findAllProducts();

	        System.out.println("\n=== ALL PRODUCTS ===");
	        for (Product p : products) {
	        	System.out.println(p);
	        }
	    } catch (DataAccessException e) {
	        System.out.println("Error fetching products: " + e.getMessage());
	    }
	}

	
	private int showMenu() {
		System.out.println("\nSALES HANDLING");
		System.out.println(" 1) Place order");
		System.out.println(" 2) Find all customers");
		System.out.println(" 3) Find all products");
		System.out.println(" 0) Quit program!");
		return kr.readInt("Make a choice: ", "You must type in an integer: ");
	}

	private void run() {
		int choice = -1;
		while (choice != 0) {
			choice = showMenu();
			switch (choice) {
				case 0:
					break;
				case 1:
					placeOrder();
					break;
				case 2:
					findAllCustomers();
					break;
				case 3:
					findAllProducts();
					break;
				default:
					System.out.println("Wrong choice - let's try again!");
			}
		}
		DBConnection.getInstance().close();
		kr.close();
	}

	public static void main(String args[]) {
		new Scenario().run();
	}
}
