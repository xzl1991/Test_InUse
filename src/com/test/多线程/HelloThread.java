package com.test.多线程;
public class HelloThread implements Runnable {
 
    public HelloThread() {
 
    }
 
    public HelloThread(String name) {
        this.name = name;
    }
 
    public void run() {
        for (int i = 0; i < 5; i++) {
        	if(Thread.currentThread().getName().contains("0")){
					System.out.println("死线程");
					for(int j=0;j<100000000;j++){
						
					}
					// TODO Auto-generated catch block
        	}else{
        		System.out.print(name + "运行     " + i);
        		System.out.println(Thread.currentThread().getName());
        	}
        }
    }
 
    public static void main(String[] args) {
        HelloThread h1=new HelloThread("线程A");
        Thread demo= new Thread(h1);
        HelloThread h2=new HelloThread("线程Ｂ");
        Thread demo1=new Thread(h2);
        demo.start();
        demo1.start();
    }
 
    private String name;
}