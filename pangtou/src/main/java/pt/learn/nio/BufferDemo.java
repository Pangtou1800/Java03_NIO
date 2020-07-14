package pt.learn.nio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 一、缓冲区（Buffer）：在Java NIO中，负责数据的存取。缓冲区就是数组。用于存储不同类型的数据。<br>
 * 
 * 根据数据类型不同（boolean除外），提供了相应类型的缓冲区：<br>
 * ByteBuffer<br>
 * CharBuffer<br>
 * ShortBuffer<br>
 * IntBuffer<br>
 * LongBuffer<br>
 * FloatBuffer<br>
 * DoubleBuffer<br>
 * 
 * 上述缓冲区的管理方式几乎一致，通过allocate()获取缓冲区<br>
 * 
 * 二、缓冲区存取数据的两个核心方法：<br>
 * put()：存入数据到缓冲区中<br>
 * get()：获取缓冲区中的数据<br>
 * 
 * 四、缓冲区中的四个核心属性：<br>
 * capacity:容量，表示缓冲区中最大存储数据的容量，一旦声明不能改变。<br>
 * limit:界限，表示缓冲区中可以操作数据的大小。 => limit后的数据不能读写<br>
 * position:位置，表示缓冲区中正在操作数据的位置。<br>
 * mark:标记，可以记录当前position的位置。可以通过reset()恢复到mark的位置<br>
 * 
 * 0 <= mark <= position <= limit <= capacity
 * 
 * 五、直接缓冲区与非直接缓冲区<br>
 * 非直接缓冲区：通过allocate()方法分配缓冲区，件缓冲区建立在JVM的内存中<br>
 * 直接缓冲区：通过allocateDirect()方法分配缓冲区，将缓冲区建立在物理内存中(可以提高效率)<br>
 */
public class BufferDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        // 1.分配一个指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println("---------- allocate() ----------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 2.利用put()存入数据到缓冲区去
        String str = "ABCDE";
        buf.put(str.getBytes("UTF-8"));

        System.out.println("------------- put() ------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 3.调用flip()切换为读数据模式
        buf.flip();

        System.out.println("------------ flip() ------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 4.利用get()读取缓冲区中的数据
        byte[] dst = new byte[buf.limit()];
        buf.get(dst);

        System.out.println("------------- get() ------------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println(new String(dst));

        // 5.利用rewind()重复读数据
        buf.rewind();

        System.out.println("----------- rewind() -----------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        // 6.clear()清空缓冲区，但是缓冲区中的数据依然存在，只是处于“被遗忘”状态
        buf.clear();

        System.out.println("------------ clear() -----------");
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        System.out.println((char) buf.get());

        test2();

        test3();
    }

    public static void test2() {
        String str = "ABCDE";

        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(str.getBytes());
        buf.flip();

        byte[] dst = new byte[buf.limit()];
        buf.get(dst, 0, 2);
        System.out.println(new String(dst, 0, 2));
        System.out.println(buf.position());

        buf.mark();

        buf.get(dst, 2, 2);
        System.out.println(new String(dst, 0, 4));
        System.out.println(buf.position());

        buf.reset();

        System.out.println(buf.position());

        // 判断缓冲区中是否还有剩余数据
        if (buf.hasRemaining()) {
            // 返回缓冲区中的剩余数据
            System.out.println(buf.remaining());
        }
    }

    public static void test3() {
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);

        System.out.println(buf.isDirect());
    }
}