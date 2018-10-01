package com.merlin.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @Title AsyncTimeClientHandler
 * @ProjectName exam
 * @Description 客户端handler
 * @Author Merlin Chen
 * @Date 2018/9/26 16:11
 **/
public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

    private AsynchronousSocketChannel client;

    private String host;

    private int port;

    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.port = port;
        this.host = host;
        try{
            client = AsynchronousSocketChannel.open();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void  run(){
        // 防止操作未完成导致线程推出
        latch = new CountDownLatch(1);
        client.connect(new InetSocketAddress(host, port), this, this);
        try {
            latch.await();
        }catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        try {
            client.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void completed(Void result, AsyncTimeClientHandler clientHandler) {
        // 创建请求消息体
        byte[] request = "QUERY TIME ORDER".getBytes();
        // 复制到写缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(request.length);
        writeBuffer.put(request);
        writeBuffer.flip();
        client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer byteBuffer) {
                // 如果有尚未发送的数据，则继续发送直到完全发送成功
                if(byteBuffer.hasRemaining()) {
                    client.write(byteBuffer, byteBuffer, this);
                }else { // 数据读取
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {
                            attachment.flip();
                            byte[] bytes = new byte[attachment.remaining()];
                            attachment.get(bytes);
                            String body;
                            try{
                                body = new String(bytes);
                                System.out.println("Now is:" + body);
                                latch.countDown();
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                client.close();
                                latch.countDown();
                            }catch (IOException e) {
                                // ignore on close
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
                try {
                    client.close();
                    latch.countDown();
                }catch (IOException e) {
                    // ignore on close
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        exc.printStackTrace();
        try {
            client.close();
            latch.countDown();
        }catch (IOException e) {
            // ignore on close
        }
    }
}
