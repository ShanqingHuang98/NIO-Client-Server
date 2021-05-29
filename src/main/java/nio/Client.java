package nio;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        // 创建两个socket，想让它们先后与server连接
        Socket socket = new Socket("localhost", 8001);
        System.out.println("客户端启动");
        PrintStream out;
        BufferedReader in;

        // in和out好像都是socket的ip和端口
        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String content = "我是2号客户端";

        while (true) {
            out.println(content);
            System.out.println("客户端发送：" + content);
            String reply = in.readLine();//这里好像不能正常读取
            System.out.println("服务器回复 :" + reply);
        }
//        socket.close();
    }

}