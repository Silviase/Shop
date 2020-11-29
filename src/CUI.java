import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CUI {

    Scanner in;
    PrintWriter out;

    public CUI() {
        in = new Scanner(System.in);
        out = new PrintWriter(System.out, true);
    }
    
    public void init(InputStream in, OutputStream out) {
    	this.in = new Scanner(in);
    	this.out = new PrintWriter(out, false);
    }

    public String readStr(){
        String res;
        try {
            res = in.next();
        } catch (Exception e){
            return null;
        }
        return res;
    }

    public int readInt(){
        int res;
        try {
            res = in.nextInt();
        } catch (Exception e){
            return -1;
        }
        return res;
    }

    public void println(String s){
        out.println(s);
    }

    public boolean yesNo() {
        String ans = readStr().toLowerCase();
        return ans.equals("y") || ans.equals("yes");
    }

    public Pair<String, Integer> readStrInt() {
        String name = in.next();
        int num = in.nextInt();
        return new Pair<>(name, num);
    }

    public Customer parseCustomer(){
        String name = in.next();
        int id = in.nextInt();
        String password = in.next();

        return new Customer(name, id, password);
    }
}
