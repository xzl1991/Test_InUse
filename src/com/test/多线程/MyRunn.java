package com.test.多线程;

public class MyRunn implements Runnable {
	public void run() {
        try{
                 for(int i = 0;i < 10;i++){
                          Thread.sleep(100);
                          System.out.println("run:" + i);
                 }
        }catch(Exception e){}
}
}
