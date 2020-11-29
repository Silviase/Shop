import java.io.PrintWriter;
import java.util.Scanner;

public class CUI {

    Scanner in;
    PrintWriter out;
    PrintWriter err;

    public CUI() {
        in = new Scanner(System.in);
        out = new PrintWriter(System.out);
        err = new PrintWriter(System.err);
    }

    public String readStr(){
        String res;
        try {
            res = in.next();
        } catch (Exception e){
            err.println("Read String error");
            return null;
        }
        return res;
    }

    public int readInt(){
        int res;
        try {
            res = in.nextInt();
        } catch (Exception e){
            err.println("Not Int");
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

}
