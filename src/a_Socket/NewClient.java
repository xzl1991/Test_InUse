package a_Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;
public class NewClient {
	public static void main(String args[]) throws Exception  {
		Socket socket = new Socket("127.0.0.1",8080);
		String message = null;
		Scanner sca = new Scanner(System.in);
		System.out.print("输入发送的信息:");
		message = sca.nextLine();
		while(!message.equals("-1")){
			send(socket,message);
				/*****获取服务器信息******/
				String getString = null;
	                try {  
	                    InputStream in = (InputStream) socket.getInputStream();  
	                    DataInputStream datain = new DataInputStream(in);  
	                    String readUTF = datain.readUTF();  
	                    if(!readUTF.equals("-1")){
	                    	System.out.println("收到的服务器信息: " + readUTF); 
	                    	System.out.print("输入发送的信息:");
	                		message = sca.nextLine();
	                		if(!message.equals("-1")){
	                			send(socket,message);
	                		}
	                    }else{
	                    	message = "-1";
	                    }
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	            } 
		}
			
	}
	public static void send(Socket socket,String message) throws Exception{
		//给服务器发信息
		OutputStream out = socket.getOutputStream();
		 DataOutputStream dout = new DataOutputStream(out); 
		 dout.writeUTF(message);  
         // scaner.close();
		 out.flush();
         dout.flush();  
	}
}

















