package ma.enset.blocking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadBlockingServer extends Thread {
    int clientCount;
    public static void main(String[] args) {
        new MultiThreadBlockingServer().start();

    }

    @Override
    public void run() {
        System.out.println("The server is started using port =1234");
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (true){
                Socket socket= serverSocket.accept();
                ++clientCount;
                new Conversation(socket,clientCount).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    class Conversation extends Thread{
        private Socket socket;
        private int clientId;
        public Conversation(Socket socket,int clientId ){
            this.socket=socket;
            this.clientId=clientId;
        }
        @Override
        public void run() {
            try {
                InputStream is=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                OutputStream os=socket.getOutputStream();
                PrintWriter pw=new PrintWriter(os,true);
                String ip=socket.getRemoteSocketAddress().toString();
                System.out.println("New client connection =>"+clientId+ " IP="+ip);
                pw.println("welcome, your ID is => "+clientId);
                String request;
                while ((request=br.readLine())!=null){
                    System.out.println("New Request => IP ="+ip+" Request="+request);
                    String response="Size ="+request.length();
                    pw.println(response);
                }
                socket.close(); // close the connection after each conversation
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
