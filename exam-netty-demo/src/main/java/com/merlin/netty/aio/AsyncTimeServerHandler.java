package com.merlin.netty.aio;

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
            socketChannel = AsynchronousServerSocketChannel.open();
            socketChannel.bind(new InetSocketAddress(port));
            System.out.println("The time server is start in port:" + port);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
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
