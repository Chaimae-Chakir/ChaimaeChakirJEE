package ma.enset.blocking;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;



public class MyTelnetClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 1234);
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            //Separating the part that listens for responses from the part that sends requests into 2 threads.
            // to avoid the risk of the interface blocking the receipt of a message

            // Thread that listens for server responses
            new Thread(() -> {
                try {
                    String request;
                    while ((request = br.readLine()) != null) {
                        String response;
                        while ((response = br.readLine()) != null) {
                            System.out.println(response);
                        }
                    }
                } catch (SocketException e) {
                    System.out.println("Server connection closed.");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();


            // Thread to read user input
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String request = scanner.nextLine();
                    pw.println(request);
                }
            }).start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
