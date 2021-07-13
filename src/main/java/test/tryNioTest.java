package test;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;


public class tryNioTest {
    @Test
    public void client() throws IOException {
        // get channel
        SocketChannel sChannel1 = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        // 切换到非阻塞
        sChannel1.configureBlocking(false);
        // allocate buffer
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //send data to server
        buf.put(new Date().toString().getBytes(StandardCharsets.UTF_8));
        buf.flip();
        sChannel1.write(buf);
        buf.clear();
        // close channel
        sChannel1.close();
    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel ssChannel1 = ServerSocketChannel.open();
        //flip to nbloking
        ssChannel1.configureBlocking(false);
        //bind
        ssChannel1.bind(new InetSocketAddress(9898));
        // get selector
        Selector selector = Selector.open();
        //register channel to selector
        //ops listen to  accept
        ssChannel1.register(selector, SelectionKey.OP_ACCEPT);
        // get "ready" event of selector
        while (selector.select() > 0) {
            // get current registered selection key(read/write/accept...)
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            //iterator
            while (it.hasNext()) {
                //get event in  "ready" status
                SelectionKey sk = it.next();
                // judge the concrete event
                if (sk.isAcceptable()) {
                    // if ready ,get connction of client
                    SocketChannel sChannel = ssChannel1.accept();
                    // flip to nbloking of server
                    sChannel.configureBlocking(false);
                    //register to key
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    // get "read" ready channel in selector
                    SocketChannel sChannel = (SocketChannel) sk.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    int len = 0;
                    while ((len = sChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }
                it.remove();
            }
        }
    }

}