package a_Socket1;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * udp/ip 是无法连接的，如果希望双向通信，必须启动一个监听接口
		 * */
		try {
			DatagramSocket serverSocket = new DatagramSocket(8080);
			byte[] buff = new byte[65507];
			
			DatagramPacket receivePacket =  new DatagramPacket(buff,buff.length);
			DatagramSocket socket = new DatagramSocket();
			byte[] datas = new byte[6555];
			DatagramPacket packet = new DatagramPacket(datas, datas.length,null, 8080);
			
			socket.send(packet);
			socket.setSoTimeout(2000);
			
			serverSocket.receive(receivePacket);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


























