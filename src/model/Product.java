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
    public int getProductNumber() {
		return productNumber;
	}
    
    public void setProductNumber(int productNumber) {
    	this.productNumber = productNumber;	
    }
    
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;	
	}
	
	public int getMinStock() {
		return minStock;
	}
	
	public void setMinStock(int minStock) {
		this.minStock = minStock;
	}
	
	@Override
	public String toString() {
	    return "Product[productNumber=" + productNumber +
	           ", name=" + name +
	           ", minStock=" + minStock + "]";
	}

}
