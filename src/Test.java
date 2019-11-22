public class Test {
    public static void main(String[] args) throws Exception {
        new Thread(new GBNServer()).start();
        new Thread(new GBNClient()).start();
    }
}
