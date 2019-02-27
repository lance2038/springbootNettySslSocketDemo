package com.lance.api.common.util;


import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

/**
 * sftp连接工具
 *
 * @author lance
 */
@Slf4j
public class SftpUtil
{
    private ChannelSftp sftp = null;
    private Channel channel = null;
    private Session session = null;

    public SftpUtil()
    {
    }

    public void connect(String host, int port, String userName, String password, String path) throws Exception
    {
        this.connectServer(host, port, userName, password, path);
    }

    private boolean connectServer(String hostName, int port, String userName, String password, String path) throws Exception
    {
        try
        {
            JSch jSch = new JSch();
            this.session = jSch.getSession(userName, hostName, port);
            this.session.setPassword(password);
            this.session.setConfig(this.getSshConfig());
            this.session.connect();
            this.channel = this.session.openChannel("sftp");
            this.channel.connect();
            this.sftp = (ChannelSftp) this.channel;
            if (path.length() != 0)
            {
                this.sftp.cd(path);
            }

            return true;
        }
        catch (JSchException e)
        {
            log.info("SSH方式连接FTP服务器时有JSchException异常!");
            e.printStackTrace();
            throw e;
        }
    }

    private Properties getSshConfig() throws Exception
    {
        Properties sshConfig = null;

        try
        {
            sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            return sshConfig;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public boolean downloadFile(String remoteFilename, String localFilename) throws SftpException, IOException, Exception
    {
        FileOutputStream output = null;
        boolean success = false;

        boolean result;
        try
        {
            File localFile = new File(localFilename);
            if (!localFile.exists())
            {
                log.info("本地不存在待下载文件: " + localFilename + " ,将创建一个新文件!");
                localFile.createNewFile();
            }

            output = new FileOutputStream(localFile);
            this.sftp.get(remoteFilename, output);
            success = true;
            log.info("成功接收文件,本地路径：" + localFilename);
            return success;
        }
        catch (SftpException e)
        {
            log.info("接收文件时有SftpException异常!");
            e.printStackTrace();
            result = success;
        }
        catch (IOException e)
        {
            log.info("接收文件时有I/O异常!");
            e.printStackTrace();
            result = success;
        }
        finally
        {
            try
            {
                if (null != output)
                {
                    output.close();
                }

                this.disconnect();
            }
            catch (IOException e)
            {
                log.info("关闭文件时出错!");
                e.printStackTrace();
            }

        }
        return result;
    }

    public boolean uploadFile(String remoteFilename, String localFileName) throws SftpException, Exception
    {
        boolean success = false;
        FileInputStream fis = null;

        try
        {
            File localFile = new File(localFileName);
            fis = new FileInputStream(localFile);
            this.sftp.put(fis, remoteFilename);
            success = true;
            log.info("成功发送文件,本地路径：" + localFileName);
        }
        catch (SftpException e)
        {
            log.info("发送文件时有SftpException异常!");
            e.printStackTrace();
            throw e;
        }
        catch (Exception e)
        {
            log.info("发送文件时有异常!");
            e.printStackTrace();
            throw e;
        }
        finally
        {
            try
            {
                if (null != fis)
                {
                    fis.close();
                }

                this.disconnect();
            }
            catch (IOException e)
            {
                log.info("关闭文件时出错!");
                e.printStackTrace();
            }

        }

        return success;
    }

    public boolean uploadFile(String remotePath, String remoteFilename, InputStream input) throws Exception
    {
        boolean success = false;

        try
        {
            if (null != remotePath && remotePath.trim() != "")
            {
                this.sftp.cd(remotePath);
            }

            this.sftp.put(input, remoteFilename);
            success = true;
        }
        catch (SftpException e)
        {
            log.info("发送文件时有SftpException异常!");
            e.printStackTrace();
            log.info(e.getMessage());
            throw e;
        }
        catch (Exception e)
        {
            log.info("发送文件时有异常!");
            e.printStackTrace();
            throw e;
        }
        finally
        {
            try
            {
                if (null != input)
                {
                    input.close();
                }

                this.disconnect();
            }
            catch (IOException e)
            {
                log.info("关闭文件时出错!");
                e.printStackTrace();
            }

        }

        return success;
    }

    public boolean deleteFile(String remoteFilename) throws Exception
    {
        boolean success = false;

        boolean result;
        try
        {
            this.sftp.rm(remoteFilename);
            log.info("删除远程文件" + remoteFilename + "成功!");
            success = true;
            return success;
        }
        catch (SftpException e)
        {
            log.info("删除文件时有SftpException异常!");
            e.printStackTrace();
            log.info(e.getMessage());
            result = success;
        }
        catch (Exception e)
        {
            log.info("删除文件时有异常!");
            log.info(e.getMessage());
            result = success;
        }
        finally
        {
            this.disconnect();
        }
        return result;
    }

    public List<ChannelSftp.LsEntry> listFiles(String directory) throws Exception
    {
        Vector list = null;

        try
        {
            list = this.sftp.ls(directory);
        }
        catch (SftpException e)
        {
            throw e;
        }
        finally
        {
            this.disconnect();
        }

        return list;
    }

    private void disconnect() throws Exception
    {
        try
        {
            if (this.sftp.isConnected())
            {
                this.sftp.disconnect();
            }

            if (this.channel.isConnected())
            {
                this.channel.disconnect();
            }

            if (this.session.isConnected())
            {
                this.session.disconnect();
            }

        }
        catch (Exception e)
        {
            throw e;
        }
    }
}
