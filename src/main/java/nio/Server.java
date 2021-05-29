package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Server {
    private Selector selector;
    // 虽然老王不让我写在这里但是这些都是write和read都要用的啊。。
    private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
    String str;

    public void start() throws IOException, InterruptedException {
        // 打开服务器套接字通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        // 服务器配置为非阻塞
        ssc.configureBlocking(false);
        // 进行服务的绑定
        ssc.bind(new InetSocketAddress("localhost", 8001));

        // 通过open()方法找到Selector
        selector = Selector.open();
        // 注册到selector，等待连接
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            System.out.println("keys=" + keys.size());
            Thread.sleep(1000);
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    accept(key);
                } else if (key.isReadable()) {
                    read(key);
                } else if (key.isWritable()) {
                    write(key);
                }
                keyIterator.remove();
            }
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        String a = "a";
//        channel.write(ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8)));
        channel.write(ByteBuffer.wrap(a.getBytes(StandardCharsets.UTF_8)));

//        System.out.print("服务器返回的字符串：" + str);
        System.out.print("服务器返回的字符串：" + a);

        channel.register(selector, SelectionKey.OP_READ);
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        this.readBuffer.clear();
        int numRead;
        try {
            numRead = socketChannel.read(this.readBuffer);
        } catch (IOException e) {
            key.cancel();
            socketChannel.close();
            return;
        }
        str = new String(readBuffer.array(), 0, numRead);
        System.out.println("读取客户端发送的字符串:" + str);
        socketChannel.register(selector, SelectionKey.OP_WRITE);
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = ssc.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(selector, SelectionKey.OP_READ);
        System.out.println("连接新的客户端 :" + clientChannel.getRemoteAddress());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("服务开始...");
        new Server().start();
    }
}