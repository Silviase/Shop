import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Shop {
    HashMap<Product, Integer> stock;
    ArrayList<Order> reservations;
    HashSet<Product> handledProducts;

    public Shop(){
        this.stock = new HashMap<>();
        this.reservations = new ArrayList<>();
        this.handledProducts = new HashSet<>();
    }

    public void Receive(Order o) {
        // validation of order
        if(!validate(o)){
            System.out.println("注文に取り扱いのない商品があります");
            return;
        }

        // check stock
    }

    private boolean validate(Order o) {
        for (Product p: o.orderList){
            if(!handledProducts.contains(p)){
                return false;
            }
        }
        return true;
    }
}
