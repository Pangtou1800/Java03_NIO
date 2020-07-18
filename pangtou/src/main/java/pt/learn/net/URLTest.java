package pt.learn.net;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL网络编程<br>
 * 1.URL:统一资源定位符，对应着互联网上的某一资源地址<br>
 * 2.格式：<br>
 * http://localhost:8080/examples/beauty.jpg#chap1?user=pangtou <br>
 * 协议 主机名 端口号 资源地址 片段名（锚点） 参数列表 <br>
 */
public class URLTest {
    public static void main(String[] args) throws MalformedURLException {
        URL url = new URL("http://localhost:8080/examples/beauty.jpg?user=pangtou#chap1");

        System.out.println(url.getProtocol());
        System.out.println(url.getHost());
        System.out.println(url.getPort());
        System.out.println(url.getPath());
        System.out.println(url.getFile());
        System.out.println(url.getQuery());
    }
}