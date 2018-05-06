package com.study.nettyserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class NettyServerHandler extends  ChannelInboundHandlerAdapter  {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		//没有解码器的情况线下默认接受和发送ByteBuf
	    ByteBuf in = (ByteBuf) msg;
	    try {
	        while (in.isReadable()) { // (1)
	            System.out.print((char) in.readByte());
	            System.out.flush();
	        }
	        String data="成功";
	       //没有解码器的情况线下默认接受和发送ByteBuf
			ByteBuf heapBuffer = Unpooled.buffer(data.length());    
			heapBuffer.writeBytes(data.getBytes()); 
	        ctx.writeAndFlush(heapBuffer);
	    } finally {
	        ReferenceCountUtil.release(msg); // (2)
	    }
	}
}
