package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleOrder {
    private int id;
    private Date orderDate;
    private double totalAmount;
    private String deliveryStatus;
    private Date deliveryDate;

    private Customer customer;
    private Freight freight;
    private Discount discount;
    private List<Product> products;

    public SaleOrder(Date orderDate, double totalAmount, String deliveryStatus, Date deliveryDate) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.deliveryStatus = deliveryStatus;
        this.deliveryDate = deliveryDate;
        this.products = new ArrayList<>();
    }

    // -----------------------------
    // Basic getters / setters
    // -----------------------------
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public Date getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Freight getFreight() { return freight; }
    public void setFreight(Freight freight) { this.freight = freight; }

    public Discount getDiscount() { return discount; }
    public void setDiscount(Discount discount) { this.discount = discount; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    // -----------------------------
    // Business logic
    // -----------------------------
    public void addProduct(Product p) {
        if (p != null)
            products.add(p);
    }

    public int getProductCount() {
        return products.size();
    }
}
