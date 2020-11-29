import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
    	System.out.println("はじめ");
    	Shop shop = new Shop();
        shop.open();
        shop.close();
        System.out.println("おわり");
    }
}
