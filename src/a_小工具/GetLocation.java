package a_小工具;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class GetLocation {
	public static void main(String[] args){
		System.out.println(getAddressByIP()+"....");
	}
	public static String getAddressByIP()
	{ 
		StringBuffer result1 = new StringBuffer(); 
	  try
	  {
	    String strIP = "218.30.109.26";
	    URL url = new URL( "http://ip.qq.com/cgi-bin/searchip?searchip1=" + strIP); 
	    URLConnection conn = url.openConnection(); 
	    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK")); 
	    String line = null; 
	    StringBuffer result = new StringBuffer(); 
	    while((line = reader.readLine()) != null)
	    { 
	      result.append(line); 
	    } 
	    reader.close(); 
	    strIP = result.substring(result.indexOf( "该IP所在地为：" ));
	    strIP = strIP.substring(strIP.indexOf( "：") + 1);
	    String province = strIP.substring(6, strIP.indexOf("省"));
	    String city = strIP.substring(strIP.indexOf("省") + 1, strIP.indexOf("市"));
	    result1.append(strIP).append(province).append(city);
	  }
	  catch( IOException e)
	  { 
	    return "读取失败"; 
	  }
	return result1.toString();
	}
}
