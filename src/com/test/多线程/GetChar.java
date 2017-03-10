
package com.test.多线程;
public class GetChar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char a =1;
		char b=20;
		char c =' ';
		String s = "<<<我>%是&&lt;&lt;&lt;谁《啊》";
		s=s.replaceAll("(<<<|>>>|《《《|《《|》》》|》》|》|&lt;&lt;&lt;|&gt;&gt;&gt;&gt;|&gt;&gt;&gt;|&gt;&gt;)", "");
		System.out.println(a+";"+b+";"+c);
		System.out.println(String.valueOf(c));
		System.out.println(s);
		String[] sp = s.split("是");
		for(String i:sp){
			System.out.println(i);
		}
		String sub = "<!--newcode:3611002718--> <!--gaoqing:-->";
		int begin = sub.indexOf("<!--newcode");
		int end = sub.indexOf("<!--newcode")+25;
		sub = sub.substring(begin,end);
		System.out.println("截取字符串:"+sub+"开始位置:"+begin+".结束位置:"+end);
		
		for(int i=0;i<10;i++){
			GetChar.getI();
		}
		
	}
	private static int j;
	public static void getI(){
		System.out.println(j);
		j++;
	}

}
