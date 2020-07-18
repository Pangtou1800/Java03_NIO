package pt.learn.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPTest {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    receiver();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            sender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 发送端
    public static void sender() throws IOException {
        DatagramSocket socket = new DatagramSocket();

        String str = "我是UDP方式发送的导弹";
        byte[] buf = str.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName("127.0.0.1"), 9080);

        socket.send(packet);

        socket.close();
    }

    // 接收端
    public static void receiver() throws IOException {
        DatagramSocket socket = new DatagramSocket(9080);

        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        System.out.println(new String(packet.getData(), 0, packet.getLength()));

        socket.close();
    }
}