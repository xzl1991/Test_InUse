package com.test.多线程;

/* @author abc
*
*/
public class 死锁测试1 implements Runnable{

   /**
    * @param args
    */
   public static void main(String[] args) {
       死锁测试1 lock1=new 死锁测试1();
       死锁测试1 lock2=new 死锁测试1();
       Thread thread1=new Thread(lock1);
       Thread thread2=new Thread(lock2);
       lock1.setFlag(0);
       lock2.setFlag(1);
       thread1.start();
       thread2.start();
   }
   
   private static Object obj1=new Object();//一把A筷子的锁
   private static Object obj2=new Object();//一把B筷子的锁
   private static Object obj3=new Object();//加一把锁，保证A锁和B锁锁上同一组，只有拿到此锁才能拿到A锁或者B锁
   private int flag=0;
   
   public int getFlag() {
       return flag;
   }
   public void setFlag(int flag) {
       this.flag = flag;
   }
   public void eatMeat(){
       synchronized (obj3) {
            synchronized (obj1) {
                System.out.println(Thread.currentThread().getName()+":拿到A筷子！");
               try {
                   Thread.sleep(1000);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
               synchronized (obj2) {
                   System.out.println(Thread.currentThread().getName()+":拿到B筷子！开始吃饭");
               }
               }
       }
   }
   public void eatVegetable(){
       synchronized (obj3) {
            synchronized (obj2) {
                   System.out.println(Thread.currentThread().getName()+":拿到B筷子！");
                   try {
                       Thread.sleep(1000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   
                   synchronized (obj1) {
                       System.out.println(Thread.currentThread().getName()+":拿到A筷子！开始吃饭");
                   }
               }
   //    }
       }
   }

   @Override
   public void run() {
       if(flag==0){
           eatMeat();
       }else{
           eatVegetable();
       }
   }
}
