package com.test.thread;

public class AccountSyn {
	private double balance;
	private  String accountNo;
	public AccountSyn(String accountNo, double balance) {
		// TODO Auto-generated constructor stub
		this.accountNo = accountNo;
		this.balance = balance ;
	}

	public  void draw(double drawAccount){
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("卡里有钱:"+balance);
		if(balance>=drawAccount){
			System.out.println(Thread.currentThread().getName()+"取钱成功:"+drawAccount);
			balance = balance -drawAccount ;
			System.out.println("剩余:"+balance);
		}else{
			System.out.println("余额不足，取钱失败！余额:"+balance);
		}
	}
	
	public  int hashCode(){
		return accountNo.hashCode();
	}
	public boolean equals(Object obj){
		if(obj==this){
			return true;
		}
		if(obj!=null&&obj.getClass()==AccountSyn.class){
			AccountSyn targ = (AccountSyn) obj;
			return targ.accountNo.equals(accountNo);
		}
		return false;
		
	}
}
