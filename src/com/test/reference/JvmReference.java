package com.test.reference;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

public class JvmReference {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		Person per = null;
//		for(int i=0;i<10;i++){
//			per = new Person("张三"+i,i+10);
//			
//			System.out.println(per.toString());
//		}
//		//
//		System.out.println(per.getName()+","+per.getAge());
//		System.gc();
//		System.runFinalization();
//		System.out.println(per.getName()+","+per.getAge());
//		Person[] person = new Person[10000000];
//		for(int j =0;j<1000000;j++){
//			person[j] = new Person("李四"+j,j+10);
//			System.out.println(person[j].toString());
//		}
//		System.out.println(person[1].getName()+","+per.getAge());
//		System.gc();
//		System.runFinalization();
//		System.out.println(person[4].getName()+","+per.getAge());
		//软引用
		SoftReference<Person>[] person1 = new SoftReference[100000];
		for(int j =0;j<person1.length;j++){
			person1[j] = new SoftReference<Person>(new Person("弱引用"+j,j+10));;
			System.out.println(person1[j].get().toString());
		}
		System.out.println(person1[4].get().getName()+","+person1[4].get().getAge());
		System.gc();
		System.runFinalization();
		System.out.println(person1[4].get().getName()+","+person1[4].get().getAge());
	}

}














