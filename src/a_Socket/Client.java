package a_Socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;


public class Client {  
    private Socket socket = null;  
  
    private class ReceiveThread extends Thread {  
        @Override  
        public void run() {  
            while (true) {  
                try {  
                    InputStream in = (InputStream) socket.getInputStream();  
                    DataInputStream datain = new DataInputStream(in);  
                    String readUTF = datain.readUTF();  
                    System.out.println("Server Send Message: " + readUTF);  
  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
  
    public void start() throws IOException {  
        socket = new Socket(InetAddress.getByName("127.0.0.1"), 7788);  
  
        new ReceiveThread().start();  
        System.out.println("Client start ....\nPlease input message : ");  
        while (true) {  
            Scanner scaner = new Scanner(System.in);  
            String next = scaner.next();  
            OutputStream out = socket.getOutputStream();  
            DataOutputStream dout = new DataOutputStream(out);  
            dout.writeUTF(next);  
            // scaner.close();  
            dout.flush();  
        }  
    }  
  
    public static void main(String[] args) throws IOException {  
        System.out.println("client ...");  
        Client client = new Client();  
        client.start();  
    }  
  
} 
