package a_不常用类;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 判断汉字 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("sadsa".contains(""));
		String s = "鍖椾";
		System.out.println(s=="");
		String s1 = "123.sd@s.Com";
		System.out.println(s+"是否汉字："+c(s));
		System.out.println(s1 +" 包含汉字："+c(s1));
		s1= "北京";
		System.out.println(s1 +" 包含汉字："+c(s1));
		System.out.println(checkEmail(s1));
	}
	public static boolean c(String str){
//		String regEx = "[\\u4e00-\\u9fa5]"; 
		String regEx = "^(?:[\\x00-\\x7f]|[\\xe0-\\xef][\\x80-\\xbf]{2})+$"; 
		Pattern p = Pattern.compile(regEx);  
		  Matcher m = p.matcher(str);
		return m.find();  
	}
	public static boolean c1(String str){
		String regEx = "^http://[^('|\")]+?.(jpg|gif|bmp|png)$"; 
		Pattern p = Pattern.compile(regEx);  
		  Matcher m = p.matcher(str);
		return m.find();  
	}
	  public static boolean checkEmail(String email){
	        boolean flag = false;
	        try{
	                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	                Pattern regex = Pattern.compile(check);
	                Matcher matcher = regex.matcher(email);
	                flag = matcher.matches();
	            }catch(Exception e){
	                flag = false;
	            }
	        return flag;
	    }
}
