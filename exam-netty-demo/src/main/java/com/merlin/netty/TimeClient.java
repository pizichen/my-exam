package com.merlin.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

    public void connect(int port, String host) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        public void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 等待客户端链路关闭.
            /*
             * 开发过程中由于没有调用sync()方法，导致服务器端接受到了客户端的请求并返回了应答，但是客户端一直没有接收到
             * 服务器端的应答。也即是，没有回到TimeClientHandler的channelRead()方法
             */
            channelFuture.channel().closeFuture().sync();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            // 退出，同时释放NIO线程组
            eventLoopGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        int port = 9090;
        String host = "127.0.0.1";
        if(args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new TimeClient().connect(port, host);
    }
}
