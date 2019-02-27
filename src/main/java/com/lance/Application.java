package com.lance;

import com.lance.api.business.socketserver.nativesocket.ServerSocket;
import com.lance.api.business.socketserver.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.concurrent.Executor;

/**
 * <p>启动类
 *
 * @author lance
 * @since 2018-10-15
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application implements CommandLineRunner
{
    @Autowired
    private NettyServer nettyServer;
    @Autowired
    private ServerSocket serverSocket;
    @Autowired
    private Executor threadAsyncServiceProvider;

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        serverSocket.init();
        // 启动新线程是为了原生socket与netty共存启动
        threadAsyncServiceProvider.execute(serverSocket);
        // 启动netty-socket
        nettyServer.start();
    }
}
