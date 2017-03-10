package a_SocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class Client {
	public static void main(String[] args){
		try {
			SocketChannel channel = SocketChannel.open();
			//1.设置为非阻塞模式
			channel.configureBlocking(false);
			//2.对于非阻塞模式，立刻返回false 表示连接正在建立中
			SocketAddress address = new InetSocketAddress("127.0.0.1",8080);
			channel.connect(address);
			Selector selector = Selector.open();
			//3.向Channel 注册selector 及感兴趣的连接
			channel.register(selector, SelectionKey.OP_CONNECT);
			/**4.阻塞至 有感兴趣的io 发生，或等到超时，如果想一直等至 有感兴趣的事发生，
			 * 	可调用无参数的select 方法，如果希望不阻塞直接返回当前是否有感兴趣的事发生，可调用 selectNow 方法
			 */
			int nKeys = selector.select(2000); 
			//5如果 nekys 大于0 说明有感兴趣的是发生
			SelectionKey sk = null;
			if(nKeys>0){
				Set<SelectionKey>  keys = selector.selectedKeys();
				for(SelectionKey key:keys){
					//6.对于 发生连接的事件
					if(key.isConnectable()){
						SocketChannel sc = (SocketChannel) key.channel();
						sc.configureBlocking(false);
						/**
						 *7.注册感兴趣的io 读事件，通常不直接注册写事件
						 *	在发送缓存区未满的情况下一直是可写的，
						 *	因此如果注册了写事件 而又不用写数据，很容易在成cpu 消耗100%的现象 
						 */
						sk = sc.register(selector, SelectionKey.OP_READ);
						sc.finishConnect();
					}else if(key.isReadable()){
						//8.有流可读取
						ByteBuffer bf = ByteBuffer.allocateDirect(1024);
						SocketChannel sc = (SocketChannel) key.channel();
						int readBytes = 0;
						int ret = 0;
						/**
						 * 8.读取目前可读的流，sc.read 返回的为成功复制到 bytebuffer 里的字节数
						 * 	此步为阻塞操作，值可能为0
						 * */
						while((ret = sc.read(bf))>0){
							readBytes += ret;
						}
						bf.flip(); 
						bf.clear();
					}else if(key.isWritable()){
						//可写入流
						//取消对 OP_Write的注册
						key.interestOps(key.interestOps()&(~SelectionKey.OP_WRITE));
						SocketChannel sc = (SocketChannel) key.channel();
						/**
						 * 9.此步为阻塞操作，知道写入操作系统发送缓存区 或者网络IO 出现异常
						 * 	  返回的为成功写入的字节数，当操作系统的发送缓存区已满，此处会返回0
						 * */
						ByteBuffer bf = ByteBuffer.allocateDirect(1024);
						int writeSize = sc.write(bf);
						if(writeSize==0){
							key.interestOps(key.interestOps()|SelectionKey.OP_WRITE);
						}
					}
					selector.selectedKeys().clear();
					//对于 要写入的流.可直接调用 channnel.write 来完成.只有写入成功时才要注册OP_WRITE事件
					ByteBuffer bf = ByteBuffer.allocateDirect(1024);
					int wSize = channel.write(bf);
					if(wSize==0){
						key.interestOps(key.interestOps()|SelectionKey.OP_WRITE);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}




























