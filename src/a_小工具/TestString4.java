package a_小工具;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sun.xml.internal.ws.util.StringUtils;


public class TestString4 {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException  {
		String s = "sdsa?sds";
		System.out.println(s.split(","));
		Collection<String> li = new ArrayList<String>();
		li.add("第1");
		li.add("第2");
		li.add("第3");
		li.add("第4");
		String a1 = "%BC%C3%C4%CF";
		 String sys = System.getProperty("os.name").toLowerCase();
		 int a = 5;
		 a= a<<2;
		 System.out.println("左移2:"+a);
		 
		 System.out.println("本机的系统:"+URLDecoder.decode(a1, "gb2312"));
	}
}

