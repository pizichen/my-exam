package com.merlin.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @Title AsyncTimeServerHandle
 * @ProjectName exam
 * @Description TODO
 * @Author Merlin Chen
 * @Date 2018/9/26 15:27
 **/
public class AsyncTimeServerHandler implements Runnable {

    private int port;

    CountDownLatch latch;

    AsynchronousServerSocketChannel socketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            // 创建一个异步服务端通道，并绑定监听端口
            socketChannel = AsynchronousServerSocketChannel.open();
            socketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port:" + port);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        /*
         * 初始化CountDownLatch对象，用于在完成一组正在执行的操作之前，允许当前的线程一直阻塞。
         * 在本实例中，我们让线程在此阻塞，防止服务端执行完成退出。在实际项目中，不需要启动独立的线程来处理
         * AsynchronousServerSocketChannel，这里仅是一个demo
         */

        latch = new CountDownLatch(1);
        doAccept();
        try {
            latch.await();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doAccept(){
        socketChannel.accept(this, new AcceptCompletionHandler());
    }
}
