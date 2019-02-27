 # socket/sslSocket + Netty/原生socket 整合的Demo,可任意切换
 # CSDN地址:
**关于项目中使用的4个证书，使用Java自带的keytool命令，在命令行生成：**
    _1、生成服务器端私钥kserver.keystore文件_
        `keytool -genkey -alias serverkey -validity 1 -keystore kserver.keystore`
    _2、根据私钥，导出服务器端安全证书_
        `keytool -export -alias serverkey -keystore kserver.keystore -file server.crt` 
    _3、将服务器端证书，导入到客户端的Trust KeyStore中_
        `keytool -import -alias serverkey -file server.crt -keystore tclient.keystore`
    _4、生成客户端私钥kclient.keystore文件_
        `keytool -genkey -alias clientkey -validity 1  -keystore kclient.keystore`
    _5、根据私钥，导出客户端安全证书_
        `keytool -export -alias clientkey -keystore kclient.keystore -file client.crt`
    _6、将客户端证书，导入到服务器端的Trust KeyStore中_
        `keytool -import -alias clientkey -file client.crt -keystore tserver.keystore`
**生成的文件分成两组，服务器端保存：kserver.keystore tserver.keystore 客户端保存：kclient.keystore  tclient.kyestore**