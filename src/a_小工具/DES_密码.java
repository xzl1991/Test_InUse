package a_小工具;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;


public class DES_密码 {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchAlgorithmException 
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, Exception {
		// TODO Auto-generated method stub
		String name="这个测试";
		name = DES.encodeDES(name, "12312311", "12312312");
		System.out.println("加密："+name);
		String	name1 = DES.decodeDES(name, "12312311", "12312312");
		System.out.println("jie密："+name1);
		String pass = "35D178201111EF9496F017B430EB737D";
		System.out.println( "sql:"+DES.decodeDES(pass, "achsankl", "achsankl"));
		 name="这个测试1";
		name = DES.encodeDES(name, "123", "456");
		System.out.println("加密1："+name);
		String	name11 = DES.decodeDES(name, "123", "456");
		System.out.println("jie密1："+name11);
		
	}

}


















