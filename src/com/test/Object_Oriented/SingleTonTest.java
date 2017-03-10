package com.test.Object_Oriented;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

 class SingleTon implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @param args
	 */
	private static SingleTon instance;
	private String name;
	private SingleTon(String name){
		System.out.println("构造方法");
		this.name = name ;
	} 
	public static SingleTon getInstance(String name){
		//只有instance为 null 时才创建该对象
		if(instance==null){
			instance = new SingleTon(name);
		}
		return instance;
	}
	//提供readresolve方法
	private Object readResolve(){
		return instance;
	}
	
}
public class SingleTonTest{
	public static void main(String[] args){
		//调用实例
		SingleTon s = SingleTon.getInstance("wolf");
		SingleTon s1 = null;
		ObjectOutputStream oos= null;
		ObjectInputStream ois = null;
		try {
			 oos = new ObjectOutputStream(new FileOutputStream("E://"+"a1.bin"));
			 ois = new ObjectInputStream(new FileInputStream("E://"+"a1.bin"));
			 //输出实例 
			 oos.writeObject(s);
			 oos.flush();
			 //获取
				s1 = (SingleTon) ois.readObject();
				System.out.println("相等"+s.equals(s1));
				System.out.println("是相同对象:"+(s==s1));
		} catch (final Exception e) {
			e.printStackTrace();
		} finally{
				try {
					if(oos==null){
					oos.close();
					}
					if(ois==null){
						ois.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
	}
}
















