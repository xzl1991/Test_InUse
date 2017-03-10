package a_tryFinally;

public class TryCatchFinally {
	 
    @SuppressWarnings("finally")
    public static final String test() {
        String t = "";
 
        try {
            t = "try";
            return t;
        } catch (Exception e) {
            // result = "catch";
            t = "catch";
            return t;
        } finally {
            t = "finallys";
            System.out.println(t+"....");
        }
    }
 
    public static void main(String[] args) {
        System.out.print(TryCatchFinally.test());
    }
 
}