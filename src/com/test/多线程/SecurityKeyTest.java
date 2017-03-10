package com.test.多线程;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurityKeyTest {
	public static void main(String[] args) throws Exception{
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");
			//对称加密算法
			SecretKey key = generator.generateKey();
			saveFile("test1.txt", key.getEncoded());
			System.out.println();
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			String text = "Hello World";
			//使用加密
			byte[] encrypted = cipher.doFinal(text.getBytes());
			saveFile1("encrypted.bin", encrypted);
			
			System.out.println();
			saveFiles("没加密.bin");
			
			
			//读取
//			byte[] keyData = getData("test1.txt");
//			SecretKeySpec keySpec = new SecretKeySpec(keyData, "DES");
//			Cipher cipher1 = Cipher.getInstance("DES");
//			cipher.init(Cipher.DECRYPT_MODE, keySpec);
			
			System.out.println("读取:");
			byte[] data = getData("encrypted.bin");
			byte[] result = cipher.doFinal(data);
			for(int i=0;i<result.length;i++){
				System.out.print((char)result[i]);
			}
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static  byte[] getData(String string) throws Exception {
		// TODO Auto-generated method stub
		File readFile = new File("E:"+File.separator+string);
		InputStream input = new FileInputStream(readFile);
		int temp = 0;
		byte b[] = new byte[100];
		System.out.println("开始..");
//		while(input.read()!=-1){
////			System.out.print((char)temp);
////			b=(byte)input.read();
//		}
		input.close();
		return null;
	}

	private static void saveFile(String string, byte[] encoded) throws Exception {
		// TODO Auto-generated method stub
		File in = new File("E:"+File.separator+"test.txt");
		File saveFile = new File("E:"+File.separator+string);
		InputStream input = new FileInputStream(in);
		OutputStream output = new FileOutputStream(saveFile);
		int temp = 0;
 		while((temp=input.read())!=-1){
 			System.out.print((char)temp);
 			output.write(temp);
 		}
 		input.close();
 		output.close();
	}
	private static void saveFile1(String string, byte[] encoded) throws Exception {
		// TODO Auto-generated method stub
		File saveFile = new File("E:"+File.separator+string);
		OutputStream output = new FileOutputStream(saveFile);
 		for(int i=0;i<encoded.length;i++){
 			System.out.print((char)encoded[i]+".");
 			output.write(i);
 		}	
 		output.close();
	}
	private static void saveFiles(String string1) throws Exception {
		// TODO Auto-generated method stub 写文件
		File saveFile = new File("E:"+File.separator+string1);
		OutputStream output = new FileOutputStream(saveFile);
		String text1 = "Hello World";
		byte[] byt = text1.getBytes();
 		for(int i=0;i<byt.length;i++){
 			System.out.print((char)byt[i]);
 			output.write(i);
 		}	
 		
 		//读文件
 		File readFile = new File("E:"+File.separator+"test.txt");
		InputStream input = new FileInputStream(readFile);
		int temp = 0;
		byte b[] = new byte[]{1,2,3};
		for(byte i:b){
			System.out.println(i);
		}
		System.out.println("开始..");
		while((temp=input.read())!=-1){
			input.read();
			System.out.print("读出的:"+(char)temp);
		}
		input.close();
 		output.close();
	}
}	
