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

    public double calculateSubtotal() {
		return 0;
//        double subtotal = 0.0;
//        for (Product p : products) {
//            subtotal += p.getPrice();
//        }
//        return subtotal;
    }

    public double calculateDiscount() {
		return 0;
//        if (discount == null)
//            return 0.0;
//        double subtotal = calculateSubtotal();
//        if (subtotal >= discount.getThreshold())
//            return (discount.getAmount() / 100.0) * subtotal;
//        return 0.0;
    }

    public double calculateFreightCost() {
		return 0;
//        if (freight == null)
//            return 0.0;
//        double subtotal = calculateSubtotal();
//        if (subtotal >= freight.getFreeThreshold())
//            return 0.0;
//        return freight.getBaseCost();
    }

    public double calculateTotal() {
        double subtotal = calculateSubtotal();
        double discountAmount = calculateDiscount();
        double freightCost = calculateFreightCost();
        totalAmount = subtotal - discountAmount + freightCost;
        return totalAmount;
    }

    // -----------------------------
    // Summary for debugging / printing
    // -----------------------------
    @Override
    public String toString() {
        return String.format(
            "SaleOrder #%d [%s]\nCustomer: %s\nItems: %d\nSubtotal: %.2f\nDiscount: %.2f\nFreight: %.2f\nTotal: %.2f",
            id,
            deliveryStatus,
            (customer != null ? customer.getName() : "N/A"),
            getProductCount(),
            calculateSubtotal(),
            calculateDiscount(),
            calculateFreightCost(),
            calculateTotal()
        );
    }
}
