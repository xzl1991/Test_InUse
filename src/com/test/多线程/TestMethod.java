package com.test.多线程;

import java.util.ArrayList;
import java.util.List;

public class TestMethod {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Integer> url = new ArrayList<Integer>();
		List<String> url1 = new ArrayList<String>();
		List<String> url2 = new ArrayList<String>();
		for(int i=0;i<6;i++){
			url.add(i);
			url1.add(1+"第二个");
			url2.add(2+"第三个");
		}
//		int[] ary = url.get(index)
		for(int i=0;i<4;i++){
		 int a = url.get(i);
		 String b = url1.get(i);
		 String c = url2.get(i);
		 
		 if(i==3){
			 System.out.println("处理一下:"+b);
			 url1.set(i, "处理结果*****");
			 dealList(url1,i);
			 
			 System.out.println("处理一下:"+c);
			 url2.set(i, "处理结果*****");
			 dealList(url2,i);
		 }
		
		}
		
		for(int i=0;i<url1.size();i++){
			 int a = url.get(i);
			 String b = url1.get(i);
			 String c = url2.get(i);
			 
			 System.out.println(a);
			 System.out.println(b);
			 System.out.println(c);
			}
		
	}
	private List src = new ArrayList();
	private int iNO;
	public static void dealList(List<String> src,int i){
		System.out.println("deal方法："+src.get(i));
		src.set(i, "KKKKK");
//		System.out.println("deal2方法："+src.get(i));
//		src.remove(i);
		
	}
	public List getSrc() {
		return src;
	}
	public void setSrc(List src) {
		this.src = src;
	}
	public int getiNO() {
		return iNO;
	}
	public void setiNO(int iNO) {
		this.iNO = iNO;
	}

}
