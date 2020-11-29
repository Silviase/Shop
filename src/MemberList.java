import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class MemberList {

    private static final File memberFile = new File("src/data/member.txt");
    HashSet<Customer> ml;

    public MemberList() {
        ml = new HashSet<>();
    }

    public void init() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(memberFile));
        String str = br.readLine();
        while (str != null) {
            String[] s = str.split(" ");
            assert (s.length == 3);
            this.ml.add(new Customer(s[0], Integer.parseInt(s[1]), s[2]));
            str = br.readLine();
        }
        br.close();
    }

    public boolean exists(Customer c){
        return this.ml.contains(c);
    }

    public HashSet<Customer> getMemberList() {
        return ml;
    }

    public Customer searchFromID(int customerID) {
        for (Customer c : this.ml) {
            if (c.getCustomerID() == customerID) {
                return c;
            }
        }
        return null;
    }

    public void addMember(String name) {
        Scanner sc = new Scanner(System.in);
        System.out.println("パスワードを決めて下さい");
        String password = sc.next();

        Customer nc = new Customer(name, this.generateID(), password);
        ml.add(nc);
        System.out.println("あなたは" + nc.toString() + "です。よく覚えておいてね。");
    }

    private int generateID() {
        return this.ml.size();
    }
}
