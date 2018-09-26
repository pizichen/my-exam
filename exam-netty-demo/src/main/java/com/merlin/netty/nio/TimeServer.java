package com.merlin.netty.nio;

import com.merlin.netty.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TimeServer {

    public static void main(String[] args) throws IOException{
        int port = 8090;
        if (args != null && args.length > 0) {
            try {
                Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
//                采用默认值
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);

        new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
    }
}
