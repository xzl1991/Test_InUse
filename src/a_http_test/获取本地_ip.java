package a_http_test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class 获取本地_ip {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		InetAddress address = InetAddress.getLocalHost(); 
		//获取本机 ip	
		String ip = address.getHostAddress().toString();
		System.out.println("本地ip:"+ip);
	}

}























