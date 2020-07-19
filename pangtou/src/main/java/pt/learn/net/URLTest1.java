package pt.learn.net;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class URLTest1 {

    // Why won't this work?
    // => Cause there's gotta be a response-header

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

        testURL();

    }

    public static void server() throws IOException {
        ServerSocket sSocket = new ServerSocket(8080);
        System.out.println("Server started");
        Socket socket = sSocket.accept();
        System.out.println("Server accepted");
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        byte[] buf = new byte[100];
        int len;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while ((len = is.read(buf)) != -1) {
            baos.write(buf, 0, len);
            System.out.println("Server receiving");
            if (is.available() <= 0) {
                break;
            }
        }
        System.out.println("Server received");

        System.out.println(baos.toString());

        baos.close();

        // Http header first
        os.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());

        FileInputStream fis = new FileInputStream("1.png");
        while ((len = fis.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
        fis.close();
        os.close();

        is.close();
        socket.close();
        sSocket.close();
    }

    public static void testURL() {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/examples/beauty.jpg");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        URLConnection conn = null;
        HttpURLConnection hConn = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            conn = url.openConnection();
            hConn = (HttpURLConnection) conn;
            hConn.connect();
            is = hConn.getInputStream();

            fos = new FileOutputStream("2.png");

            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
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
            if (hConn != null) {
                hConn.disconnect();
            }
        }
    }
}