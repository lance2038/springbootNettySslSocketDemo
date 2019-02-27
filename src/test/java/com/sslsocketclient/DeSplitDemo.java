package com.sslsocketclient;

import com.alibaba.fastjson.JSON;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeSplitDemo
{
    private static final byte BLOCK_START_SIGN = 0x68;
    private static final String ENCODING = "UTF-8";
    //服务端地址
    private static final String serverHost = "127.0.0.1";
    //服务端监听端口
    private static final int serverPort = 9002;
    //客户端私钥密码
    private static final String clientKeyPassword = "123456";
    private static final String kcp = "src\\test\\java\\com\\sslsocketclient\\res\\kclient.keystore";
    //客户端信任证书密码
    private static final String trustKeyPassword = "123456";
    private static final String tcp = "src\\test\\java\\com\\sslsocketclient\\res\\tclient.keystore";

    public static Socket createSocketNote()
    {
        try
        {
            FileInputStream clientPrivateKey = new FileInputStream(kcp);
            FileInputStream trustKey = new FileInputStream(tcp);//客户端信任证书列表，即服务端证书

            SSLContext ctx = SSLContext.getInstance("SSL");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            KeyStore ks = KeyStore.getInstance("JKS");
            KeyStore tks = KeyStore.getInstance("JKS");

            ks.load(clientPrivateKey, clientKeyPassword.toCharArray());
            tks.load(trustKey, trustKeyPassword.toCharArray());

            kmf.init(ks, clientKeyPassword.toCharArray());
            tmf.init(tks);

            ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            return (Socket) ctx.getSocketFactory().createSocket(serverHost, serverPort);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    protected List doCanProcess(ByteDataBuffer obj) throws Exception
    {
        List list = new ArrayList();
        Map bodyMap;
        Map headMap;
        ByteDataBuffer databuf = obj;
        String sourceReceivedStr = new String(databuf.getBytes());
        System.out.println("原始报文为:" + sourceReceivedStr);
        System.out.println("字节数:" + sourceReceivedStr.length());
        System.out.println("报文转为16进制: \n" + HexStringUtils.toHex(sourceReceivedStr));
        databuf.setEncoding(ENCODING);
        databuf.setInBigEndian(false);
        int totalLen = 0; // 长度
        byte sign = databuf.readInt8();
        if (sign != BLOCK_START_SIGN)
        {
            System.out.println("无法找到起始标记!");
        }

        totalLen = databuf.readInt32();
        databuf.readString(6);
        byte[] dataBytes = new byte[totalLen];
        databuf.readBytes(dataBytes);
        String message = new String(dataBytes);
        System.out.println("原始返回报文为：\n" + message + "\n");
        // 报文是json格式，把json报文转换成Map类型的
        Map messageMap = JSON.parseObject(message, Map.class);
        bodyMap = (Map) messageMap.get("body");
        headMap = (Map) messageMap.get("head");
        list.add(headMap);
        list.add(bodyMap);
        return list;
    }

}
