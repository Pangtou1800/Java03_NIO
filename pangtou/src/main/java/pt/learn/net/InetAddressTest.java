package pt.learn.net;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 网络通信的两个要素：<br>
 * 1.IP和端口号<br>
 * 2.网络通信协议：TCP/IP参考模型（应用层=>HTTP、传输层=>TCP、网络层=>IP、物理+数据链路层）<br>
 * 
 * IP和端口号<br>
 * 1. IP:唯一的标识Internet上的计算机<br>
 * 2. 在Java中使用InetAddress类代表IP<br>
 * 3. 分类:IPv4, IPv6; 万维网和局域网<br>
 * 4. 域名：www.baidu.com<br>
 * 5. 本地回路地址：127.0.0.1<br>
 * 6. 如何实例化InetAddress:<br>
 * 1)getByName(String hostname)<br>
 * 2)getLocalHost()<br>
 * 两个常用方法：getHostName(), getHostAddress()<br>
 */
public class InetAddressTest {
    public static void main(String[] args) {
        try {
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            System.out.println(ip);

            ip = InetAddress.getByName("localhost");
            System.out.println(ip);
            ip = InetAddress.getLocalHost();
            System.out.println(ip);

            ip = InetAddress.getByName("www.baidu.com");
            System.out.println(ip);

            System.out.println(ip.getHostName());
            System.out.println(ip.getHostAddress());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}