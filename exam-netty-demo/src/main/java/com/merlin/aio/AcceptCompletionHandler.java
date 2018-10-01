package com.merlin.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @Title AcceptCompletionHandler
 * @ProjectName exam
 * @Description 接收通知的回调handler
 * @Author Merlin Chen
 * @Date 2018/9/26 15:36
 **/
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {

    public void completed(AsynchronousSocketChannel socketChannel, AsyncTimeServerHandler serverHandler){
        serverHandler.socketChannel.accept(serverHandler, this);
        // 预分配1M的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        socketChannel.read(buffer, buffer, new ReadCompletionHandler(socketChannel));
    }

    public void failed(Throwable exc, AsyncTimeServerHandler serverHandler) {
        exc.printStackTrace();
        serverHandler.latch.countDown();
    }
}
