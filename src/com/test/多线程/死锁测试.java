package com.test.多线程;

public class 死锁测试 implements Runnable{

    /**
     * @param args
     */
	private static  boolean state;
    public static void main(String[] args) {
        死锁测试 lock1=new 死锁测试();
        死锁测试 lock2=new 死锁测试();
        Thread thread1=new Thread(lock1);
        Thread thread2=new Thread(lock2);
        lock1.setFlag(0);
        lock2.setFlag(1);
        thread1.start();
        thread2.start();
        state = true;
    }
    
    private static Object obj1=new Object();//一把A筷子的锁
    private static Object obj2=new Object();//一把B筷子的锁
    private int flag=0;
    
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public void eatMeat(){
             synchronized (obj1) {
                 System.out.println(Thread.currentThread().getName()+":拿到A筷子！");
                try {
                	obj1.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                synchronized (obj2) {
                    System.out.println(Thread.currentThread().getName()+":拿到B筷子！开始吃饭");
                }
                }
    }
    	public void eatVegetable(){
             synchronized (obj2) {
                    System.out.println(Thread.currentThread().getName()+":拿到B筷子！");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    synchronized (obj1) {
                        System.out.println(Thread.currentThread().getName()+":拿到A筷子！开始吃菜");
                        try {
                        	obj1.notifyAll();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                    }
                }
    }

    @Override
    public void run() {
    	if(state){
    		System.out.println("-----"+state);
    	}else{
    		System.out.println("-****-"+state);
    	}
    	System.out.println(".....???"+flag);
        if(flag==0){
            eatMeat();
        }else{
            eatVegetable();
        }
    }
    	
//    	@Override
//        public void run() {
//            eatMeat();
//        }
}
