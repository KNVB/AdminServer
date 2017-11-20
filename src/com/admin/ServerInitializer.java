package com.admin;

import org.apache.logging.log4j.Logger;
import com.admin.handler.AdminSessionHandler;


import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;


public class ServerInitializer extends ChannelInitializer<SocketChannel> 
{
	private static Logger logger=null;
	private static final String WEBSOCKET_PATH = "/websocket";
	public ServerInitializer(Logger logger) 
	{
		this.logger=logger;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception 
	{
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new AdminSessionHandler(logger));
	}
}
