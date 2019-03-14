package com.lance.api.business.socketserver.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.security.KeyStore;

/**
 * netty服务启动类
 *
 * @author lance
 */
@Component
@Slf4j
public class NettyServer extends ChannelInitializer
{
    @Value("${sslSocket.nettyPort}")
    private int port;
    @Value("${sslSocket.sslFlag:false}")
    private boolean ssl;
    @Value("${sslSocket.server_key_store_password}")
    private String serverKeyStorePassword;
    @Value("${sslSocket.server_trust_key_store_password}")
    private String serverTrustKeyStorePassWord;

    private static final EventLoopGroup BOSS_GROUP = new NioEventLoopGroup();
    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup();

    /**
     * 业务处理器
     */
    @Autowired
    private ServerHandler serverHandler;

    /**
     * 关闭服务
     */
    @PreDestroy
    public void close()
    {
        log.info("关闭服务....");
        BOSS_GROUP.shutdownGracefully();
        WORKER_GROUP.shutdownGracefully();
    }

    /**
     * 开启服务
     */
    public void start() throws Exception
    {
        final SSLContext sslCtx;
        if (ssl)
        {
            sslCtx = SSLContext.getInstance("SSL");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");

            org.springframework.core.io.Resource frk = new ClassPathResource("keystores/kserver.keystore");
            org.springframework.core.io.Resource frt = new ClassPathResource("keystores/tserver.keystore");
            ks.load(frk.getInputStream(), serverKeyStorePassword.toCharArray());
            tks.load(frt.getInputStream(), serverTrustKeyStorePassWord.toCharArray());

            kmf.init(ks, serverKeyStorePassword.toCharArray());
            tmf.init(tks);

            sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        else
        {
            sslCtx = null;
        }
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(BOSS_GROUP, WORKER_GROUP);
        serverBootstrap.channel(NioServerSocketChannel.class);
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 100);

        try
        {
            //设置事件处理
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>()
            {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception
                {
                    if (sslCtx != null)
                    {
                        SSLEngine sslEngine = sslCtx.createSSLEngine();
                        sslEngine.setUseClientMode(false);
                        ch.pipeline().addFirst(new SslHandler(sslEngine));
                    }
                    ChannelPipeline pipeline = ch.pipeline();
                    // 解码器
                    pipeline.addLast(new MessageDecoder());
                    // 编码器
                    pipeline.addLast(new MessageEncoder());
                    // 业务处理器
                    pipeline.addLast(serverHandler);

                }
            });
            log.info("netty服务在[{}]端口启动监听", port);
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        }
        catch (InterruptedException e)
        {
            log.info("[出现异常] 释放资源");
            BOSS_GROUP.shutdownGracefully();
            WORKER_GROUP.shutdownGracefully();
        }
    }

    @Override
    protected void initChannel(Channel channel) throws Exception
    {

    }
}