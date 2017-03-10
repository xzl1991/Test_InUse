package a_大文件读取;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

public class RandomTest {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args)  {
		RandomAccessFile rs = null;
		try {
			String result =  null;
			rs = new RandomAccessFile("E:/test.txt", "rw");
			rs.seek(0);
			 byte[] buff = new byte[3]; 
			 rs.read(buff);
			 result = new String(buff,"utf8");  
			System.out.println(result);
			rs.seek(3);
			System.out.println(rs.read(new byte[3]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
