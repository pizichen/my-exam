package com.merlin.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

    public void bind(int port) {
        System.out.println("server start int " + port + "...");
        // 创建两个EventLoopGroup的原因是，一个用于服务端接受客户端的连接，另一个用于进行SocketChannel得网络读写
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // .childHandler()绑定I/O处理类，主要用于处理网络I/O事件处理，例如记录日志、对消息进行编码解码等
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChildChannelHandler());
            // 绑定端口，等待完成同步
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

            // 等待服务端监听端口关闭
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) {
            socketChannel.pipeline().addLast(new TimeServerHandler());
        }
    }


    public static void main(String[] args) {
        int port = 9090;
        if(args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        new TimeServer().bind(port);
    }
}
