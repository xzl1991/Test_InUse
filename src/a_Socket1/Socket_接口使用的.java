package a_Socket1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.apache.catalina.connector.InputBuffer;



public class Socket_接口使用的 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String s = "王八蛋反党法轮功试试";
		System.out.println(s);
		test(s);
	}
	public static boolean test(String content) throws IOException{
		long start =  System.currentTimeMillis();
//		content = content.toLowerCase();
//		content = content.replace("\r", "");
////		content = content.replace("\n", "");
//		content = content.replace("\0", "");
//		content = content.replace(" ", "");
		String post = null;
		Socket socket = null;
		OutputStream out=null;
		PrintWriter pw=null;
		InputStream in=null;
		BufferedReader br=null;
		try {
			post = "CheckPost~全国~ask~" +content+"\n"+"aaaaa法轮功bbbbb"+"\n"+"王八蛋反党法轮功试试"+"\0";
//			post = "CheckPost~全国~ask~" +URLEncoder.encode(content, "gbk")+"\n"+URLEncoder.encode(content, "gbk")+ "\0";
			System.out.println(post);
			socket = new Socket("218.30.110.47", 3637);	
			socket.setKeepAlive(true);
			socket.setSoTimeout(500);
			// 给服务器发送信息
			// 给服务器发信息
			out = socket.getOutputStream();
			pw=new PrintWriter(out);
			// 接收返回信息
			in = (InputStream) socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			pw.write(post);
            pw.flush(); 
			String receive = "";	
			char[] ch=new char[1000];
			br.read(ch);
			receive=new String(ch);
			System.out.println("接收内容："+receive);
         	String[] receiveDatas = receive.split("~");
            if (receiveDatas.length == 3)
            {
                if (receiveDatas[0] == "1")
                {
                    return true;
                }
            }
            return false;
         } catch (IOException e) {  
             // TODO Auto-generated catch block  
             e.printStackTrace();  
//         }  
         }finally{
        	 pw.close();
        	 out.close();
        	 br.close();
        	 in.close();
        	 socket.close();
        	 System.out.println("关键词验证时间:"+(System.currentTimeMillis()-start));
         } 
		return true;
	
	}
}
