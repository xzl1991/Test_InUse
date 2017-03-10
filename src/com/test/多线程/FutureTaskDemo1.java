package com.test.多线程;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


public class FutureTaskDemo1 {  
          
    public static void main(String args[]){  
        Changgong worker = new Changgong();  
        FutureTask<Integer> jiangong = new FutureTask<Integer>(worker);  
        new Thread(jiangong).start();  
        while(!jiangong.isDone()){  
            try {  
                System.out.println("看长工做完了没...");  
                Thread.sleep(1000);  
            } catch (InterruptedException e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }  
        }  
        int amount;  
        try {  
            amount = jiangong.get();  
            System.out.println("工作做完了,上交了"+amount);  
        } catch (InterruptedException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (ExecutionException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
}  


 class Changgong implements Callable<Integer>{  
  
    private int hours=12;  
    private int amount;  
      
    @Override  
    public Integer call() throws Exception {  
        while(hours>0){  
            System.out.println("I'm working......");  
            amount ++;  
            hours--;  
            Thread.sleep(1000);  
        }  
        return amount;  
    }  
}


