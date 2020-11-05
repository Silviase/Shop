
public class Customer {
	private int customerID;
	private String name;
	
	public Customer(String s) {
		this.name = s;
	}

	public String toString() {
		return "ID: " + this.customerID + "\n"
				+ "Name: " + this.name + "\n";
	}
}
