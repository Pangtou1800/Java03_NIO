package pt.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 一、使用NIO完成网络通信的三个核心：<br>
 * 1.通道（Channel）：负责连接<br>
 * java.nio.channels.Channel接口<br>
 * |--SelectableChannel抽象类<br>
 * |--SocketChannel<br>
 * |--ServerSocketChannel<br>
 * |--DatagramChannel<br>
 * 
 * |--Pipe.SinkChannel<br>
 * |--Pipe.SourceChannel<br>
 * ※FileChannel不能切换为非阻塞模式<br>
 * 2.缓冲区（Buffer）：负责数据的存储<br>
 * 3.选择器（Selector）：是SelectChannel的多路复用器。用于监控SelectableChannel的IO状况<br>
 */
public class TestBlockingNIO {

    // 客户端
    public static void client() throws IOException {
        // 1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 2.分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 3.从本地读取文件并发送到服务端
        FileChannel fChannel = FileChannel.open(Paths.get("1.png"), StandardOpenOption.READ);
        while (fChannel.read(buf) != -1) {
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        // 4.关闭通道
        fChannel.close();
        sChannel.close();
    }

    // 服务端
    public static void server() throws IOException {
        // 1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 2.绑定连接端口号
        ssChannel.bind(new InetSocketAddress(9898));

        // 3.获取客户端连接
        SocketChannel sChannel = ssChannel.accept();

        // 4.接受客户端发送的数据并保存到本地
        FileChannel fChannel = FileChannel.open(Paths.get("2.png"), StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while (sChannel.read(buf) != -1) {
            buf.flip();
            fChannel.write(buf);
            buf.clear();
        }

        // 5.关闭通道
        sChannel.close();
        fChannel.close();
        ssChannel.close();
    }

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "启动");
                try {
                    server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "结束");
            }
        }, "服务端").start();

        try {
            client();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}