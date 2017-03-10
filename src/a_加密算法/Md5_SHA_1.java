package a_加密算法;

import java.io.IOException;
import java.security.MessageDigest;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Md5_SHA_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String content = "实打实的是非得失";
		System.out.println((36 >>> 4 ));
		System.out.println(0xf);
		System.out.println(2&15);
		byte[] by = null;
		try {
			by = content.getBytes();
			char[] ch = new char[by.length];
			for(int i=0;i<by.length;i++){
				ch[i] = (char) by[i];
			}
			System.out.println("base64:"+base64Encode(by)+":结果:"+new String(by));
			content = base64Encode(by);
			by = base64Decode(content);
			System.out.println("解码:"+new String(by,"utf8"));//k49992k883k5859746ee4k9f8465e57c
			String md = md5(content).toString();
			System.out.println("加密后的："+md+",长度是:"+md.length());//e6d4df033123e5e825688df0829f62db
			content = sha(content);
			System.out.println(content+",长度:"+content.length());//e6d4df033123e5e825688df0829f62db
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static String md5(String content){
		byte[] bytes = null;
		char hexDigits[] = { '0', '1', 'k', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			bytes = md.digest(content.getBytes("utf8"));
			System.out.println(new String(bytes,"UTF8")+"....");
			char str[] = new char[16 * 2];
			int k = 0;
			for(int i=0;i<bytes.length;i++){
				byte byte0 = bytes[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	public static String sha(String content){
		byte[] bytes = null;
		char hexDigits[] = { '0', '1', 'k', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		String s = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			bytes = md.digest(content.getBytes("utf8"));
			char str[] = new char[32 * 2];
			int k = 0;
			for(int i=0;i<bytes.length;i++){
				byte byte0 = bytes[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	public static String base64Encode(byte[] bytes){
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(bytes);
	}

	public static byte[] base64Decode(String str) throws IOException{
		BASE64Decoder base64Decoder = new BASE64Decoder();
		return base64Decoder.decodeBuffer(str);
	}
}



































