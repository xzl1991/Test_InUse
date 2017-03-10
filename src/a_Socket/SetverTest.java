package a_Socket;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import a_生成xml.StringUtils;
public class SetverTest {

	public static void main(String args[]) throws Exception {
		ServerSocket server = null;
		try {
			server = new ServerSocket(3036);
			System.out.println(".....");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Socket socket=null;
		try {
			socket = server.accept();
			String line;
			PrintWriter os=new PrintWriter(socket.getOutputStream());
			BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
//			System.out.println("Client:"+is.readLine());
			BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String getfromClient = is.readLine();
			System.out.println("Client解码前:"+getfromClient);
			getfromClient = StringUtils.URLDecoder_GBK(getfromClient);
			System.out.println("Client解码后:"+getfromClient);
			line=sin.readLine();
			
			
			
			
			while(!line.equals("bye")){
				os.println(line+"ssss");
				os.flush();
//				System.out.println("Server:"+line);
				//在系统标准输出上打印读入的字符串
//				System.out.println("Client:"+is.readLine());
				//从Client读入一字符串，并打印到标准输出上
				line=sin.readLine();
				 send(line+"返回个客户端");  
			}
			os.close(); //关闭Socket输出流
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	private static List<Socket> list = new LinkedList<Socket>();  
	 public static void send(String msg) throws IOException {  
	        for (int i =0 ; i < list.size(); i++) {  
	            Socket groupSocket = list.get(i);  
	            OutputStream out = groupSocket.getOutputStream();  
	            DataOutputStream dout = new DataOutputStream(out);  
	            dout.writeUTF(msg);  
	            dout.flush();  
	            out.flush();  
	        }  
	    } 
}
