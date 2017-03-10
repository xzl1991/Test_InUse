package a_大文件读取;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class 测试Io {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//           System.out.println("测试IO/NIO："+(testRWbyIO()/testRWbyNIO1()));
		long start = System.currentTimeMillis(); 
           // TODO Auto-generated method stub  
           final int DOWN_THREAD_NUM = 10;//起10个线程去读取指定文件  
           final String OUT_FILE_NAME = "D:/WorkSoft.rar";//"E:/MyDownloads/QQDownLoad\\倚天屠龙记.txt";  
            //jdk1.5线程辅助类，让主线程等待所有子线程执行完毕后使用的类，  
           //另外一个解决方案：自己写定时器，个人建议用这个类  
           RandomAccessFile[] outArr = new RandomAccessFile[DOWN_THREAD_NUM];  
           File inFile=new File("D:/A_Copy.rar");     //这是要进行数据流输出的文件
           RandomAccessFile writeF= new RandomAccessFile(inFile,"rw"); 
           try{  
               long length = new File(OUT_FILE_NAME).length();  
               System.out.println("文件总长度："+length+"字节");  
               //每线程应该读取的字节数    
               long numPerThred = length / DOWN_THREAD_NUM;    
               System.out.println("每个线程读取的字节数："+numPerThred+"字节");  
             //整个文件整除后剩下的余数    
               long left = length % DOWN_THREAD_NUM;  
               for (int i = 0; i < DOWN_THREAD_NUM; i++) {    
                   //为每个线程打开一个输入流、一个RandomAccessFile对象，    
                   //让每个线程分别负责读取文件的不同部分  
                   outArr[i] = new RandomAccessFile(OUT_FILE_NAME, "rw");  
                   if (i != 0) {    
//                       isArr[i] = new FileInputStream("d:/勇敢的心.rmvb");    
                   }    
                   if (i == DOWN_THREAD_NUM - 1) {    
//                       //最后一个线程读取指定numPerThred+left个字节    
//                     System.out.println("第"+i+"个线程读取从"+i * numPerThred+"到"+((i + 1) * numPerThred+ left)+"的位置");  
                       new ReadThreadCopy(i * numPerThred, (i + 1) * numPerThred    
                               + left,writeF, outArr[i]).start();    
                   } else {    
                       //每个线程负责读取一定的numPerThred个字节    
//                     System.out.println("第"+i+"个线程读取从"+i * numPerThred+"到"+((i + 1) * numPerThred)+"的位置");  
                       new ReadThreadCopy(i * numPerThred, (i + 1) * numPerThred,    
                    		   writeF,outArr[i]).start();    
                   }    
               }  
           }catch(Exception e){  
               e.printStackTrace();  
           }  
//         finally{  
//               
//         }  
           //确认所有线程任务完成，开始执行主线程的操作  
           try {  
//               doneSignal.await();  
           } catch (Exception e) {  
               // TODO Auto-generated catch block  
               e.printStackTrace();  
           }  
           //这里需要做个判断，所有做read工作线程全部执行完。  
           double ll = System.currentTimeMillis()-start;
       System.out.println("复制结束...总时间:"+(testRWbyIO()/ll));
	}
	 public static float testRWbyIO() throws IOException { 
		 long start = System.currentTimeMillis(); 
	        FileInputStream fins= null;  
	        FileOutputStream fouts = null;  
	        try {  
	          
	        	File f = new File("D:/WorkSoft1.rar");  
	            fins = new FileInputStream(f);  
	              
	            File outF = new File("D:/WorkSoft11.rar");   
	            fouts = new FileOutputStream(outF);  
	              
	            byte[] bytes = new byte[1024*1024];  
	            while(fins.read(bytes)>0) {  
	                fouts.write(bytes);  
	            }  
	        }finally {  
	            if (fins != null) {  
	                fins.close();  
	            }  
	            if (fouts != null) {  
	                fouts.close();  
	            }  
	        } 
	        System.out.println("IO总时间:"+(System.currentTimeMillis()-start));
			return System.currentTimeMillis()-start;
	    }  
	      
	    @Test  
	    public   void testRWbyNIO1() throws IOException { 
	    	long start = System.currentTimeMillis();
	        FileInputStream fins= null;  
	        FileOutputStream fouts = null;  
	        try {  
	          
	            File f = new File("D:/WorkSoft.rar");  
	            System.out.println(f.length()+"....");
	            fins = new FileInputStream(f);  
	            FileChannel readChannel = fins.getChannel();  
	            File outF = new File("D:/WorkSoft2.rar");  
	            fouts = new FileOutputStream(outF);  
	            FileChannel writeChannel = fouts.getChannel();  
	            ByteBuffer bb = ByteBuffer.allocate(1024*1024);  
	            while(true) {  
	                bb.clear();  
	                int i = readChannel.read(bb);  
	                if (i ==-1) {  
	                    break;  
	                }  
	                  
	                bb.flip();  
	                writeChannel.write(bb);  
	            }  
	        }finally {  
	            if (fins != null) {  
	                fins.close();  
	            }  
	            if (fouts != null) {  
	                fouts.close();  
	            }  
	        }  
	        System.out.println("NIO总时间:"+(System.currentTimeMillis()-start));
//	        return System.currentTimeMillis()-start;
	    }  
}
