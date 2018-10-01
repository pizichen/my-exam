package com.merlin.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * @title ReadCompletionHandler
 * @ProjectName exam
 * @Description 读取通知的回调handler
 * @author  Merlin Chen
 * @date  2018/9/26 15:50
 **/
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private static String TRANSFER_CONTENT = "QUERY TIME ORDER";

    /**
     * 将AsynchronousSocketChannel通过参数传递到ReadCompletionHandler中当做成员变量来使用，
     * 主要用于读取半包消息和发送应答。
     */
    private AsynchronousSocketChannel socketChannel;

    public ReadCompletionHandler(AsynchronousSocketChannel socketChannel){
        this.socketChannel = socketChannel;
    }

    public void completed(Integer result, ByteBuffer byteBuffer){
        // 为后续从缓冲区读取数据做准备
        byteBuffer.flip();
        byte[] body = new byte[byteBuffer.remaining()];
        byteBuffer.get(body);
        String request = new String(body);
        System.out.println("The time server receive order:" + request);
        String currentTime = TRANSFER_CONTENT.equalsIgnoreCase(request)
                ? new Date(System.currentTimeMillis()).toString() : "BAD REQUEST";
        doWrite(currentTime);
    }

    /**
     *  服务器端应道客户端请求
     * @param currentTime 系统时间字符串描述
     */
    private void doWrite(String currentTime){
        if(currentTime != null && currentTime.trim().length() > 0) {
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            socketChannel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer byteBuffer) {
                    // 如果还有剩余的字节可写，说明没有发送完成，需要继续发送知道发送成功
                    if(byteBuffer.hasRemaining()) {
                        socketChannel.write(byteBuffer, byteBuffer, this);
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

    public void failed(Throwable exc, ByteBuffer byteBuffer) {
        try {
            this.socketChannel.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
