package a_Socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;


public class Server {  
    private ServerSocket ss = null;  
  
    private List<Socket> list = new LinkedList<Socket>();  
  
    private class ReceiveThread extends Thread {  
        private Socket socket = null;  
  
        public ReceiveThread(Socket socket) {  
            super("ReceiveThread");  
            this.socket = socket;  
        }  
  
        @Override  
        public void run() {  
            InputStream in = null;  
            DataInputStream dis = null;  
            while (true) {  
                try {  
                    sleep(50);  
                } catch (InterruptedException e1) {  
                    e1.printStackTrace();  
                }  
  
                if (socket == null || socket.isClosed()  
                        || !socket.isConnected()) {  
                    continue;  
                }  
                try {  
                    in =  socket.getInputStream();  
                    dis = new DataInputStream(in);  
                    String readUTF = dis.readUTF();  
                    System.out.println("receive client message :" + socket  
                            + " " + readUTF);  
                    send(readUTF+"返回个客户端");  
                } catch (IOException e) {  
                    try {  
                        in.close();  
                        dis.close();  
                    } catch (IOException e1) {  
                        e1.printStackTrace();  
                    }  
                    list.remove(socket);  
                    e.printStackTrace();  
                }  
            }  
  
        }  
    }  
  
    public void send(String msg) throws IOException {  
        for (int i =0 ; i < list.size(); i++) {  
            Socket groupSocket = list.get(i);  
            OutputStream out = groupSocket.getOutputStream();  
            DataOutputStream dout = new DataOutputStream(out);  
            dout.writeUTF(msg);  
            dout.flush();  
            out.flush();  
        }  
    }  
  
    public class AcceptSocketThread extends Thread {  
        public AcceptSocketThread() {  
            super("AcceptSocketThread");  
        }  
  
        public void run() {  
            try {  
                ss = new ServerSocket(7788);  
  
                while (true) {  
                    Socket socket = ss.accept();  
                    list.add(socket);  
                    new ReceiveThread(socket).start();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
  
        }  
    }  
  
    private void start() throws IOException {  
        new AcceptSocketThread().start();  
    }  
  
    public static void main(String[] args) throws Exception {  
        System.out.println("Server start ....");  
        new Server().start();  
    }  
  
}  
