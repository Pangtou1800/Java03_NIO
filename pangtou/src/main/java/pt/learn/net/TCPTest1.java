package pt.learn.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现TCP的网络编程<br>
 * 例1:客户端发送信息给服务端，服务端将数据显示在控制台上。
 */
public class TCPTest1 {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                server();
            }
        }, "[Server]").start();

        Thread.currentThread().setName("[Client]");
        client();
    }

    public static void client() {
        Socket socket = null;
        OutputStream os = null;
        try {
            InetAddress inet = InetAddress.getByName("127.0.0.1");
            socket = new Socket(inet, 8899);

            os = socket.getOutputStream();
            os.write("你好，我是客户端MM".getBytes());
            System.out.println(
                    Thread.currentThread().getName() + "From: " + socket.getInetAddress() + ":" + socket.getPort());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static void server() {
        ServerSocket sSocket = null;
        Socket socket = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            // 1.创建服务器端的ServerSocket，指明自己的端口号
            sSocket = new ServerSocket(8899);
            // 2.调用accept()接收来自客户端的socket
            socket = sSocket.accept();
            // 3.获取输入流
            is = socket.getInputStream();

            // 4.读取输入流中的数据
            baos = new ByteArrayOutputStream();
            byte[] buf = new byte[5];
            int len = -1;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            System.out.print(Thread.currentThread().getName() + "Received: " + baos.toString());
            System.out.println(" From: " + socket.getInetAddress() + ":" + socket.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 5.关闭资源
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (sSocket != null) {
                try {
                    sSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}