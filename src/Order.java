import java.util.HashMap;
import java.util.Map;

public class Order {
    HashMap<Product, Integer> orderList;
    Customer customer;

    public Order(Customer c) {
        this.orderList = new HashMap<>();
        this.customer = c;
    }

    public void add(Product p, int num) {
        this.orderList.put(p, num);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Customer:\n");
        sb.append(this.customer.toString()).append("\n");
        sb.append("-------注文一覧-------\n");
        for (Map.Entry<Product, Integer> e : orderList.entrySet()) {
            sb.append(e.getKey().toString()).append(" : ").append(e.getValue()).append("個\n");
        }
        sb.append("合計金額:").append(this.getTotalPrice()).append("円\n");
        sb.append("----------------------\n");
        return new String(sb);
    }

    public int getTotalPrice() {
        int res = 0;
        for (Map.Entry<Product, Integer> e : orderList.entrySet()) {
			res += e.getKey().getPrice() * e.getValue();
        }
        return res;
    }
}
