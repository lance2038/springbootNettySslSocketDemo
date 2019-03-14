package com.lance.api.business.socketserver.nativesocket;


import com.lance.api.business.util.DealSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.TrustManagerFactory;
import java.net.Socket;
import java.security.KeyStore;


/**
 * ssl socket
 *
 * @author lance
 * @since 2018-10-10
 */
@Component("serverSocket")
@Slf4j
public class ServerSocket implements Runnable
{
    @Value("${sslSocket.port}")
    private int port;
    @Value("${sslSocket.server_key_store_password}")
    private String serverKeyStorePassword;
    @Value("${sslSocket.server_trust_key_store_password}")
    private String serverTrustKeyStorePassword;

    @Autowired
    private DealSocket dealSocket;

    private SSLServerSocket serverSocket;


    /**
     * 启动程序
     */
    @Override
    public void run()
    {
        if (serverSocket == null)
        {
            log.error("ERROR:启动socket服务失败");
            return;
        }
        while (true)
        {
            try
            {
                Socket socket = serverSocket.accept();

                dealSocket.analysisAndDealMsg(socket);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化
     */
    public void init()
    {
        try
        {
            SSLContext ctx = SSLContext.getInstance("SSL");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");
            Resource fileResourceK = new ClassPathResource("keystores/kserver.keystore");
            Resource fileResourceT = new ClassPathResource("keystores/tserver.keystore");
            ks.load(fileResourceK.getInputStream(), serverKeyStorePassword.toCharArray());
            tks.load(fileResourceT.getInputStream(), serverTrustKeyStorePassword.toCharArray());

            kmf.init(ks, serverKeyStorePassword.toCharArray());
            tmf.init(tks);

            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            serverSocket = (SSLServerSocket) ctx.getServerSocketFactory().createServerSocket(port);
            serverSocket.setNeedClientAuth(true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
