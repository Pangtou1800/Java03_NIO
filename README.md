# Java03 NIO

## 1. Java NIO 简介

    Java NIO(New IO/Non Blocking IO)是从Java1.4版本开始引入的一个新的IO API，可以替代标准的JavaIO API。NIO与原来的IO有同样的作用和目的，但是使用的方式完全不同，NIO支持面向缓冲区的、基于通道的IO操作。NIO将以更加高效的方式进行文件的读写操作。

## 2. Java NIO与IO的主要区别

            IO                          NIO
    面向流（Stream Oriented）    面向缓冲区（Buffer Oriented）
    阻塞IO（Blocking IO）        非阻塞IO（Non Blocking IO）
    （无）                       选择器（Selector）

## 3. 缓冲区（Buffer）和通道（Channel）

    Java NIO系统的核心在于：通道（Channel）和缓冲区（Buffer）。通道表示打开到IO设备的连接。若需要使用NIO系统，需要获取用于连接IO设备的通道以及用于容纳数据的缓冲区。然后操作缓冲区对数据进行处理。

    简而言之，Channel负责传输，Buffer负责存储。

### 缓冲区（buffer）

    一、缓冲区（Buffer）

        一个用于特定基本数据类型的容器。由java.nio包所定义，所有缓冲区都是Buffer抽象类的子类。在Java NIO中，负责数据的存取。缓冲区就是数组。用于存储不同类型的数据。根据数据类型不同（boolean除外），提供了相应类型的缓冲区：
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

    三、缓冲区中的四个核心属性：
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

## 4. 通道（Channel）

    由java.nio.channels包定义。Channel表示IO源与目标打开的连接。Channel类似于传统的“流”。只不过Channel本身不能直接访问数据，Channel只能与Buffer进行交互。

    一、通道（Channel）：
        用于源节点与目标节点的连接。在Java NIO中负责缓冲区中数据的传输。
        Channel本身不存储数据，因此需要配合缓冲区进行传输。
    
    二、通道的主要实现类：
        java.nio.channels.Channel 接口
            |--FileChannel
            |--SocketChannel
            |--ServerSocketChannel
            |--DatagramChannel
    
    三、获取通道

        1. Java针对支持通道的类提供了getChannel()方法

            本地IO：
                FileInputStream/FileOutputStream
                RandomAccessFile
            网络IO：
                Socket
                ServerSocket
                DatagramSocket

        2. 在JDK1.7中的NIO.2针对各个通道提供了一个静态方法open()

        3. 在JDK1.7中的NIO.2的Files工具类的newByteChannel()

    
    四、通道之间的数据传输
        transferFrom()
        transferTo()
    
    五、分散（Scatter）与聚集（Gather）
        分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中
        聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到一个通道中
    
    六、字符集（Charset）
        编码：字符串(char) -> 字节数组(byte)
        解码：字节数组(byte) -> 字符串(char)

## 5. NIO的非阻塞式网络通信

    > 选择器（Selector）

    > SocketChannel, ServerSocketChannel, DatagramChannel

### 阻塞与非阻塞

    ·传统的IO流都是阻塞式的。
        也就是说，当一个线程调用read()或write()时，改线程被阻塞，直到有一些数据被读入或写入，该线程在此期间不能执行其他任务。
        因此，在完成网络通信的IO操作时，由于线程会阻塞，所以服务端必须为每一个客户端都提供一个独立的线程进行处理。
        当服务器端需要处理大量客户时，性能急剧下降。
    ·Java NIO是非阻塞式的。
        当线程从某通道读写数据时，若没有数据可用，该线程可以进行其他。
        线程通常将非阻塞IO的空闲时间用于在其他通道上执行IO操作，所以单独的线程可以管理多个输入和输出通道。
        因此，NIO可以让服务器端使用一个或几个有限的线程来同时处理连接到服务器端的所有客户端。

