package a_可重入锁;

class Father {
	 synchronized void dos(){
		System.out.println("父类的...");
	}
}
public class Child extends Father{
	public synchronized void dos(){
		System.out.println("子类的...");
		super.dos();
	}
	public static void main(String[] args){
		Father f = new Child();
		f.dos();
	}
}