import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ProductList {

    private static final File productFile = new File("src/data/product.txt");
    HashMap<String, Product> productHashMap;

    public ProductList() {
        this.productHashMap = new HashMap<>();
    }

    public void init() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(productFile));
        String str = br.readLine();
        while (str != null) {
            String[] s = str.split(" ");
            assert (s.length == 2);
            String name = s[0];
            int price = Integer.parseInt(s[1]);
            Product p = Product.productFactory(name, price);
            this.productHashMap.put(name, p);
            str = br.readLine();
        }
        br.close();
    }

    public boolean validate(String name){
        return this.productHashMap.containsKey(name);
    }

    public Product getProductInfoFromName(String name){
        if(validate(name)){
            return this.productHashMap.get(name);
        }
        return null;
    }

    
}
