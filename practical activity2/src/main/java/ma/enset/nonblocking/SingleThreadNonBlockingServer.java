package ma.enset.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SingleThreadNonBlockingServer {
    public static void main(String[] args) throws IOException {
        Selector  selector=Selector.open();
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        //configure with nonblocking mode
        serverSocketChannel.configureBlocking(false);
        //open listenning service(server socket channel listens to the 4444 port and accept any connection any AddressIP), we can add localhost to accept just the connection's local
        serverSocketChannel.bind(new InetSocketAddress("0.0.0.0",4444));
        //possible number of events
        // int validOps=serverSocketChannel.validOps();
        //serverSocketChannel.register(selector, validOps);
        //register ServersocketChannel
        //serverSocketChannel Or SocketChannel will subscribe to the Selector
        //OP_ACCEPT means that serverSocketChannel will be informed if there's any connection request
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println(Thread.currentThread().getName());

        while(true){
            int channelCount=Selector.open().select();
            if(channelCount==0) continue;
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                //client want to connect or begin sending data
                //acceptable concerns ServerSocketChannel not SocketChannel that's why in methods we convert them in ServerSocketChannel
                if(selectionKey.isAcceptable()){
                    handleAccept(selectionKey,selector);
                } else if (selectionKey.isReadable()) {
                    handleReadWrite(selectionKey,selector);
                }
                iterator.remove();
            }
        }
    }

    private static void handleAccept(SelectionKey selectionKey, Selector selector) throws IOException {
        System.out.println(Thread.currentThread().getName());
        ServerSocketChannel serverSocketChannel=(ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        //nonBlocking mode
        socketChannel.configureBlocking(false);
        //when socketChannel is launched, he will wait for requests for events
        socketChannel.register(selector,SelectionKey.OP_READ);
        System.out.println(String.format("New Connection from %s ",socketChannel.getRemoteAddress()));
    }

    private static void handleReadWrite(SelectionKey selectionKey, Selector selector) throws IOException {
        System.out.println(Thread.currentThread().getName());
        //the read events concerns just SocketChannel
        SocketChannel socketChannel=(SocketChannel) selectionKey.channel();
        //read and write the data in Buffer
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        //Read the data that is in the channel and this data will pass it to the buffer
        int dataSize = socketChannel.read(byteBuffer);
        //the disconnection case
        if(dataSize==-1){
            System.out.println(String.format("The client %s has been disconnected ",socketChannel.getRemoteAddress()));
        }
        String request=new String(byteBuffer.array()).trim();
        System.out.println(String.format("New Request %s from %s ",request,socketChannel.getRemoteAddress()));
        String response=new StringBuffer(request).reverse().toString().toUpperCase()+"\n";
        ByteBuffer byteBufferResponse=ByteBuffer.allocate(1024);
        byteBufferResponse.put(response.getBytes());
        //we use flip to convert the buffer from the reading mode to the writting mode to send the response and the reverse is true.
        byteBufferResponse.flip();
        //There is data in the buffer and it will pass to the channel for it to be sent to the client
        socketChannel.write(byteBufferResponse);

    }
}