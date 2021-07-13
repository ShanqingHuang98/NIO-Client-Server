package nio;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Client2 {

    public static void main(String[] args) throws IOException {
        // 创建两个socket，想让它们先后与server连接
        Socket socket = new Socket("localhost", 8080);
        System.out.println("客户端启动");
        PrintStream out;
        BufferedReader in;

        // in和out好像都是socket的ip和端口
        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String uname = "123";
        String upass = "321";
        User user = new User(uname, upass);

        while (true) {
            out.println(user);
            System.out.println("客户端发送：" + user);
            String reply = in.readLine();
            System.out.println("服务器回复 :" + reply);
        }
//        socket.close();
    }

}