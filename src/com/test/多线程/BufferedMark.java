package com.test.多线程;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BufferedMark {
	public static void main(String [] args) throws Exception{
		File file = new File("E:"+File.separator+"test.txt");
		InputStream in = null;
		in = new FileInputStream(file);
		resetStream(input(in));
	}
	private static BufferedInputStream input(InputStream input) throws Exception{
		BufferedInputStream in = new BufferedInputStream(input,2) ;
		System.out.println("读取..");
		in.mark(3);
		return in;
	}
	
	private static void resetStream(InputStream input){
		try {
			System.out.println("reset.."+input.markSupported());
			int temp =0;
			byte b[] = new byte[10];
//			while((temp=input.read())!=-1){
//				in.read(b);
				temp = input.read();
				System.out.print((char)temp+".");
				System.out.print((char)input.read()+".");
				System.out.print((char)input.read()+".");
				input.reset();
				System.out.print((char)input.read()+".");
				System.out.print((char)input.read()+".");
//			}
			input.close();
			input.mark(10);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
