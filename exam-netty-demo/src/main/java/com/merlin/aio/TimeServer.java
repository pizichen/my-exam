package com.merlin.aio;

import java.io.IOException;

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
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);

        new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
    }
}
