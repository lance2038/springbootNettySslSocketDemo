package com.lance.api.business.socketserver.netty;

import com.lance.api.business.pojo.model.ReturnModel;
import com.lance.api.business.util.ComposeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 报文组装
 *
 * @author lance
 */
@Component
public class MessageEncoder extends MessageToByteEncoder<ReturnModel>
{
    @Autowired
    private ComposeUtil composeUtil;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ReturnModel returnModel, ByteBuf out) throws Exception
    {
        byte[] bytes = null;
        try
        {
            bytes = composeUtil.doCanProcess(returnModel.getMap(), returnModel.getServCode(), returnModel.getMsgId());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        out.writeBytes(bytes);
    }
}
