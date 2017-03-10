package a_Socket1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Socket1_Service {
	public static void main(String args[]) throws Exception  {
	 ServerSocket ss = null;
		try {
			Scanner inputMsg = new Scanner(System.in);
			String msg = inputMsg.nextLine();
			while(!msg.equals("-1")){
				System.out.println(msg);
				msg = inputMsg.nextLine();
			}
			//1.创建堆本地端口的监听
			ss = new ServerSocket(8080);
			//2.接收客户端的请求 并返回 Socket 对象，以便和客户进行交互
			//交互方式与客户端相同
			Socket socket = ss.accept();
			//接收
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String inMsg = in.readLine();
			if(inMsg.equals("-1")){
				System.out.println("谢谢,下次联系吧");
			}else{
				System.out.println("客户:"+inMsg);
			}
			//向客户发送信息
			PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
			out.println("拨打110");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}












