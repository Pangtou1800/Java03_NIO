package pt.learn.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;

/**
 * 一、通道（Channel）：用于源节点与目标节点的连接。在Java NIO中负责缓冲区中数据的传输。
 * Channel本身不存储数据，因此需要配合缓冲区进行传输。
 * 
 * 二、通道的主要实现类：<br>
 * java.nio.channels.Channel 接口<br>
 * |--FileChannel<br>
 * |--SocketChannel<br>
 * |--ServerSocketChannel<br>
 * |--DatagramChannel<br>
 * 
 * 三、获取通道<br>
 * 1. Java针对支持通道的类提供了getChannel()方法<br>
 * 本地IO：<br>
 * FileInputStream/FileOutputStream<br>
 * RandomAccessFile<br>
 * 网络IO：<br>
 * Socket ServerSocket<br>
 * DatagramSocket<br>
 * 2. 在JDK1.7中的NIO.2针对各个通道提供了一个静态方法open()<br>
 * 3. 在JDK1.7中的NIO.2的Files工具类的newByteChannel()
 * 
 * 四、通道之间的数据传输<br>
 * transferFrom()<br>
 * transferTo()<br>
 * 
 * 五、分散（Scatter）与聚集（Gather）<br>
 * 分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中<br>
 * 聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到一个通道中<br>
 * 
 * 六、字符集（Charset）<br>
 * 编码：字符串 -> 字节数组<br>
 * 解码：字节数组 -> 字符串<br>
 */
public class TestChannel {
    public static void main(String[] args) throws IOException {
        // long start = System.currentTimeMillis();
        // test1();
        // System.out.println("Passed: " + (System.currentTimeMillis() - start));
        // start = System.currentTimeMillis();
        // test2();
        // System.out.println("Passed: " + (System.currentTimeMillis() - start));
        // test3();
        // test4();
        // test5();
        test6();
    }

    // 1.利用通道完成文件的复制（非直接缓冲区）
    public static void test1() throws IOException {
        FileInputStream fis = new FileInputStream("1.png");
        FileOutputStream fos = new FileOutputStream("2.png");

        // 1)获取通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        // 2)分配指定大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);

        // 3)将通道中的数据读入缓冲区
        while (inChannel.read(buffer) != -1) {
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        }

        outChannel.close();
        inChannel.close();
        fos.close();
        fis.close();
    }

    // 2.利用通道完成文件的复制（直接缓冲区）
    public static void test2() throws IOException {

        // 1)获取通道
        FileChannel inChannel = FileChannel.open(Paths.get("1.png"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("2.png"), StandardOpenOption.READ, StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);

        // 2)获取内存映射文件
        MappedByteBuffer inMappedBuf = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBuf = outChannel.map(MapMode.READ_WRITE, 0, inChannel.size());

        // 3)直接对缓冲区进行数据的读写操作
        byte[] dst = new byte[inMappedBuf.limit()];
        inMappedBuf.get(dst);
        outMappedBuf.put(dst);

        outChannel.close();
        inChannel.close();
    }

    // 3.通道之间的数据传输
    public static void test3() throws IOException {
        // 1)获取通道
        FileChannel inChannel = FileChannel.open(Paths.get("1.png"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("2.png"), StandardOpenOption.READ, StandardOpenOption.WRITE,
                StandardOpenOption.CREATE);

        inChannel.transferTo(0, inChannel.size(), outChannel);
        outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    // 4.分散和聚集
    public static void test4() throws IOException {
        RandomAccessFile raf1 = new RandomAccessFile("1.txt", "rw");

        // 1)获取通道
        FileChannel ch1 = raf1.getChannel();

        // 2)分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(5);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);

        // 3)分散读取
        ByteBuffer[] bufs = { buf1, buf2 };
        ch1.read(bufs);

        for (ByteBuffer buf : bufs) {
            buf.flip();
        }

        System.out.println(new String(buf1.array(), 0, buf1.limit()));
        System.out.println(new String(buf2.array(), 0, buf1.limit()));

        // 4)聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("2.txt", "rw");
        FileChannel ch2 = raf2.getChannel();

        ch2.write(bufs);

        ch1.close();
        ch2.close();
        raf1.close();
        raf2.close();
    }

    public static void test5() {
        Map<String, Charset> map = Charset.availableCharsets();
        System.out.println(map);
    }

    public static void test6() throws CharacterCodingException {
        Charset cs1 = Charset.forName("GBK");

        // 获取编码器与解码器
        CharsetEncoder encoder = cs1.newEncoder();
        CharsetDecoder decoder = cs1.newDecoder();

        CharBuffer cb = CharBuffer.allocate(100);
        cb.put("我要学Java");
        cb.flip();

        ByteBuffer bb = encoder.encode(cb);
        System.out.println(Arrays.toString(bb.array()));

        cb = CharBuffer.allocate(100);
        cb = decoder.decode(bb);
        System.out.println(Arrays.toString(cb.array()));

        Charset cs2 = Charset.forName("UTF-8");
        cb = CharBuffer.allocate(100);
        bb.flip();
        cb = cs2.decode(bb);
        System.out.println(Arrays.toString(cb.array()));
    }
}
