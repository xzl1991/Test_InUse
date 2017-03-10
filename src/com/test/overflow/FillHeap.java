package com.test.overflow;

import java.util.ArrayList;
import java.util.List;

public class FillHeap {

	/**
	 * @param args
	 */
	private static final int _1MB = 1024*1024;
	public static void main(String[] args) throws InterruptedException {
		fillHeap(1000);
		System.gc();
	}
	private static void fillHeap(int num) throws InterruptedException {
		// TODO Auto-generated method stub
		List<OOMObject> obj = new ArrayList<OOMObject>();
		for(int i=0;i<num;i++){
			Thread.sleep(50);
			obj.add(new OOMObject());
			
		}
		System.out.println("结束");
		
	}

}
class OOMObject{
	public byte[] placeholder = new byte[64*1024];
}











