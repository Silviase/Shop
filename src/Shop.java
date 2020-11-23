import java.io.*;
import java.util.*;

public class Shop {
    private static final File memberFile = new File("src/data/member.txt");
    private static final File productFile = new File("src/data/product.txt");
    private static final File stockFile = new File("src/data/stock.txt");
    private static final File arrivalFile = new File("src/data/arrival.txt");

    HashMap<Product, Integer> stock;
    ArrayList<Order> reservations;
    HashMap<String, Product> handledProducts;
    HashSet<Customer> memberList;
    Customer currentUser;

    public Shop() throws IOException {
        this.stock = new HashMap<>();
        this.reservations = new ArrayList<>();
        this.handledProducts = new HashMap<>();
        this.memberList = new HashSet<>();
        this.currentUser = null;

        this.initMemberList();
        this.initProductList();
        this.initStock();
        this.mergeStock();

    }

    public void open() {
        boolean cont = true;
        userLogin();
        System.out.println("よぉ！" + this.currentUser.getName() + "！");
        System.out.println("何か用かい？");
        while(cont){
            Scanner sc = new Scanner(System.in);
            System.out.println("注文する            -----> 1");
            System.out.println("登録情報を変更する  -----> 2");
            System.out.println("特にない            -----> 0");

            int command = sc.nextInt();
            switch (command){
                case 0:
                    logOut();
                    cont = false;
                    break;
                case 1:
                    receiveOrder();
                    System.out.println("まだ何か用かい？");
                    break;
                case 2:
                    modifyAccount();
                    System.out.println("まだ何か用かい？");
                    break;
                default:
                    System.err.println("どうしたんだ？");
                    break;
            }
        }
    }

    public void close() throws IOException {
        System.out.println(this.stock.toString());
        updateStock();
    }

    private void initMemberList() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(memberFile));
        String str = br.readLine();
        while (str != null) {
            String[] s = str.split(" ");
            assert (s.length == 3);
            this.memberList.add(new Customer(s[0], Integer.parseInt(s[1]), s[2]));
            str = br.readLine();
        }
        br.close();
    }

    private void userLogin() {
        Scanner sc = new Scanner(System.in);
        System.out.println("大岡山酒店へようこそ！");
        System.out.println("名前を入力してください");
        String name = sc.next();
        System.out.println("IDを入力してください[数字]");
        int id = sc.nextInt();
        System.out.println("パスワードを入力してください");
        String password = sc.next();
        Customer c = new Customer(name, id, password);
        if(memberList.contains(c)){
            currentUser = c;
            return;
        }

        System.out.println("メンバーではないようです。");
        System.out.println("今回はゲストユーザとしてログインしておきますね。");
        this.currentUser = new Customer("guest", 1, "guest");

    }

    private void logOut() {
        this.currentUser = null;
        System.out.println("まいど！");
    }

    private void initProductList() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(productFile));
        String str = br.readLine();
        while (str != null) {
            String[] s = str.split(" ");
            assert (s.length == 2);
            String name = s[0];
            int price = Integer.parseInt(s[1]);
            Product p = Product.productFactory(name, price);
            this.handledProducts.put(name, p);
            str = br.readLine();
        }
        br.close();
    }

    public void receiveOrder(){
        System.out.println("注文は何だ？");
        System.out.println("[商品名 数量] のように入力してください");
        System.out.println("注文を終わる場合、何も入力せず改行してください");

        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        Order o = new Order(this.currentUser);
        while(!s.equals("")){
            String name = s.split(" ")[0];
            int num = Integer.parseInt(s.split(" ")[1]);

            if (validate(name)) {
                o.add(this.handledProducts.get(name), num);
                System.out.println("注文を受けたぜ");
            } else {
                System.out.println("それは扱ってねぇんだ…すまんな。");
            }
            s = sc.nextLine();
        }

        if (checkStock(o)) {
            takeOut(o);
            System.out.println("料金は" + o.getTotalPrice() + "円だ。");
        }else{
            System.out.println("すまんな、今切らしてるんだ。");
            System.out.println("予約にはしっかり入れといたぜ。");
        }

    }

    public void addMember(Customer c) throws IOException {
        if(c.getCustomerID() == -1) {
            // not generated
            int newID = this.generateCustomerID();
            c.setCustomerID(newID);
        }
        this.memberList.add(c);
        FileWriter wr = new FileWriter(memberFile);
        wr.write(c.toString());
        wr.write("\n");
        wr.close();
    }

    private int generateCustomerID() {
        return this.memberList.size();
    }

    private boolean validate(String s) {
        return this.handledProducts.containsKey(s);
    }

    private void modifyAccount() {}

    private void initStock() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(stockFile));
        String str = br.readLine();
        while (str != null) {
            String[] s = str.split(" ");
            assert (s.length == 2);
            String name = s[0];
            int num = Integer.parseInt(s[1]);
            Product p = this.handledProducts.get(name);
            this.stock.put(p, num);
            str = br.readLine();
        }
        br.close();
    }

    private void mergeStock() throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(arrivalFile));
        String str = br.readLine();
        while (str != null) {
            String[] s = str.split(" ");
            assert (s.length == 2);
            String name = s[0];
            int num = Integer.parseInt(s[1]);
            Product p = this.handledProducts.get(name);
            this.stock.merge(p, num, Integer::sum);
            str = br.readLine();
        }
        br.close();
    }

    public boolean checkStock(Order o) {
        for (Map.Entry<Product, Integer> e : o.orderList.entrySet()) {
            if(this.stock.get(e.getKey()) < e.getValue()) {
                return false;
            }
        }
        return true;
    }

    public void takeOut(Order o) {
        for (Map.Entry<Product, Integer> e : o.orderList.entrySet()) {
            this.stock.merge(e.getKey(), -e.getValue(), Integer::sum);
        }
    }

    private void updateStock() throws IOException {
        FileWriter fw = new FileWriter(stockFile);

        for (Map.Entry<Product, Integer> e : this.stock.entrySet()){
            fw.write(e.getKey().getName() + " " + e.getValue() + "\n");
        }

        fw.close();
    }

}
