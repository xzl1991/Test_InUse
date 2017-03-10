package com.test.thread;



 
 public class TestSonNoStatic {
	 public static void main(String[] args) {
			// TODO Auto-generated method stub
		 System.out.println("子类");
		 TestInner1.TestIn t1 = new TestInner1.TestIn();
		}
//	 public TestSonNoStatic(){
//		 TestInner1.TestIn t1 = new TestInner1.TestIn();
//	 }
 }
 
 
 class TestInner1 {
		/**
		 * @param args
		 */
		
		public static int cal(int i){
			int a = i+8;
			return a;
			
		}
		public  class TestIn{
			public void info(){
				System.out.println("TestIn:内部父类");
			}
			public TestIn(){
				System.out.println("TestIn:构造器");
			}
		}
		public TestInner1(){
			TestIn t1 = new TestIn();
		}
		
	}












