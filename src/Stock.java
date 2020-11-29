import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Stock {

    private static final File f = new File("src/data/stock.txt");

    HashMap<Product, Integer> remain;
    ProductList pl;

    public Stock() throws IOException {
        remain = new HashMap<>();
        pl = new ProductList();
        pl.init();
    }

    public void init() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        String str = br.readLine();
        while (str != null) {
            String[] s = str.split(" ");
            assert (s.length == 2);
            String name = s[0];
            int num = Integer.parseInt(s[1]);
            Product p = this.pl.getProductInfoFromName(name);
            this.remain.put(p, num);
            str = br.readLine();
        }
        br.close();
    }

    public boolean isDealing(String productName) {
        return this.pl.validate(productName);
    }

    public boolean isAvailable(Order o) {
        for (Map.Entry<Product, Integer> e : o.orderList.entrySet()) {
            if (this.remain.get(e.getKey()) < e.getValue()) {
                return false;
            }
        }
        return true;
    }

    public void update() throws IOException {
        FileWriter fw = new FileWriter(f);

        for (Map.Entry<Product, Integer> e : this.remain.entrySet()) {
            fw.write(e.getKey().getName() + " " + e.getValue() + "\n");
        }

        fw.close();
    }

    public void takeOut(Order o) {
        for (Map.Entry<Product, Integer> e : o.orderList.entrySet()) {
            this.remain.merge(e.getKey(), -e.getValue(), Integer::sum);
        }
    }

    public void arrival(Product p, int num) {
        this.remain.merge(p, num, Integer::sum);
    }

    public void arrival(Order o) {
        for (Map.Entry<Product, Integer> e : o.getOrderList().entrySet()) {
            Product p = e.getKey();
            int num = e.getValue();
            arrival(p, num);
        }
    }
}
