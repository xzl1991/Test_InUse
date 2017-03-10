package com.test.Object_Oriented;

public class TestInstance1 {
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		TestInstance1 t = new TestInstance1();
		t.info("", 13);
	}
	public void info(String st,int num) throws  Exception{
		System.out.println(new inner());
		//使用反射方式
		System.out.println(inner.class.newInstance());
		System.out.println("字符:"+st);
		System.out.println("数字:"+num);
	}
	public void info(String[] st,int num){
		System.out.println("字符集:"+st);
		System.out.println("数字:"+num);
	
	}
//	TestInstance1 t1 =null;
//	{
//	t1= new TestInstance1();
//	}
	public class inner{
			public inner(){
				System.out.println("内部类");
			}
		};
	public String toString(){
		return "Inner对象";
	}
}
