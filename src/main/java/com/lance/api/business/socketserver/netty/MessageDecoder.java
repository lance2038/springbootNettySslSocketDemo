package com.lance.api.business.socketserver.netty;

import com.lance.api.business.pojo.model.EntryModel;
import com.lance.api.business.util.ByteDataBuffer;
import com.lance.api.business.util.ComposeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 报文解码
 *
 * @author lance
 */
public class MessageDecoder extends ByteToMessageDecoder
{

    /**
     * 从ByteBuf中获取字节，转换成对象
     *
     * @param ctx
     * @param buffer
     * @param out
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception
    {
        int len = buffer.readableBytes();
        byte[] req = new byte[len];
        buffer.getBytes(0, req);

        ByteDataBuffer dataBuffer = new ByteDataBuffer(req);

        EntryModel entryModel = ComposeUtil.deSplit(dataBuffer);
        out.add(entryModel);
        buffer.skipBytes(len);
    }
}
