package com.test.Object_Oriented;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializedTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Wolf_Serialize w = new Wolf_Serialize("dog");
		Wolf_Serialize w1 = null;
		ObjectOutputStream oos = null;
		ObjectInputStream ois = null;
		try {
			//创建输入输出流对象
			oos = new ObjectOutputStream(new FileOutputStream("E://"+"a.bin"));
			ois = new ObjectInputStream(new FileInputStream("E://"+"a.bin"));
			//输出
			oos.writeObject(w);
			oos.flush();
			//反序列化 恢复 java对象
			w1 = (Wolf_Serialize) ois.readObject();
			System.out.println(w.equals(w1));
			//不是同一个对象
			System.out.println(w==w1);
			
		} catch (final Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
				try {
					if(oos!=null){
						oos.close();
					}
					if(ois!=null){
						ois.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
	}

}
