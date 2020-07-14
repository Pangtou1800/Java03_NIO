# Java03 NIO

## 1. Java NIO 简介

    Java NIO(New IO/Non Blocking IO)是从Java1.4版本开始引入的一个新的IO API，可以替代标准
    的JavaIO API。NIO与原来的IO有同样的作用和目的，但是使用的方式完全不同，NIO支持面向缓冲区
    的、基于通道的IO操作。NIO将以更加高效的方式进行文件的读写操作。

## 2. Java NIO与IO的主要区别

            IO                          NIO
    面向流（Stream Oriented）    面向缓冲区（Buffer Oriented）
    阻塞IO（Blocking IO）        非阻塞IO（Non Blocking IO）
    （无）                       选择器（Selector）

## 3. 缓冲区（Buffer）和通道（Channel）

    Java NIO系统的核心在于：通道（Channel）和缓冲区（Buffer）。通道表示打开到IO设备的连接。
    若需要使用NIO系统，需要获取用于连接IO设备的通道以及用于容纳数据的缓冲区。然后操作缓冲区
    对数据进行处理。

    简而言之，Channel负责传输，Buffer负责存储。

### 缓冲区（buffer）

    
    一、缓冲区（Buffer）
        一个用于特定基本数据类型的容器。
        由java.nio包所定义，所有缓冲区都是Buffer抽象类的子类。
        在Java NIO中，负责数据的存取。缓冲区就是数组。用于存储不同类型的数据。
        根据数据类型不同（boolean除外），提供了相应类型的缓冲区：
            ·ByteBuffer
            ·CharBuffer
            ·ShortBuffer
            ·IntBuffer
            ·LongBuffer
            ·FloatBuffer
            ·DoubleBuffer

        上述缓冲区的管理方式几乎一致，通过allocate()获取缓冲区

    二、缓冲区存取数据的两个核心方法：
        ·put()：存入数据到缓冲区中
        ·get()：获取缓冲区中的数据

    四、缓冲区中的四个核心属性：
        ·capacity:容量，表示缓冲区中最大存储数据的容量，一旦声明不能改变。
        ·limit:界限，表示缓冲区中可以操作数据的大小。 => limit后的数据不能读写
        ·position:位置，表示缓冲区中正在操作数据的位置。
        ·mark:标记，可以记录当前position的位置。可以通过reset()恢复到mark的位置

            0 <= mark <= position <= limit <= capacity

### 直接缓冲区与非直接缓冲区

    ·字节缓冲区要么是直接的，要么是非直接的。如果为直接字节缓冲区，则Java虚拟机会尽最大努力直接在此缓冲区上执行本机IO操作。也就是说，在每次调用基础操作系统的一个本机IO操作之前、之后，虚拟机都会尽量避免将缓冲区的内容复制到中间缓冲区（或从中间缓冲区中复制内容）。

    ·直接缓冲区可以通过调用此类的allocateDirect()工厂方法来创建。此方法返回的缓冲区进行分配和取消所需成本通常高于非直接缓冲区。直接缓冲区的内容可以驻留在常规的垃圾回收堆之外，因此，它们对应用程序的内存需求量造成的影响可能并不明显。所以，建议将直接缓冲区分配给那些易受基础系统的本机IO操作影响的大型、持久的缓冲区。一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好处时分配它们。

    ·直接字节缓冲区还可以通过FileChannel的map()方法将文件区域直接映射到内存中来创建。该方法返回MappedByteBuffer。Java平台的实现有助于通过JNI从本机代码直接创建字节缓冲区。如果以上这些缓冲区中的某个缓冲区实例指的是不可访问的内存区域，则试图访问该区域不会更改缓冲区的内容，并且会在访问期间或稍后的某个时间导致抛出不确定的异常。

    ·字节缓冲区是直接缓冲区还是非直接缓冲区可通过调用其isDirect()方法来确定。提供此方法是为了能够在性能关键型代码中执行显式缓冲区管理。

#### 非直接缓冲区

                        内核地址空间        用户地址空间
    物理磁盘  -- read() --> 缓存  -- copy --> 缓存  -- read()  --> 应用程序
    物理磁盘 <-- write() -- 缓存 <-- copy --  缓存 <-- write() --  应用程序

#### 直接缓冲区

                        内核地址空间                  用户地址空间
    物理磁盘  -- read() --> 缓存 <-- 物理内存映射文件 --> 缓存  -- read()  --> 应用程序
    物理磁盘 <-- write() -- 缓存 <-- 物理内存映射文件 --> 缓存 <-- write() --  应用程序

## 4. 文件通道（FileChannel）

## 5. NIO的非阻塞式网络通信

    > 选择器（Selector）

    > SocketChannle, ServerSocketChannel, DatagramChannel

## 6. 管道（Pipe）

## 7. Java NIO2（Path, Paths 与 Files）
