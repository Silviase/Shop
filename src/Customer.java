import java.util.Objects;

public class Customer {
    // passwordをCustomer管理にする
    private final String name;
    private int customerID;
    private final String password;


    public Customer(String s, int ID, String password) {
        this.name = s;
        this.customerID = ID;
        this.password = password;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return this.name + " " + this.customerID;
    }

    public String getPassword(){
        return this.password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerID == customer.customerID &&
                name.equals(customer.name) &&
                password.equals(customer.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, customerID, password);
    }
}
