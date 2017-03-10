package a_小工具;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.LinkedList;


public class PoolLinked {

	/**
	 * @param args
	 */
	static LinkedList<String> pool = new LinkedList<String>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/**
		 * 获取数据源单例
		 */
		for(int i=0;i<10;i++){
			System.out.println("开始："+getLink(i));
			System.out.println("开始："+getLink(i));
			freeConnection("s"+i);
			System.out.println(pool.size());
		}
		
		for(int i=0;i<10;i++){
			System.out.println("pool的数据"+pool.get(i).toString());
			
		}
		for(int i=0;i<pool.size();i++){
			System.out.println(pool.get(i).toString());
		}
	}
	public static String getLink(int i){
		synchronized (pool) {
			if (pool.size() > 0) {
				return pool.removeFirst();
			} else {
				pool.add("s"+i);
				return pool.removeFirst();
			}
		}
	}
	public static void freeConnection(String conn) {
		pool.addLast(conn);
	}

}
