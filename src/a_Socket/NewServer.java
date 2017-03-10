package a_Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class NewServer {
	public static void main(String args[]) throws Exception  {
		ServerSocket server = null;//服务
		Socket socket = null;//socket
			server = new ServerSocket(8080);//开启服务端口
			socket = server.accept();//服务获取端口
			/*****获取客户传送数据******/
//			InputStream in = (InputStream) socket.getInputStream();  
//			DataInputStream datain = new DataInputStream(in);  
			String readUTF = getMsg(socket);
			
			Scanner sca = new Scanner(System.in);
			System.out.print("输入发送的信息:");
			String message = null;
//			while (!readUTF.equals("-1")) {  
//                try {  
//                	System.out.println("收到的信息: " + readUTF); 
//                	System.out.print("你要发的信息，-1结束:");
//                	message = sca.nextLine();
//                	if(!message.equals("-1")){
//                		 sentback(socket, message+"&......这是我给你返回的信息....");
//                		 readUTF = getMsg(socket);
//                	}else{
//                		readUTF = "-1";
//                	}
//                } catch (IOException e) {  
//                    // TODO Auto-generated catch block  
//                    e.printStackTrace();  
//                }  
//            } 
				//处理后返回
			message = sca.nextLine();
			while (!message.equals("-1")) { 
				sentback(socket, message+"&......这是我给你返回的信息...."); 
                try {  
                	 InputStream in = (InputStream) socket.getInputStream();  
	                    DataInputStream datain = new DataInputStream(in);  
	                   readUTF = datain.readUTF();  
                	 if(!readUTF.equals("-1")){
                		 System.out.println("收到的信息: " + readUTF); 
	                    	System.out.print("输入发送的信息:");
	                		message = sca.nextLine();
	                		if(!message.equals("-1")){
	                			sentback(socket,message);
	                		}
	                    }else{
	                    	message = "-1";
	                    }
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            } 
			 }
	public static void sentback(Socket socket,String back) throws IOException{
		OutputStream out = socket.getOutputStream();
		 DataOutputStream dout = new DataOutputStream(out); 
		 dout.writeUTF(back);  
        // scaner.close();  
        out.flush();
        dout.flush();  
	}
	public static String getMsg(Socket socket) throws Exception{
		InputStream in = (InputStream) socket.getInputStream();  
		DataInputStream datain = new DataInputStream(in);  
		String readUTF = datain.readUTF();
		return readUTF;
	}
}

















