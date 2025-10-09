package model;

public class Customer {
	private String name;
	private String phoneNo;
	private String type;
	
	public Customer(String name, String address, String zipcode, String city, String phoneNumber, String type) {
		this.name = name;
		this.phoneNo = phoneNumber; 
		this.type = type;
	}
		
	
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getPhoneno() {
			return phoneNo;
		}
		
		public void setPhoneno(String phoneno) {
			this.phoneNo = phoneno;
		}
		
		public String getType() {
			return type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
		
		public static Customer customer(String name, String address, String zipcode, String city, String phoneNumber, String type) {
			return new Customer(name, address, zipcode, city, phoneNumber, type);
		} 
	}
   