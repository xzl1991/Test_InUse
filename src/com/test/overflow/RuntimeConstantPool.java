package com.test.overflow;

import java.util.ArrayList;
import java.util.List;

public class RuntimeConstantPool {

	/**
	 * @param args
	 */
	private static final int _1MB = 1024*1024;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//使用list 保存常量池 避免回收
		List<String> ls = new ArrayList<String>();
		//10m 的 permsize
		int i=0;
		while(true){
			ls.add(String.valueOf(i).intern());
		}
	}

}
