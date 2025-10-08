package tui;

import java.util.List;

import db.DataAccessException;
import db.DBConnection;
import dk.raptus.KeyboardReader; // Provided by KeyboardReader.jar (in /lib)

import ctrl.SaleOrderController;

import model.SaleOrder;

public class Scenario {
	private KeyboardReader kr;
	private SaleOrderController saleOrderController;

	public Scenario() {
		kr = KeyboardReader.getInstance();
		saleOrderController = new SaleOrderController();
	}
	
	private void placeOrder() {
	    try {
	        System.out.println("\n=== PLACE NEW ORDER ===");
	        
	     // Start transaction
	        saleOrderController.startTransaction();
	        
	        // Step 1: Create the order
	        System.out.println("Creating sale order...");
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

	
	private int showMenu() {
		System.out.println("\nSALES HANDLING");
		System.out.println(" 1) Place order");
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
