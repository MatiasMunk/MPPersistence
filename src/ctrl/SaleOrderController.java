package ctrl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.CustomerDB;
import db.DiscountDB;
import db.FreightDB;
import db.ProductDB;
import db.SaleOrderDB;
import db.DataAccessException;

import model.Customer;
import model.Discount;
import model.Freight;
import model.Invoice;
import model.Product;
import model.SaleOrder;

public class SaleOrderController {
    private SaleOrder currentOrder;
    private final ProductDB productDB;
    private final CustomerDB customerDB;
    private final FreightDB freightDB;
    private final DiscountDB discountDB;
    private final SaleOrderDB saleOrderDB;

    private final List<Product> productsInOrder;

    public SaleOrderController() {
        productDB = new ProductDB();
        customerDB = new CustomerDB();
        freightDB = new FreightDB();
        discountDB = new DiscountDB();
        saleOrderDB = new SaleOrderDB();
        productsInOrder = new ArrayList<>();
    }
    
    // 1.1 create SaleOrder
    public SaleOrder placeOrder() throws DataAccessException {
        currentOrder = new SaleOrder(new Date(), 0.0, "Pending", new Date());
        return currentOrder;
    }

    // 2.1 addProduct(productNumber, quantity)
    public void addProduct(int productNumber, int quantity) throws DataAccessException {
        try {
            Product p = productDB.findProductByNumber(productNumber);
            if (p == null) {
                throw new SQLException();
            }

            productDB.reserveProduct(productNumber, quantity);
            for (int i = 0; i < quantity; i++) {
                productsInOrder.add(p);
            }

            System.out.println(quantity + " × " + p.getName() + " added to order.");

        } catch (SQLException e) {
            // Wrap in DataAccessException with product-specific error code
            throw new DataAccessException(0x10F2, e);
        }
    }

    // 3.1 addCustomer(phoneNumber)
    public void addCustomer(int phoneNumber) throws DataAccessException {
    }

    // 4.1 createFreight(method)
    public void freightDecision(Boolean isFreight) throws DataAccessException {
    }

    // Optional: apply discount
    public void addDiscount(String type) throws DataAccessException {
    }

    // 5.1 confirmation()
    public void confirmation() throws DataAccessException {
    }
}
