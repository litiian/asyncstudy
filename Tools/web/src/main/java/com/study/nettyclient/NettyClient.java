package com.study.nettyclient;
import java.io.IOException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
class NettyClient {
	
    public static void main(String[] args) throws InterruptedException, IOException {
    	String host="localhost";//服务端ip
    	int port =8080;//服务端端口    	
    	EventLoopGroup workerGroup=new NioEventLoopGroup();
    	
    	try{
    		Bootstrap b=new Bootstrap();
    		b.group(workerGroup);
    		b.channel(NioSocketChannel.class);
    		b.option(ChannelOption.SO_KEEPALIVE, true);
    		b.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					//没有解码器的情况线下默认接受和发送ByteBuf
					ch.pipeline().addLast(new NettyClientHandler());
				}});
    		
    		//启动客户端
    		ChannelFuture f=b.connect(host,port).sync();
    		//等待链接关闭
    		f.channel().closeFuture().sync();
    	}finally {
    		workerGroup.shutdownGracefully();
    	}
    	
	}
    
    public static void star() throws IOException{
       
   }
}
