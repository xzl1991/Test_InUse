
public class 验证字符串Int {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "10";
		System.out.println(checkParamIsInt(s));
	}
	public static boolean checkParamIsInt(String str) {
		boolean result=str.matches("[0-9]+");
		if(result==true){
			return false;
		}else{
			return true;
		}
	}
}
