package pt.learn.net;

import java.io.ByteArrayOutputStream;
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
 * 例3:客户端发送文件给服务端，服务端将文件保存在本地。之后服务端返回“发送成功！”给客户端<br>
 */
public class TCPTest3 {

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
        InputStream is = null;
        try {
            socket = new Socket(InetAddress.getByName("127.0.0.1"), 9898);

            os = socket.getOutputStream();

            fis = new FileInputStream("1.png");

            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }

            // 显式指示socket发送完成，避免服务端读取的阻塞
            socket.shutdownOutput();
            System.out.println("图片发送完成！");

            // 从服务端接收反馈，并显示到控制台上
            is = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            System.out.println(baos.toString());
            baos.close();

            fis.close();
            os.close();
            socket.close();
            is.close();
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
            if (is != null) {
                try {
                    is.close();
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
        OutputStream os = null;
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

            System.out.println("图片接收完成！");

            // 服务端给客户端反馈
            os = socket.getOutputStream();
            os.write("发送成功！".getBytes());

            fos.close();
            is.close();
            socket.close();
            sSocket.close();
            os.close();

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
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}