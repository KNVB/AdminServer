package com.admin;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.Logger;

import com.admin.handler.AdminChannelTimeoutHandler;
import com.admin.handler.AdminSessionHandler;




import com.admin.listeners.AdminChannelClosureListener;
import com.util.DbOp;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleStateHandler;
/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 * @author SITO3
 *
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> 
{
	private Server adminServer=null;
	private static final String WEBSOCKET_PATH = "/websocket";
	public ServerInitializer(Server server) 
	{
		this.adminServer=server;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception 
	{
		ChannelPipeline pipeline = ch.pipeline();
		String remoteIp=(((InetSocketAddress) ch.remoteAddress()).getAddress().getHostAddress());
		ch.closeFuture().addListener(new AdminChannelClosureListener(adminServer.getLogger(),remoteIp));
		pipeline.addLast("CommandChannelTimeoutHandler", new AdminChannelTimeoutHandler(adminServer.getLogger(),remoteIp));
		pipeline.addLast("idleStateHandler", new IdleStateHandler(30, 30, 30));
		pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new AdminSessionHandler(adminServer));
	}
}
