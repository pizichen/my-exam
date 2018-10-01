package com.merlin.aio;

import java.io.IOException;

public class TimeClient {

    public static void main(String[] args) throws IOException{
        int port = 8090;
        if (args != null && args.length > 0) {
            try {
                Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // 采用默认值 8090
            }
        }

        AsyncTimeClientHandler timeClientHandle = new AsyncTimeClientHandler("127.0.0.1", port);
        // 在实际项目中我们不需要独立的线程创建异步连接对象，因为底层都是通过jdk的系统回调实现的
        // 实际项目应该如何处理呢？？？
        new Thread(timeClientHandle, "AIO-AsyncTimeClientHandler-001").start();
    }
}