### 使用NIO完成网络通信的三个核心

    1.通道（Channel）：负责连接    

        java.nio.channels.Channel接口    
            |--SelectableChannel抽象类    
                |--SocketChannel    
                |--ServerSocketChannel    
                |--DatagramChannel    
    
                |--Pipe.SinkChannel    
                |--Pipe.SourceChannel    
        
        ※FileChannel不能切换为非阻塞模式    

    2.缓冲区（Buffer）：负责数据的存储    

    3.选择器（Selector）：是SelectChannel的多路复用器。用于监控SelectableChannel的IO状况    

## 6. 管道（Pipe）

    Java NIO管道是2个线程之间的单向数据连接。
    Pipe有一个source通道和一个sink通道。
    数据会被写到sink通道，从source通道读取。

## 7. Java NIO2（Path, Paths 与 Files）

---

# 基础知识补充：网络编程

## 一、网络编程概述

    ·Java是Internet上的语言，它从语言级上提供了对网络应用程序的支持，程序员能够很容易开发常见的网络应用程序。

    ·Java提供的网络类库，可以实现无痛的网络连接，联网的底层细节被隐藏在Java的本机安装系统里，由JVM进行控制。
    并且Java实现了一个跨平台的网络库，程序员面对的是一个统一的网络编程环境。

### 计算机网络

### 网络编程的目的

    直接或间接地通过网络协议与其他计算机实现数据交换，进行通讯。

### 网络编程中的两个主要问题

    1.如何准确定位网络上的一台或多台主机，以及定位主机上特定的应用

    2.找到主机后如何可靠高效地进行数据传输

## 二、网络通信中的两个要素

### 通信双方地址

    ·IP地址

    ·端口号

### 一定的规则

    ·OSI参考模型
        模型过于理想化，未能在因特网上进行广泛推广

    ·TCP/IP参考模型（或TCP/IP协议）
        事实上的国际标准

<table class="show">
<tr>
<th>OSI参考模型</th>
<th>TCP/IP参考模型</th>
<th>TCP/IP参考模型各层对应的协议</th>
</tr>
<tr>
<td>应用层</td>
<td rowspan="3">应用层</td>
<td rowspan="3">HTTP, FTP, Telnet, DNS, ...</td>
</tr>
<tr>
<td>表示层</td>
</tr>
<tr>
<td>会话层</td>
</tr>
<tr>
<td>传输层</td>
<td>传输层</td>
<td>TCP, UDP, ...</td>
</tr>
<tr>
<td>网络层</td>
<td>网络层</td>
<td>IP, ICMP, ARP, ...</td>
</tr>
<tr>
<td>数据链路层</td>
<td rowspan="2">物理+数据链路层</td>
<td rowspan="2">Link</td>
</tr>
<tr>
<td>物理层
</tr>
</table>

### IP(Internet Protocol)地址：InetAddress

    ·唯一的标识Internet上的计算机
    ·本地回环地址(hostAddress): 127.0.0.1
    ·主机名(hostName)：localhost

    ·IP地址的分类：
        1）
            >IPv4
                4个字节组成，4个0-255。大概42亿，30亿分配在北美，亚洲4亿。
            >IPv6
                16个字组成，写成8个无符号整数。
        2）
            >公网地址（万维网使用）
            >私有地址（局域网使用） => 192.168开头

### 端口号

    ·端口号标识正在计算机上运行的进程（程序）
    ·不同的进程有不同的端口号
    ·被规定为一个16位的整数0~65535
    ·端口分类：
        >公认端口：0~1023
            被预先定义的服务通信占用
                HTTP：80
                FTP：21
                Telnet：23
        >注册端口：1024~49151
            分配给用户进程或应用程序
                通常的默认端口如下
                    Tomcat：8080
                    MySQL：3306
                    Oracle：1521
        >动态/私有端口：49152~65535

    ·端口号与IP地址的组合得出一个网络套接字（Socket）。

