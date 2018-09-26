package com.merlin.netty.aio;

import com.merlin.netty.nio.TimeClientHandle;

import java.io.IOException;

public class TimeClient {

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
        AsyncTimeClientHandler timeClientHandle = new AsyncTimeClientHandler("127.0.0.1", port);

        new Thread(timeClientHandle, "AIO-AsyncTimeClientHandler-001").start();
    }
}
