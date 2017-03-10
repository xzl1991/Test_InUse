package a_大文件读取;

import java.io.File;
import java.io.IOException;  
import java.io.RandomAccessFile;  
import java.util.concurrent.CountDownLatch;  
  
/** 
 * 这个线程用来读取文件，当获取到指定关键字时，在指定的对象加1 
 * @author 刘峰管理2 
 * 
 */  
public class ReadThreadCopy extends Thread{  
  
    //定义字节数组（取水的竹筒）的长度    
    private final int BUFF_LEN = 1024*1024;    
    //定义读取的起始点    
    private long start;    
    //定义读取的结束点    
    private long end;   
    //将读取到的字节输出到raf中  randomAccessFile可以理解为文件流，即文件中提取指定的一部分的包装对象  
    private RandomAccessFile readF;    
    private RandomAccessFile writeF;    
    //线程中需要指定的关键字  
    //此线程读到关键字的次数  
    private int curCount = 0;  
    /** 
     * jdk1.5开始加入的类，是个多线程辅助类 
     * 用于多线程开始前统一执行操作或者多线程执行完成后调用主线程执行相应操作的类 
     */  
    private CountDownLatch doneSignal;  
    public ReadThreadCopy(long start, long end,RandomAccessFile writeF,RandomAccessFile readF){  
        this.start = start;  
        this.end = end;  
        this.readF  = readF;  
        this.writeF  = writeF;  
    }  
    public void run(){  
        try {  
            //本线程负责读取文件的大小    
            long contentLen = end - start;    
            //定义最多需要读取几次就可以完成本线程的读取    
            long times = contentLen / BUFF_LEN+1;    
            System.out.println(this.toString() + " 需要读的次数："+times);  
            byte[] buff = new byte[BUFF_LEN];  
            readF.seek(start);  
            writeF.seek(start);  
            while(start<end)  
            {  
                int len=0;  
                if(start+BUFF_LEN<end)//如果可以装满一个缓冲区  
                {  
                    len=readF.read(buff);  
                     
                }else  
                {  
                    len=readF.read(buff,0,(int)(end-start));  
                }  
                writeF.write(buff,0,len);   
                start+=len;  
            } 
//            readF.close();
//            writeF.c
//            doneSignal.countDown();//current thread finished! noted by latch object!  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
  
    public long getStart() {  
        return start;  
    }  
  
    public void setStart(long start) {  
        this.start = start;  
    }  
  
    public long getEnd() {  
        return end;  
    }  
  
    public void setEnd(long end) {  
        this.end = end;  
    }  
  
    public RandomAccessFile getReadF() {
		return readF;
	}
	public void setReadF(RandomAccessFile readF) {
		this.readF = readF;
	}
	public RandomAccessFile getWriteF() {
		return writeF;
	}
	public void setWriteF(RandomAccessFile writeF) {
		this.writeF = writeF;
	}
	public RandomAccessFile getRaf() {  
        return readF;  
    }  
  
    public void setRaf(RandomAccessFile readF) {  
        this.readF = readF;  
    }  
      
    public int getCountByKeywords(String statement,String key){  
        return statement.split(key).length-1;  
    }  
  
    public int getCurCount() {  
        return curCount;  
    }  
  
    public void setCurCount(int curCount) {  
        this.curCount = curCount;  
    }  
  
    public CountDownLatch getDoneSignal() {  
        return doneSignal;  
    }  
  
    public void setDoneSignal(CountDownLatch doneSignal) {  
        this.doneSignal = doneSignal;  
    }  
}  
