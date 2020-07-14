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

## 4. 文件通道（FileChannel）

## 5. NIO的非阻塞式网络通信

    > 选择器（Selector）

    > SocketChannle, ServerSocketChannel, DatagramChannel

## 6. 管道（Pipe）

## 7. Java NIO2（Path, Paths 与 Files）
