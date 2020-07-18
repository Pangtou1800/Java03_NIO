package pt.learn.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    // TODO: Why won't this work?

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

            File file = new File("1.png");
            FileInputStream fis = null;
            byte[] buf = new byte[1024];
            if (file.exists()) {
                System.out.println("exists");
                fis = new FileInputStream(file);
                int ch = fis.read(buf);
                while (ch != -1) {
                    output.write(buf, 0, ch);
                    ch = fis.read(buf);
                }
            }
            fis.close();

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}