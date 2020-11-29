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
        cui.println("名前を入力してください");
        String name = cui.readStr();

        cui.println("IDを入力してください[数字]");
        int id = cui.readInt();

        cui.println("パスワードを入力してください");
        String password = cui.readStr();

        Customer c = new Customer(name, id, password);
        if (ml.exists(c)) {
            currentUser = c;
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
        cui.println("注文を終わる場合、何も入力せず改行してください");

        Order o = new Order(this.currentUser);

        while (cui.in.hasNext()) {
            Pair<String, Integer> pair = cui.readStrInt();
            String name = pair.getT();
            int     num = pair.getU();

            if (stock.isDealing(name)) {
                Product product = stock.pl.getProductInfoFromName(name);
                o.add(product, num);
                cui.println("注文を受けたぜ");
            } else {
                cui.println("それは扱ってねぇんだ…すまんな。");
            }
        }

        if(o.containAgeVerifyProduct() || !verify()){
            cui.println("すまんな、未成年には酒は売れねえんだ。");
            return;
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
        BufferedReader br = new BufferedReader(new FileReader(reservationFile));
        String str = br.readLine();
        while (str != null) {
            HashMap<Product, Integer> hs = new HashMap<>();
            String[] s = str.split(" ");
            int customerID = Integer.parseInt(s[0]);
            int lineNum = Integer.parseInt(s[1]);

            Customer c = this.ml.searchFromID(customerID);
            Order o = new Order(c);
            for (int i = 0; i < lineNum; i++) {
                String nameNum = br.readLine();
                String name = nameNum.split(" ")[0];
                int num = Integer.parseInt(nameNum.split(" ")[1]);
                Product p = this.stock.pl.getProductInfoFromName(name);
                o.add(p, num);
            }
            this.reservations.add(o);
            str = br.readLine();
        }
        br.close();
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
