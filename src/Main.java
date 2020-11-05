public class Main {
    public static void main(String[] args) {
        Shop okayama = new Shop();

        Customer c = new Customer("東工大太郎");
        Order o = new Order(c);
        Wine sample = new Wine("sample", 114514);
        o.add(sample);

        System.out.println(o.toString());

    }
}
