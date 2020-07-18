package pt.learn.net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 实现TCP的网络编程<br>
 * 例2:客户端发送文件给服务端，服务端将文件保存在本地。
 */
public class TCPTest2 {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                server();
            }
        }).start();

        client();
    }

    public static void client() {
        Socket socket = null;
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9898);

            os = socket.getOutputStream();

            fis = new FileInputStream("1.png");

            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }

            fis.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
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
        FileOutputStream fos = null;
        try {
            sSocket = new ServerSocket(9898);

            socket = sSocket.accept();

            is = socket.getInputStream();

            fos = new FileOutputStream("2.png");

            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }

            fos.close();
            is.close();
            socket.close();
            sSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sSocket != null) {
                try {
                    sSocket.close();
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
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}