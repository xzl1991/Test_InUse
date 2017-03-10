package a_加密算法;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES_test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String name = "hello,i am chenkanxiang,good night!";
		SecretKey key = null;
		byte[] bytes = null;
		try {
			 key = loadkey(getKeyAES());
			 System.out.println(getKeyAES());
			 bytes = encryptAES(name.getBytes(),key);
			 
			 //des 加密
			 bytes = encryptAES(name.getBytes(),key);
			 String s = Md5_SHA_1.base64Encode(bytes);
			 System.out.println("des:"+s);
			 bytes = decryptDES(Md5_SHA_1.base64Decode(s),key);
			 System.out.println("解码:"+new String(bytes));
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String getKeyAES() throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");  
		keyGen.init(128);
//		keyGen.init(256);// 加密软件的出口管制原因
		SecretKey key = keyGen.generateKey();
		String base64Str = Md5_SHA_1.base64Encode(key.getEncoded());
		return base64Str;
	}
	public static SecretKey loadkey(String base64Str) throws IOException{
		byte[] bytes = Md5_SHA_1.base64Decode(base64Str);
		SecretKey key = new SecretKeySpec(bytes,"AES");
		return key;
	}
	/*
	 * 加密解密
	 */
	public static byte[] encryptAES(byte[] source,SecretKey key) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] bytes = cipher.doFinal(source);
		return bytes;
	}
	public static byte[] decryptDES(byte[] source,SecretKey key) throws Exception{
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] bytes = cipher.doFinal(source);
		return bytes;
	}
}








































