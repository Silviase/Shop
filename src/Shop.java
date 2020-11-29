import java.io.*;
import java.util.*;

public class Shop {
    private static final File reservationFile   = new File("src/data/reservation.txt");
    private static final File arrivalFile       = new File("src/data/arrival.txt");

    ArrayDeque<Order> reservations;
    MemberList ml;
    Stock stock;
    Customer currentUser;
    CUI cui;

    public Shop() throws IOException {
        this.reservations = new ArrayDeque<>();
        this.ml = new MemberList();
        this.stock = new Stock();
        this.currentUser = null;
        this.cui = new CUI();

        this.ml.init();
        this.stock.init();
        this.dailyArrival();
        this.initReservation();
        this.reservationArrival();
    }

    public void open() {
        logIn();
        cui.println("よぉ！" + this.currentUser.getName() + "！");
        cui.println("何か用かい？");

        boolean cont = true;
        while (cont) {
            cui.println("注文する              -----> 1");
            cui.println("例のブツを取りに来た  -----> 2");
            cui.println("特にない              -----> 0");

            switch (cui.readInt()) {
                case 0:
                    logOut();
                    cont = false;
                    break;
                case 1:
                    receiveOrder();
                    cui.println("まだ何か用かい？");
                    break;
                case 2:
                    absorbReservation();
                    cui.println("まだ何か用かい？");
                    break;
                default:
                    cui.println("どうしたんだ？");
                    break;
            }
        }
    }


    public void close() throws IOException {
        cui.println("在庫はこんな感じです。");
        cui.println(this.stock.remain.toString());
        this.stock.update();
        cui.println("予約はこんな感じです。");
        cui.println(this.reservations.toString());
        updateReservation();
    }

    private void logIn() {
        cui.println("大岡山酒店へようこそ！");
        cui.println("名前・ID(数字)・パスワードを空白区切りで入力してください");
        Customer c = cui.parseCustomer();
        if (ml.exists(c)) {
            this.currentUser = c;
            return;
        }

        cui.println("メンバーではないようです。");
        cui.println("今回はゲストユーザとしてログインしておきますね。");
        this.currentUser = new Customer("guest", 1, "guest");

    }

    private void logOut() {
        this.currentUser = null;
        cui.println("まいど！");
    }

    private void receiveOrder() {
        cui.println("注文は何だ？");
        cui.println("[商品名 数量] のように入力してください");
        cui.println("注文を終わる場合、[fin 0]と入力して改行してください");

        Order o = new Order(this.currentUser);

        while (true) {
            Pair<String, Integer> pair = cui.readStrInt();
            String name = pair.getT();
            int     num = pair.getU();

            if(name.equals("fin") && num == 0) {
                break;
            }

            if (stock.isDealing(name)) {
                Product product = stock.pl.getProductInfoFromName(name);
                o.add(product, num);
                cui.println("注文を受けたぜ");
            } else {
                cui.println("それは扱ってねぇんだ…すまんな。");
            }
        }

        if(o.containAgeVerifyProduct()){
            if(!verify()){
                cui.println("すまんな、未成年には酒は売れねえんだ。");
                return;
            }
        }

        if (this.stock.isAvailable(o)) {
            this.stock.takeOut(o);
            cui.println("料金は" + o.getTotalPrice() + "円だ。");
        } else {
            cui.println("すまんな、今切らしてるんだ。予約するか？[y/n]");
            if(cui.yesNo()) {
                this.reservations.add(o);
                cui.println("予約にはしっかり入れといたぜ。また来いよ。");
            } else {
                cui.println("そうか、悪かったな。");
            }
        }

    }

    private void dailyArrival() throws IOException {
        Scanner sc = new Scanner(new FileReader(arrivalFile));
        while (sc.hasNext()) {
            String name = sc.next();
            int num     = sc.nextInt();
            Product p   = this.stock.pl.getProductInfoFromName(name);
            this.stock.arrival(p, num);
        }
        sc.close();
    }

    private void reservationArrival() {
        for (Order o : this.reservations) {
            stock.arrival(o);
        }
    }

    private void initReservation() throws IOException {
        Scanner sc = new Scanner(new FileReader(reservationFile));
        while (sc.hasNext()) {
            HashMap<Product, Integer> hs = new HashMap<>();
            int customerID = sc.nextInt();
            int lineNum = sc.nextInt();

            Customer c = this.ml.searchFromID(customerID);
            Order o = new Order(c);
            for (int i = 0; i < lineNum; i++) {
                String name = sc.next();
                int num = sc.nextInt();
                Product p = this.stock.pl.getProductInfoFromName(name);
                o.add(p, num);
            }
            this.reservations.add(o);
        }
        sc.close();
    }

    private void absorbReservation() {
        for (int i = 0; i < this.reservations.size(); i++) {
            Order o = this.reservations.pollFirst();
            if (!o.getCustomer().equals(this.currentUser)) {
                this.reservations.addLast(o);
                continue;
            }

            if (this.stock.isAvailable(o)) {
                cui.println("ブツは用意できてるぜ…");
                cui.println("料金は" + o.getTotalPrice() + "円だ。");
                this.stock.takeOut(o);
            } else {
                cui.println("…すまねぇ。まだ用意できてねぇんだ。");
                cui.println("キャンセルするか？ [y/n]");
                if (cui.yesNo()) {
                    cui.println("わかった。取り消しておこう。");
                } else {
                    cui.println("わかった。もう一度予約しておこう。");
                    this.reservations.addLast(o);
                }
            }
            return;
        }
        cui.println("悪いがお前の予約はないみたいだ…");
    }

    private void updateReservation() throws IOException {
        FileWriter fw = new FileWriter(reservationFile);
        fw.write("");
        for (Order o : this.reservations) {
            fw.write(o.getCustomer().getCustomerID() + " " + o.getOrderList().size() + "\n");
            for (Map.Entry<Product, Integer> e : o.getOrderList().entrySet()) {
                fw.write(e.getKey().getName() + " " + e.getValue() + "\n");
            }
        }

        fw.close();
    }

    private boolean verify() {
        cui.println("お前、20歳以上か？[y/n]");
        return cui.yesNo();
    }

}
