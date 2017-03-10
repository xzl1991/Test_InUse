package 单例;

public class TestReturn {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(test());
		System.out.println(test1());
	}
	public static boolean test(){
		try {
			return true;
		} catch (Exception e) {
		}finally{
			return false;
		}
	}
	public static int test1(){
		int a = 1;
		try {
			return a;
		} catch (Exception e) {
		}finally{
			a = 2;
//			return a;
		}
		return a;
	}
}
