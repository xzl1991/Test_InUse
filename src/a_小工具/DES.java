package a_小工具;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {
	private static String AKey= "9e1c0g17";
	private static String AIv= "a966g0g4";
	
	public static String encodeDES(String plaintext, String AuthKey,
			String AuthIv) throws NoSuchAlgorithmException,
			NoSuchPaddingException, Exception, UnsupportedEncodingException {

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(AuthKey.getBytes("GBK"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(AuthIv.getBytes("GBK"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return toHexString(cipher.doFinal(plaintext.getBytes("GBK")));

	}
	
	public static String encodeDES(String plaintext) throws Exception{
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(AKey.getBytes("GBK"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(AIv.getBytes("GBK"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return toHexString(cipher.doFinal(plaintext.getBytes("GBK")));
	}
	
	public static String decodeDES(String plaintext, String AuthKey,
			String AuthIv) throws NoSuchAlgorithmException,
			NoSuchPaddingException, Exception, UnsupportedEncodingException {

		byte[] bytesrc = StringToHex(plaintext);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(AuthKey.getBytes("GBK"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(AuthIv.getBytes("GBK"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte,"GBK");

	}

	public static byte[] StringToHex(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	public static String toHexString(byte b[]) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}

		return hexString.toString();
	}
	
	public  static void  main(String args[]){
		
		
		 try {
			 
			System.out.println(decodeDES("6e3848d51090d3ca70948cbc45bb725a852a03dcd7599e8b ","sfbzinte","sfbzinte")); 
			 //verifycode sfbzinte	ͨ��֤ID(UserId)+��_" + �����������+ "_" + ����yyyyMMdd��ʽ
			 System.out.println(encodeDES("43948859_����_20150422","sfbzinte","sfbzinte").toUpperCase());  
			 //�鼤��ʱ���
			 System.out.println(decodeDES("F1E2E4F16721CF08F3AAC31C8EAD4DF6F44D4369B4A0C8D8 ","sfbzinte","sfbzinte")); 
//			System.out.println(encodeDES("053264952700449@@@soufun","28d978cc","28d978cc").toUpperCase());  
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

}
