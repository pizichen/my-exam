package com.merlin.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

public class TimeServerHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) {
        // 类似于JDK中得ByteBuffer对象，但功能更强大
        ByteBuf byteBuf = (ByteBuf)object;
        byte[] request = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(request);
        String body = new String(request);
        System.out.println("The time server receive order:" + body);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ?
                new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
        ByteBuf response = Unpooled.copiedBuffer(currentTime.getBytes());
        channelHandlerContext.write(response);
    }

    @Override
    public void channelReadComplete (ChannelHandlerContext channelHandlerContext){
        channelHandlerContext.flush();
    }

    @Override
    public void exceptionCaught (ChannelHandlerContext channelHandlerContext, Throwable throwable){
        channelHandlerContext.close();
    }
}
