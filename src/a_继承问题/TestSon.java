package a_继承问题;

public class TestSon {
	public static void main(String[] args){
		Father fa = new Son();
		System.out.println(fa.name);
		fa.say();
		int a ;
		for(int i=0;i<5;i++){
			a=i;
			System.out.println(a);
			
		}
	}
}
