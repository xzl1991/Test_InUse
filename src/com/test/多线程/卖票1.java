package com.test.多线程;

import java.util.concurrent.atomic.AtomicInteger;

public class 卖票1 {
	 
    public static void main(String[] args) {
     
            Ticket ticket = new Ticket();
            BuyTicket buyTicket = new BuyTicket();
            buyTicket.setTicket(ticket);
            new Thread(buyTicket,"窗口1").start();;
            new Thread(buyTicket,"窗口2").start();;
            new Thread(buyTicket,"窗口3").start();;
    }
 
}
 
 class Ticket {
    private int  ticketNum = 20;
       public  void sellTicket() {
        ticketNum--;
    }
    public   int  getTicketNum() {
         return ticketNum;
    }
}
  
 class BuyTicket implements Runnable {
     private Ticket ticket;
       
     public  void setTicket(Ticket ticket) {
         this.ticket = ticket;
     }
   
     @Override
     public  void run() {
      while(true){                    //线程开始执行之后，先进入循环，再执行同步代码。这样避免了数据读写不同步的现象。
             synchronized (ticket) {
                
              if(ticket.getTicketNum()>0){
                        ticket.sellTicket();
                        System.out.println(Thread.currentThread().getName() + "卖了一张票剩余"+ticket.getTicketNum()+"张票" );
                   }
               else {
                       break;
               }
        }
     } 
      }       
 }
