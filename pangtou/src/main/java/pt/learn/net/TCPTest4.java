package pt.learn.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPTest4 {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void client() throws IOException {
        Socket socket = new Socket(InetAddress.getByName("localhost"), 9080);
        OutputStream os = socket.getOutputStream();

        os.write("abcDEf".getBytes());
        socket.shutdownOutput();

        InputStream is = socket.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }

        System.out.println(baos.toString());

        os.close();
        is.close();
        baos.close();
        socket.close();
    }

    public static void server() throws IOException {
        ServerSocket sSocket = new ServerSocket(9080);
        Socket socket = sSocket.accept();
        InputStream is = socket.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }

        OutputStream os = socket.getOutputStream();
        os.write((baos.toString().toUpperCase() + " from server").getBytes());

        os.close();
        baos.close();
        is.close();
        socket.close();
        sSocket.close();
    }

}