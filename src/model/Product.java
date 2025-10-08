package model;

public class Product {
    private int productNumber;
    private String name;
    private int minStock;

    public Product(int productNumber, String name, int minStock) {
        this.productNumber = productNumber;
        this.name = name;
        this.minStock = minStock;
    }

    public String getName() { return name; }
}
