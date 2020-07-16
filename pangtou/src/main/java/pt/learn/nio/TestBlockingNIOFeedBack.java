package pt.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TestBlockingNIOFeedBack {
    public static void main(String[] args) {
        new Thread(new TestServer(), "Server").start();
        new Thread(new TestClient(), "Client").start();
    }
}

class TestClient implements Runnable {
    @Override
    public void run() {
        try {

            System.out.println(Thread.currentThread().getName() + " started.");
            SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

            FileChannel fChannel = FileChannel.open(Paths.get("1.png"), StandardOpenOption.READ);

            ByteBuffer buf = ByteBuffer.allocate(1024);

            while (fChannel.read(buf) != -1) {
                buf.flip();
                sChannel.write(buf);
                buf.clear();
            }

            sChannel.shutdownOutput();

            System.out.println(Thread.currentThread().getName() + " file send.");
            // ?
            while (sChannel.read(buf) != -1) {
                buf.flip();
                System.out.println(Thread.currentThread().getName() + " feedback received.");
                System.out.println(new String(buf.array(), 0, buf.limit()));
                buf.clear();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

class TestServer implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println(Thread.currentThread().getName() + " started.");
            ServerSocketChannel ssChannel = ServerSocketChannel.open();
            ssChannel.bind(new InetSocketAddress(9898));

            SocketChannel sChannel = ssChannel.accept();
            FileChannel fChannel = FileChannel.open(Paths.get("2.png"), StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE);

            ByteBuffer buf = ByteBuffer.allocate(3000);
            while (sChannel.read(buf) != -1) {
                buf.flip();
                fChannel.write(buf);
                buf.clear();
            }
            System.out.println(Thread.currentThread().getName() + " file received.");

            fChannel.close();
            sChannel.shutdownInput();

            buf.put("OK".getBytes());
            buf.flip();
            sChannel.write(buf);
            System.out.println(Thread.currentThread().getName() + " feedback send.");

            sChannel.close();
            ssChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}