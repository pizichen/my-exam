package com.merlin.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


public class TimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] request = "QUERY TIME ORDER".getBytes();
        firstMessage = Unpooled.buffer(request.length);
        System.out.println("request length..." + request.length);
        firstMessage.writeBytes(request);
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) {
        channelHandlerContext.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object object) {
        ByteBuf byteBuf = (ByteBuf)object;
        byte[] request = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(request);
        String body = new String(request);
        System.out.println("Now is:" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        System.out.println("exceptionCaught ...");
        channelHandlerContext.close();
    }
}


