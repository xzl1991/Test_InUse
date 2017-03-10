package com.test.Object_Oriented;

import java.io.Serializable;

public class Wolf_Serialize implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name ;
	public Wolf_Serialize(String name){
		System.out.println("构造器");
		this.name = "狼人";
		System.out.println(name.hashCode());
	}
	public boolean equals(Object obj){
		if(obj==this){
			return true;
		}else if(obj.getClass()==Wolf_Serialize.class){
			//判断是否相同类
			Wolf_Serialize wo = (Wolf_Serialize) obj;
			//判断数值
			return wo.name.equals(this.name);
		}
		return false;
	}
	public int hashcode(){
		return name.hashCode();
		
	}
//	public static void main(String [] args){
//		Wolf_Serialize w = new Wolf_Serialize();
//	}
}
