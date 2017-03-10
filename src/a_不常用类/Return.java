package a_不常用类;

public class Return {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(t(0));
	}
	public static String t(int a){
		
		try {
			if(a==0){
				return "是的四大";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			System.out.println("...");
		}
		return "是当官的";
	}
}
