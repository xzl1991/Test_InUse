package a_Socket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import a_生成xml.StringUtils;
public class ClientTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		Socket socket=new Socket();
		try {
			String t = "实得分";
			System.out.println(StringUtils.URLEncode_GBK(t));
			System.out.println(StringUtils.URLEncode_UTF8(t));
			Socket socket = new Socket("218.30.110.47", 3637);
//			BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
			//由系统标准输入设备构造BufferedReader对象
			PrintWriter os = new  PrintWriter(socket.getOutputStream());
//			BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String readline;
			socket.setSoTimeout(2000);
//			readline=sin.readLine(); //从系统标准输入读入一字符串
			readline = checkContent("啊啊发了的克里斯1111");
			readline = StringUtils.URLEncode_GBK(readline);
			os.println(readline);
			os.flush();
			
			
			 InputStream in = (InputStream) socket.getInputStream();  
             DataInputStream datain = new DataInputStream(in);  
             String readUTF = datain.readUTF();  
             System.out.println("Server Send Message: " + readUTF);  
//			while(!readline.equals("bye")){
//				os.println(readline+"22222");
//				os.flush();
//				System.out.println("Client...:"+readline);
//				//在系统标准输出上打印读入的字符串
//				System.out.println("Server:"+is.readLine());
//
//				readline=sin.readLine(); //从系统标准输入读入一字符串
//			}
			socket.close(); //关闭Socket
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static String checkContent(String content){
		content = content.toLowerCase();
		content = content.replace("\r", "");
		content = content.replace("\n", "");
		content = content.replace("\0", "");
		content = content.replace(" ", "");
		String post = "CheckPost~全国~ask~"+content+"\0";
//		System.out.println(post);
		return post;
	}
}
