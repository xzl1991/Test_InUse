package a_listener_Task;

public class TestZero {
	public static void main(String args[]){
		int a=1 ;int b=0;
		if(a!=0&&b!=0){
			System.out.println("ab都不是0---1");
		}
		if(!((a==0)&&(b==0))){
			System.out.println("ab都不是0---2");
		}
		if(!(a==0&&b==0)){
			System.out.println("ab都不是0---3");
		}
		
	}
}
