package com.myftpserver.admin;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.Logger;

import com.myftpserver.admin.AdminServer;
import com.myftpserver.admin.handler.CommandChannelTimeoutHandler;
import com.myftpserver.admin.listeners.SessionClosureListener;
import com.myftpserver.admin.util.Utility;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
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
public class CommandChannelInitializer extends ChannelInitializer<Channel>
{
	private Logger logger;
	private AdminServer<Integer> adminServer;
	public CommandChannelInitializer(AdminServer<Integer> t, Logger logger)
	{
		this.adminServer=t;
		this.logger=logger;
	}
	@Override
	protected void initChannel(Channel ch) throws Exception 
	{
		String remoteIp=(((InetSocketAddress) ch.remoteAddress()).getAddress().getHostAddress());
		if (adminServer.isOverConnectionLimit())
		{
			Utility.disconnectFromClient(ch,logger,remoteIp,"Too many users, server is full");
		}
		else
		{
			ch.closeFuture().addListener(new SessionClosureListener(null,ch,logger,remoteIp,"bye bye"));
			ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(30, 30, 30));
			ch.pipeline().addLast("CommandChannelTimeoutHandler", new CommandChannelTimeoutHandler(logger,remoteIp));
			ch.pipeline().addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
			ch.pipeline().addLast("frameDecoder",new LineBasedFrameDecoder(1024));
			ch.pipeline().addLast("adminSessionHandler",new SessionHandler(adminServer,remoteIp));
		}
	}

}
