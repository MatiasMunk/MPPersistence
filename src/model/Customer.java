package model;

public class Customer {
    private String phoneNumber, name, address, zipcode, city, type;

    public Customer(String phoneNumber, String name, String address, String zipcode, String type) {
        this.name = name;
        this.address = address;
        this.zipcode = zipcode;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
}
