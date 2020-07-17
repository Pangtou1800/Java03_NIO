package pt.learn.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class TestPipe {

    public static void main(String[] args) {
        try {
            test1();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test1() throws IOException {
        // 1.获取管道
        Pipe pipe = Pipe.open();

        // 2.将缓冲区数据写入管道
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("通过单向管道发送数据".getBytes());
        buf.flip();

        Pipe.SinkChannel sinkChannel = pipe.sink();
        sinkChannel.write(buf);

        // 3.读取缓冲区中的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        buf = ByteBuffer.allocate(100);
        sourceChannel.read(buf);
        buf.flip();
        System.out.println(new String(buf.array(), 0, buf.limit()));

        sourceChannel.close();
        sinkChannel.close();
    }
}