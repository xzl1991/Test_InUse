package a_大文件读取;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestFileScan {
	
	static int count1=0;
	static int count2=0;
	static ExecutorService  es=Executors.newCachedThreadPool();
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		
		String file="E:/WorkSoft";
		long start=System.nanoTime();
		test1(file);
		System.out.println(count1);
		System.out.println(System.nanoTime()-start);
		
		start=System.nanoTime();
		test2(file);
		System.out.println(count2);
		System.out.println(System.nanoTime()-start);
		
		
	}
	
	public static void test1(String file){
		
		File f=new File(file);
		for(File tmp : f.listFiles()){
			if(tmp.isDirectory()){
			//	System.out.println(tmp.getPath());
				test1(tmp.getPath());
			}
			count1 ++;
		}
	}
	
	public static void   test2(String file) throws InterruptedException, ExecutionException{
		File f=new File( file);
		for(final File tmp : f.listFiles()){
			if(tmp.isDirectory()){
			  es.submit(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
				    	test2(tmp.getPath());
					return null;
				}
			}).get();
			  
			 
			}
			synchronized (TestFileScan.class) {
				count2++;
			}
			
		}

		
		
	}

}
