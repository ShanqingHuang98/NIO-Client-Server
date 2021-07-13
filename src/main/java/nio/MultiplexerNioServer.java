package nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class MultiplexerNioServer implements Runnable {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private volatile boolean stop = false;


    public MultiplexerNioServer(int port) {
        try {
            serverSocketChannel = ServerSocketChannel.open();//获得一个serverChannel
            selector = Selector.open();////创建选择器  获得一个多路复用器
            serverSocketChannel.configureBlocking(false);//设置为非阻塞模式 如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);//绑定一个端口和等待队列长度
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//把selector注册到channel，关注链接事件
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }


    @Override
    public void run() {
        while (!stop) {
            try {
                //无论是否有读写事件发生，selector每隔1s被唤醒一次。如果一定时间内没有事件，就需要做些其他的事情，就可以使用带超时的
                int client = selector.select(1000);
                System.out.println("1:" + client);
                // 阻塞,只有当至少一个注册的事件发生的时候才会继续.
                // int client = selector.select(); 不设置超时时间为线程阻塞，但是IO上支持多个文件描述符就绪
                if (client == 0) {
                    continue;
                }
                System.out.println("2:" + client);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        //处理事件
                        handle(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                stop();
            }
        }

        if (selector != null) {
            // selector关闭后会自动释放里面管理的资源
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handle(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //连接事件
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 通过ServerSocketChannel的accept创建SocketChannel实例
                // 完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                SocketChannel sc = ssc.accept();//3次握手
                sc.configureBlocking(false);
                sc.register(selector, SelectionKey.OP_READ);//连接建立后关注读事件
            }

            //读事件
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer readbuffer = ByteBuffer.allocate(1024);//写 0 1024  1024
//                ByteBuffer readbuffer = ByteBuffer.allocateDirect(1024); //申请直接内存，也就是堆外内存
                // 读取请求码流，返回读取到的字节数
                int readBytes = socketChannel.read(readbuffer);
                // 读取到字节，对字节进行编解码
                if (readBytes > 0) {
                    // 将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
                    readbuffer.flip();//读写模式反转
                    // 将缓冲区可读字节数组复制到新建的数组中
                    byte[] bytes = new byte[readbuffer.remaining()];
                    readbuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("input is:" + body);
                    // 这里再把body中的name和pass提取出来，然后调用和数据库进行对比的方法（login），并返回登录成功或者失败

                    res(socketChannel, body);
                } else if (readBytes < 0) {
                    // 链路已经关闭 释放资源
                    key.cancel();
                    socketChannel.close();
                } else {
                    // 没有读到字节忽略
                }
            }

        }
    }

    private void res(SocketChannel channel, String response) throws IOException {
        if (response != null && response.length() > 0) {
            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer);
            System.out.println("res end");
        }
    }

    public static class NioServer {
        public static void main(String[] args) {
            int port = 8080;
            MultiplexerNioServer nioServer = new MultiplexerNioServer(port);
            new Thread(nioServer, "nioserver-001").start();
        }
    }
}

