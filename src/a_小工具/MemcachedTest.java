package a_小工具;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class MemcachedTest {
	public static void main(String args[]){
		System.out.println("***开始");
		printValues();
		System.out.println("***结束");
	}
	public static void printValues() {  
        MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses("localhost:11211"));  
        MemcachedClient memcachedClient = null;  
        try {  
            memcachedClient = builder.build();  
            memcachedClient.set("hello", 0, "Hello,xmemcached");  
            String value = memcachedClient.get("hello");  
            System.out.println("hello=" + value);  
            memcachedClient.delete("hello");  
            value = memcachedClient.get("hello");  
            System.out.println("hello=" + value);  
        } catch (MemcachedException e) {  
            System.err.println("MemcachedClient operation fail");  
            e.printStackTrace();  
        } catch (TimeoutException e) {  
            System.err.println("MemcachedClient operation timeout");  
            e.printStackTrace();  
        } catch (InterruptedException e) {  
            // ignore  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            memcachedClient.shutdown();  
        } catch (IOException e) {  
            System.err.println("Shutdown MemcachedClient fail");  
            e.printStackTrace();  
        }  
    } 
}
