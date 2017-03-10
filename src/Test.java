


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int a = (int) (Math.random()*100);
		double d = 100;
		System.out.println(a+"...");
		System.out.println((2<<3)+"...");
		Test t = new Test();
		int i = 0;
		String s = "f";
		i = i++;
		t.inInt(i);
		System.out.println(i);
		t.ins(s);
		System.out.println("String相加:"+s);
		String	s1 = new String("地址传递");
		t.ins(s1);
		System.out.println("new String()相加:"+s1);
		
	}
	public void ins(String s){
		s=s+"s";
		System.out.println("s:"+s);
	}
	public void inInt(int i){
		i++;
		System.out.println(i+"结果");
	}
}
