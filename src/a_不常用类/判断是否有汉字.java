package a_不常用类;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 判断是否有汉字 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {  
		  //方法一：  
		  
		  String s1 = "我是中国人";  
		  String s2 = "imchinese";  
		  String s3 = "im中国人";  
		  System.out.println(s1 + ":" + new String(s1).length());  
		  System.out.println(s2 + ":" + new String(s2).length());  
		  System.out.println(s3 + ":" + new String(s3).length());  
		  System.out.println((s1.getBytes().length == s1.length()) ? "s1无汉字":"s1有汉字");  
		  System.out.println((s2.getBytes().length == s2.length()) ? "s2无汉字":"s2有汉字");  
		  System.out.println((s3.getBytes().length == s3.length()) ? "s3无汉字":"s3有汉字");  
		  
		  //方法二：  
		  
		  int count = 0;  
		  String regEx = "[\\u4e00-\\u9fa5]";  
		  String str = "中文fd我是中国人as ";  
		  Pattern p = Pattern.compile(regEx);  
		  Matcher m = p.matcher(str);  
		  while (m.find()) {  
		     for (int i = 0; i <= m.groupCount(); i++) {  
		       count = count + 1;  
		     }  
		  }  
		  System.out.println("共有 " + count + "个 ");  
		 }
	public static boolean conta(String str){
		String regEx = "[\\u4e00-\\u9fa5]"; 
		Pattern p = Pattern.compile(regEx);  
		  Matcher m = p.matcher(str);
		return m.find();  
	}
}
