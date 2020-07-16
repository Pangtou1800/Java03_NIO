package pt.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class TestNonBlockingNIO {

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
        // 1.获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 2.切换为非阻塞模式
        sChannel.configureBlocking(false);

        // 3.分配缓缓从去
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 4.发送数据给服务端
        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            String str = scan.next();
            buf.put((new Date().toString() + " " + str).getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        scan.close();

        // 5.关闭通道
        sChannel.close();
    }

    public static void server() throws IOException {
        // 1.获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 2.切换为非阻塞模式
        ssChannel.configureBlocking(false);

        // 3.绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        // 4.获取选择器
        Selector selector = Selector.open();

        // 5.将通道注册到选择器上，并且制定监听事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 6.轮询式地获取选择器上已经准备就绪的事件
        while (selector.select() > 0) {
            // 7.获取当前选择器中所有注册的“选择键（已就绪的监听事件）”
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            // 8.迭代获取准备就绪的事件
            while (it.hasNext()) {
                SelectionKey sKey = it.next();
                // 9.判断具体是什么事件准备就绪
                if (sKey.isAcceptable()) {
                    // 10.若接收就绪，获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();
                    // 11.切换非阻塞模式
                    sChannel.configureBlocking(false);
                    // 12.将该通道注册到选择器上
                    sChannel.register(selector, SelectionKey.OP_READ);
                } else if (sKey.isReadable()) {
                    // 13.获取当前选择器上“读就绪”状态的通道
                    SocketChannel sChannel = (SocketChannel) sKey.channel();
                    // 14.读取数据
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    int len = 0;
                    while ((len = sChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }

                // 15.取消选择键
                it.remove();
            }
        }

    }
}
