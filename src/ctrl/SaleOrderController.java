package ctrl;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.*;
import dk.raptus.KeyboardReader;

import model.Product;
import model.SaleOrder;
import model.Customer;

public class SaleOrderController {

    private SaleOrder currentOrder;
    private Customer currentCustomer;           

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

        System.out.println("Creating sale order...");
        currentOrder = new SaleOrder(new Date(), 0.0, "Pending", new Date());

        int generatedID = saleOrderDB.createOrder(null, orderDate, amount, deliveryStatus, deliveryDate);
        currentOrder.setId(generatedID);

        // reset current customer for the new order
        currentCustomer = null;

        return currentOrder;
    }

    /**
     * Add product now:
     * 1) reserve stock (availableQty down, reservedQty up)
     * 2) insert OrderLineItem
     * 3) commit immediately so changes are visible right away
     * 4) start a fresh transaction to keep the flow consistent
     */
    public void addProduct(int productNumber, int quantity) throws DataAccessException {
        try {
            Product p = productController.findProductByNumber(productNumber);
            if (p == null) {
                throw new SQLException("No product found for number " + productNumber);
            }

            // reserve instantly
            productController.reserveProduct(productNumber, quantity);

            // write line item
            saleOrderDB.addOrderLineItem(currentOrder.getId(), productNumber, quantity);

            // cache for possible cancel
            for (int i = 0; i < quantity; i++) {
                productsInOrder.add(p);
            }

            // commit now if we are inside a transaction, then restart it
            try {
                if (!DBConnection.getInstance().getConnection().getAutoCommit()) {
                    DBConnection.getInstance().commitTransaction();
                    DBConnection.getInstance().startTransaction();
                }
            } catch (SQLException ignore) {
                // if autocommit is true this is a no-op
            }

            System.out.println(quantity + " × " + p.getName() + " added to order (reserved).");
        } catch (SQLException e) {
            throw new DataAccessException(0x1008, e);
        }
    }

    public void addCustomer(int phoneNumber) throws DataAccessException {
        try {
            if (currentOrder == null) {
                throw new IllegalStateException("No current order exists.");
            }

            // find and cache the current customer
            Customer found = customerController.findCustomerByPhone(phoneNumber);
            if (found == null) {
                throw new IllegalStateException("No customer found with phone: " + phoneNumber);
            }
            currentCustomer = found;

            // Update DB with the phone (existing API)
            String phoneStr = String.valueOf(phoneNumber);
            saleOrderDB.addCustomerToOrder(phoneStr);

            System.out.println("Customer with phone " + phoneNumber + " added to current order.");

        } catch (Exception e) {
            throw new DataAccessException(0x1010, e);
        }
    }

    // Expose the current customer for the current order
    public Customer findCurrentCustomer() {
        return currentCustomer; // may be null if not set yet
    }

    public void freightDecision(Boolean isFreight) throws DataAccessException {
    }

    public void addDiscount(String type) throws DataAccessException {
    }


    public void confirmation(String confirm) throws DataAccessException {


        Map<Integer, Integer> productQuantities = new HashMap<>();
        for (Product p : productsInOrder) {
            productQuantities.put(
                p.getProductNumber(),
                productQuantities.getOrDefault(p.getProductNumber(), 0) + 1
            );
        }

        if (confirm.equalsIgnoreCase("y")) {
            // consume reservations
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                productController.consumeReservation(entry.getKey(), entry.getValue());
            }

            // calculate totals and create invoice
            double amountBeforeDiscount = 0.0;
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                double price = productController.getCurrentPrice(entry.getKey());
                amountBeforeDiscount += price * entry.getValue();
            }

            double discountValue = 0.0;
            if ("klub".equalsIgnoreCase(currentCustomer.getType()) && amountBeforeDiscount > 1500.0) {
                discountValue = 100.0;
                new DiscountDB().upsertForOrder(currentOrder.getId(), "club", discountValue);
            } else {
                new DiscountDB().upsertForOrder(currentOrder.getId(), "none", 0.0);
            }

            double freightValue = 0.0;
            if ("privat".equalsIgnoreCase(currentCustomer.getType())) {
                freightValue = amountBeforeDiscount > 2500 ? 0.0 : 45.0;
                new FreightDB().upsertForOrder(currentOrder.getId(), "standard", 45.0, 2500.0);
            } else {
                new FreightDB().upsertForOrder(currentOrder.getId(), "none", 0.0, null);
            }

            double net = Math.max(0, amountBeforeDiscount - discountValue) + freightValue;
            double vat = Math.round(net * 0.25 * 100.0) / 100.0;
            double total = net + vat;

            new InvoiceDB().createInvoice(currentOrder.getId(), java.time.LocalDate.now(), net, vat, total);

            System.out.println("Order confirmed with invoice #" + currentOrder.getId());
        }
        else if (confirm.equalsIgnoreCase("n")) {
            // Cancel: release reservations and delete line items
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                productController.resetOrder(currentOrder.getId(), entry.getKey(), entry.getValue());
            }
            System.out.println("Order cancelled and reservations released.");
        } else {
            System.out.println("Unknown choice. Leaving order unchanged.");
        }
    }
}
