package com.lance.api.business.socketserver.netty;
// 记录调用方法的元信息的类

import com.lance.api.business.pojo.model.EntryModel;
import com.lance.api.business.pojo.model.ReturnModel;
import com.lance.api.business.util.DealSocket;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * netty业务处理类
 *
 * @author lance
 */
@Component
@Sharable
@Slf4j
public class ServerHandler extends ChannelInboundHandlerAdapter
{
    @Autowired
    private DealSocket dealSocket;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
    {
        EntryModel entryModel = (EntryModel) msg;

        String servCode = entryModel.getHeadModel().getServCode();
        String msgId = entryModel.getHeadModel().getMsgId();

        // do business
        MDC.put("logId", msgId);
        ReturnModel result = dealSocket.doBusiness(servCode, msgId, entryModel);
        MDC.clear();
        ctx.writeAndFlush(result);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception
    {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}