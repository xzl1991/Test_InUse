package a_不常用类;

public class TestWhile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int i=6;
		
	}
	public static int test(int i){
		while(i>0){
			System.out.println("sss"+i);
			i--;
			if(i==3){
				break;
			}
		}
		return i;
	}
}
