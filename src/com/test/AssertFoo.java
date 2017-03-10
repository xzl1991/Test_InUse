package com.test;

public class AssertFoo {
    public static void main(String args[]) {
    	System.out.println(1.0/0.0);
//    	System.out.println(1/0);
        //断言1结果为true，则继续往下执行
        assert true;
        System.out.println("断言1没有问题，Go！");
        int a = 4;
        System.out.println("\n-----------------\n"+a);
 
        //断言2结果为false,程序终止
        assert (1>2) : "断言失败，此表达式的信息将会在抛出异常的时候输出！";
        System.out.println("断言2没有问题，Go！");
    }
}