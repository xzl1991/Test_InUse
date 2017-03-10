package a_数据库连接池;

public class A {
	 //静态块
	 static {
	  A a = new A();
	  i = 10;
	 }
	 
	 public static int i;
	 public int j;
	 
	 public A() {
	  System.out.println(A.i);
	  System.out.println(j);
	 }
	 
	 public static void main(String[] args) {
	  A a = new A();
	 }
	}