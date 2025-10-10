package tui;

import java.util.List;

import db.DBConnection;
import db.DataAccessException;
import dk.raptus.KeyboardReader;
import ctrl.CustomerController;
import ctrl.ProductController;
import ctrl.SaleOrderController;
import model.Customer;
import model.Product;

public class Scenario {
    private final KeyboardReader kr;
    private final SaleOrderController saleOrderController = new SaleOrderController();
    private final CustomerController customerController = new CustomerController();
    private final ProductController productController = new ProductController();

    public Scenario() {
        kr = KeyboardReader.getInstance();
    }

    public void run() {
        boolean running = true;
        while (running) {
            int choice = showMenu();
            switch (choice) {
                case 1:
                    placeNewOrder();
                    break;
                case 2:
                    findAllCustomers();
                    break;
                case 3:
                    findAllProducts();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Unknown choice");
            }
        }
        DBConnection.getInstance().close();
        kr.close();
        System.out.println("Program terminated.");
        System.exit(0);
    }

    private int showMenu() {
        System.out.println("SALES HANDLING");
        System.out.println(" 1) Place order");
        System.out.println(" 2) Find all customers (not used in scenario)");
        System.out.println(" 3) Find all products (not used in scenario)");
        System.out.println(" 0) Quit program!");
        return kr.readInt("Make a choice: ", "You must type in an integer: ");
    }

    private void placeNewOrder() {
        System.out.println("=== PLACE NEW ORDER ===");

        int phone = kr.readInt("Enter customer phone: ", "You must type in an integer: ");
        Customer cust = null;
        try {
            cust = customerController.findCustomerByPhone(phone);
        } catch (DataAccessException e) {
            System.out.println("Error fetching customer: " + e.getMessage());
        }

        if (cust == null) {
            System.out.println("No customer found with phone: " + phone);
            return;
        }

        try {
            saleOrderController.placeOrder();               // create new order
            saleOrderController.addCustomer(phone);         // link customer

            while (true) {
                int pNo = kr.readInt("Enter product number (0 to finish): ",
                                     "You must type in an integer: ");
                if (pNo == 0) break;
                int qty = kr.readInt("Quantity: ", "You must type in an integer: ");

                saleOrderController.addProduct(pNo, qty);   // reserves immediately
            }

            String confirm = kr.readString("Confirm order? (y/n): ");
            saleOrderController.confirmation(confirm);

        } catch (DataAccessException e) {
            System.out.println("Error placing order: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void findAllCustomers() {
        System.out.println("=== ALL CUSTOMERS ===");
        try {
            List<Customer> customers = customerController.findAllCustomers();
            customers.forEach(System.out::println);
        } catch (DataAccessException e) {
            System.out.println("Error fetching customers: " + e.getMessage());
        }
    }

    private void findAllProducts() {
        System.out.println("=== ALL PRODUCTS ===");
        try {
            List<Product> products = productController.findAllProducts();
            products.forEach(System.out::println);
        } catch (DataAccessException e) {
            System.out.println("Error fetching products: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Scenario().run();
    }
}