### 网络通信协议：TCP/IP协议簇

    1.传输层协议中有两个非常重要的协议：

        ·传输控制协议TCP（Transmission Control Protocol）

        ·用户数据报协议UDP（User Datagram Protocol）

    2.TCP/IP以其两个主要协议：传输控制协议（TCP）和网络互联协议（IP）而得名，实际上是一组协议。
    包括多个具有不同功能且互为关联的协议。

    3.IP（Internet Protocol）协议是网络层的主要协议，支持网间互联的数据通信。

    4.TCP/IP协议模型从更实用的角度出发，形成了高效的四层体系结构，即：
        ·物理链路层
        ·IP层
        ·传输层
        ·应用层

### TCP和UDP

<dl>
<dt><b>TCP协议</b></dt>
<dd>使用TCP协议前，须先建立TCP连接，形成数据传输通道</dd>
<dd>传输前，采用“三次握手”方式，点对点通信，是可靠的</dd>
<dd>TCP协议进行通信的两个应用进程：客户端，服务端</dd>
<dd>在连接中可进行大数据量的传输</dd>
<dd>传输完毕，采用“四次挥手”方式，放已建立的连接，效率相对较低</dd>
<dt><b>UDP协议</b></dt>
<dd>将数据、源、目的封装进数据报，不需要建立连接</dd>
<dd>每个数据报的大小限制在64K以内</dd>
<dd>发送不管对方是否准备好，接受方收到也不确认，是不可靠的</dd>
<dd>可以广播发送</dd>
<dd>发送数据结束时无需释放资源，开销小，速度快</dd>
</dl>

#### TCP三次握手

        客户端                                      服务端

    发送SYN报文，
    并置发送序号为X
                        --- seq=X,SYN=1 -->
                                                发送SYN + ACK报文，
                                                并置发送序号为Y，
                                                在确认序号为X+1
                    <-- ACK=X+1,seq=Y,SYN=1 ---
    发送ACK报文，
    并置发送序号为Z，
    在确认序号为Y+1
                        --- ACK=Y+1,seq=Z -->
                                                握手成功

#### TCP四次挥手

        主动方                                      被动方

    发送FIN+ACK报文，
    并置发送序号为X
                      --- FIN=1,ACK=Z,SEQ=X -->
                                                发送ACK报文，
                                                并置发送序号为Z，
                                                在确认序号为X+1
                        <-- ACK=X+1,seq=Z ---
                                                发送FIN+ACK报文，
                                                并置发送序号为Y，
                                                在确认序号为X
                      <-- FIN=1,ACK=X,SEQ=Y ---

    发送ACK报文，
    并置发送序号为X，
    在确认序号为Y
                          --- ACK=Y,seq=X -->
                                                挥手成功

#### UDP网络通信

    ·类DatagramSocket和DatagramPacket实现了基于UDP协议的网络程序。

    ·UDP数据报通过数据报套接字DatagramSocket发送和接收.
    系统不保证UDP数据报一定能够安全送到目的地，也不能确定什么时候可以抵达。

    ·DatagramPacket对象封装了UDP数据报，在数据报中包含了发送端的IP地址和端口号以及接收端的IP地址和端口号。

    ·UDP协议中的每个数据报都给出了完整的地址信息，因此无需建立发送方和接收方的连接。

### URL编程

    URL(Uniform Resource Locator)：统一资源定位符，它表示Internet上某一资源的地址。

    ·它是一种具体的URI，即URL可以用来标识一个资源，而且还指明了如何locate这个资源。

    ·URL的基本结构由5个部分组成：
    <传输协议>://<主机名>:<端口号>/<文件名>#片段名?参数列表

#### java.net.URL的常用方法

    public String getProtocol()

    public String getHost()

    public String getPort()

    public String getPath()

    public String getFile()

    public String getQuery()
    

<style>
.show {

    margin-left: auto;
    margin-right: auto;

}
.show td {

    border: 1px solid grey;
    text-align: center;

}
</style>
