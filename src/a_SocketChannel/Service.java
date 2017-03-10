package a_SocketChannel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import a_SocketNIO.MyRequestObject;
import a_SocketNIO.SerializableUtil;

public class Service {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ServerSocket socket = ssc.socket();
			//绑定要监听的端口
			socket.bind(new InetSocketAddress(8080));
			ssc.configureBlocking(false);
			Selector selector = Selector.open();
			SocketChannel channel = SocketChannel.open();
			//注册感兴趣的连接事件
			ssc.register(selector, SelectionKey.OP_ACCEPT);

			SelectionKey sk = null;
			while (selector.select() > 0) {  
//					Set<SelectionKey>  keys = selector.selectedKeys();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();  
				while(it.hasNext()){
					ServerSocketChannel server = (ServerSocketChannel) it.next().channel();
					 it.remove();  
					 SocketChannel socketChannel = null;  
				            socketChannel = server.accept();  
				            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				            ByteBuffer buffer = ByteBuffer.allocate(1024);  
				              
				                byte[] bytes;  
				                int size = 0;  
				                while ((size = socketChannel.read(buffer)) >= 0) {  
				                    buffer.flip();  
				                    bytes = new byte[size];  
				                    buffer.get(bytes);  
				                    baos.write(bytes);  
				                    buffer.clear();  
				                }  
				                bytes = baos.toByteArray();  
				                Object obj = SerializableUtil.toObject(bytes);  
				              System.out.println("sss"+obj);
				}
//				for(SelectionKey key:keys){
//					if(key.isAcceptable()){
//						ServerSocketChannel server = (ServerSocketChannel) key.channel();
//						SocketChannel sc = server.accept();
//						if(sc==null){
//							continue;
//						}
//						sc.configureBlocking(false);
//						sc.register(selector, SelectionKey.OP_READ);
//					}
//					//6.对于 发生连接的事件
//					else if(key.isConnectable()){
//						SocketChannel sc = (SocketChannel) key.channel();
//						sc.configureBlocking(false);
//						/**
//						 *7.注册感兴趣的io 读事件，通常不直接注册写事件
//						 *	在发送缓存区未满的情况下一直是可写的，
//						 *	因此如果注册了写事件 而又不用写数据，很容易在成cpu 消耗100%的现象 
//						 */
//						sk = sc.register(selector, SelectionKey.OP_READ);
//						sc.finishConnect();
//					}else if(key.isReadable()){
//						//8.有流可读取
//						ByteBuffer bf = ByteBuffer.allocateDirect(1024);
//						SocketChannel sc = (SocketChannel) key.channel();
//						int readBytes = 0;
//						int ret = 0;
//						/**
//						 * 8.读取目前可读的流，sc.read 返回的为成功复制到 bytebuffer 里的字节数
//						 * 	此步为阻塞操作，值可能为0
//						 * */
//						while((ret = sc.read(bf))>0){
//							readBytes += ret;
//						}
//						bf.flip(); 
//						bf.clear();
//					}else if(key.isWritable()){
//						//可写入流
//						//取消对 OP_Write的注册
//						key.interestOps(key.interestOps()&(~SelectionKey.OP_WRITE));
//						SocketChannel sc = (SocketChannel) key.channel();
//						/**
//						 * 9.此步为阻塞操作，知道写入操作系统发送缓存区 或者网络IO 出现异常
//						 * 	  返回的为成功写入的字节数，当操作系统的发送缓存区已满，此处会返回0
//						 * */
//						ByteBuffer bf = ByteBuffer.allocateDirect(1024);
//						int writeSize = sc.write(bf);
//						if(writeSize==0){
//							key.interestOps(key.interestOps()|SelectionKey.OP_WRITE);
//						}
//					}
//					selector.selectedKeys().clear();
//					//对于 要写入的流.可直接调用 channnel.write 来完成.只有写入成功时才要注册OP_WRITE事件
//					ByteBuffer bf = ByteBuffer.allocateDirect(1024);
//					int wSize = channel.write(bf);
//					if(wSize==0){
//						key.interestOps(key.interestOps()|SelectionKey.OP_WRITE);
//					}
//				}
			}
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
