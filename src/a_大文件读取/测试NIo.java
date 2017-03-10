package a_大文件读取;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class 测试NIo {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("IO/NIO " +(double)(TestIo()/TestNIo() ));  
	}
	public static double TestNIo() throws Exception {
		long start = System.currentTimeMillis(); 
		FileInputStream fin = new FileInputStream("test.html");
		//获取通道
		FileChannel channel = fin.getChannel();
		//创建缓冲区
		ByteBuffer bf = ByteBuffer.allocate(1024);
		//需要将数据从通道读到缓冲区中
		
		int bytesRead = channel.read(bf);  
		while (bytesRead != -1) {  
			System.out.println("Read " + bytesRead);  
			bf.flip();  
			while(bf.hasRemaining()){  
				System.out.print( (char)bf.get());  
			}  
			bf.clear();  
			bytesRead = channel.read(bf);  
		}  
           fin.close();
		return System.currentTimeMillis()-start;
	}
	public static double TestIo() throws Exception {
		long start = System.currentTimeMillis(); 
		FileInputStream fin = new FileInputStream("test.html");
		String result = null;
		   int tempbyte;  
           while ((tempbyte = fin.read()) != -1) {  
               System.out.write(tempbyte);  
           }  
           fin.close();
           return System.currentTimeMillis()-start;
	}
}





















