package com.admin;

import org.apache.logging.log4j.Logger;

import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;


public class ServerInitializer extends ChannelInitializer<SocketChannel> 
{
	private final SslContext sslCtx;
	private static Logger logger=null;
	private static final String WEBSOCKET_PATH = "/websocket";
	public ServerInitializer(Logger logger,SslContext sslCtx) 
	{
		this.logger=logger;
		this.sslCtx=sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception 
	{
		ChannelPipeline pipeline = ch.pipeline();
		/*SslHandler handler = sslCtx.newHandler(ch.alloc());

		pipeline.addLast("ssl",handler);*/
		pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new AdminSessionHandler(logger));
	}
}
