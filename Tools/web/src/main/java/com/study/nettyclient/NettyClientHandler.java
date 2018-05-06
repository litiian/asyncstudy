package com.study.nettyclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter  {
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("开始读数据");
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("开始写数据");
		
		//发送ByteBuf数据
		String data="232301FE4C4B4A54424B425930474630303932373502001E120504120E06000530303030304130303030303536313531453833450100A1";
		ByteBuf heapBuffer = Unpooled.buffer(data.length());    
		heapBuffer.writeBytes(data.getBytes()); 
		ctx.writeAndFlush(heapBuffer);
	}
//    //
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//    	System.out.println("开始发送数据");
//         String msg = "Are you ok?";  
//         ByteBuf encoded = ctx.alloc().buffer(4 * msg.length());  
//         encoded.writeBytes(msg.getBytes());  
//         ctx.write(encoded);  
//         ctx.flush();  
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        System.out.println("连接关闭! ");
//        super.channelInactive(ctx);
//    }
//
//	@Override
//	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//			System.out.println("接收到服务端返回的数据");
//		    ByteBuf result = (ByteBuf) msg;  
//	        byte[] result1 = new byte[result.readableBytes()];  
//	        result.readBytes(result1);  
//	        System.out.println("Server said:" + new String(result1));  
//	        result.release();  
//	}
//	@Override
//	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("完成" );
//	}
}
