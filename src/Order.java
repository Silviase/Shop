import java.util.ArrayList;

public class Order {
	ArrayList<Product> orderList;
	Customer customer;
	
	public Order(Customer c) {
		this.orderList = new ArrayList<>();
		this.customer = c;
	}

	public void add(Product p) {
		this.orderList.add(p);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Customer:\n");
		sb.append(this.customer.toString());
		sb.append("-------注文一覧-------\n");
		for (Product p: orderList) {
			sb.append(p.toString());
		}
		sb.append("----------------------\n");
		return new String(sb);
	}
}
