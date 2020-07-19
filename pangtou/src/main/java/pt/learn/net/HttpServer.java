package pt.learn.net;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    // Why won't this work?
    // => Cause there's gotta be a response-header

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        int port = 8080;

        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        Socket socket = null;
        OutputStream output = null;
        try {
            socket = serverSocket.accept();
            output = socket.getOutputStream();

            // Http header first
            output.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());

            byte[] buf = new byte[1024];
            FileInputStream fis = new FileInputStream("1.png");

            int len = -1;
            while ((len = fis.read(buf)) != -1) {
                output.write(buf, 0, len);
            }
            fis.close();
            output.close();

            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}