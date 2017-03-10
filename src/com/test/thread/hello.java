package com.test.thread;

/**
 * @author Rollen-Holt 线程的中断
 * */
class hello implements Runnable {
	 public static void main(String[] args) {
	        hello he = new hello();
	        Thread demo = new Thread(he, "线程");
	        demo.start();
	        try{
	            Thread.sleep(2000);
	        }catch (Exception e) {
	            e.printStackTrace();
	        }
	        demo.interrupt(); //2s后中断线程
	    }
    public void run() {
        System.out.println("执行run方法");
        try {
            Thread.sleep(1000);
            System.out.println("线程完成休眠");
        } catch (Exception e) {
            System.out.println("休眠被打断");
            return;  //返回到程序的调用处
        }
        System.out.println("线程正常终止");
    }
}
