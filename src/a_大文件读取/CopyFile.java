package a_大文件读取;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class CopyFile {
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		System.out.println("请输入要进行复制的文件");
		// Scanner sc=new Scanner(System.in);
		String src = new String("D:/WorkSoft.rar");
		File outFile = new File(src); // 这是要进行数据流输入的文件
		File inFile = new File(outFile.getParent(), "_Copy" + outFile.getName()); // 这是要进行数据流输出的文件
		RandomAccessFile readF = new RandomAccessFile(outFile, "r");
		RandomAccessFile writeF = new RandomAccessFile(inFile, "rw");
		byte[] tem = new byte[513];
		while (readF.getFilePointer() != readF.length()) { // 文件复制操作
			readF.read(tem);
			writeF.write(tem);
		}
		readF.close();
		writeF.close();
		System.out.println("复制结束");
	     double ll = System.currentTimeMillis()-start;
	       System.out.println("复制结束...总时间:"+(测试Io.testRWbyIO()/ll));
	}
}
