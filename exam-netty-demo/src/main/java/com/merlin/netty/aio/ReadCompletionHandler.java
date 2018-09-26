package com.merlin.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * @Title ReadCompletionHandler
 * @ProjectName exam
 * @Description TODO
 * @Author Merlin Chen
 * @Date 2018/9/26 15:50
 **/
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel socketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel){
        if (this.socketChannel == null) {
            this.socketChannel = socketChannel;
        }
    }

    public void completed(Integer result, ByteBuffer attachment){
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);
        String request = new String(body);
        System.out.println("The time server receive order:" + request);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(request)
                ? new Date(System.currentTimeMillis()).toString() : "BAD REQUEST";
        doWrite(currentTime);
    }

    private void doWrite(String currentTime){
        if(currentTime != null && currentTime.trim().length() > 0) {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    // 如果没有发送成功，则继续发送
                    if(attachment.hasRemaining()) {
                        socketChannel.write(attachment, attachment, this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    try {
                        socketChannel.close();
                    }catch (IOException e) {
                        // ignore on close
                    }
                }
            });
        }
    }

    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.socketChannel.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
