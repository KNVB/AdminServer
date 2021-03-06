package com.myftpserver.admin.handler;

import org.apache.logging.log4j.Logger;

import com.myftpserver.admin.AdminServer;
import com.myftpserver.admin.util.Utility;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
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
public class SessionHandler extends SimpleChannelInboundHandler<String>
{
	private Logger logger;
	private String remoteIp;
	private AdminServer<?> adminServer;
	public SessionHandler(AdminServer<?> adminServer, String remoteIp) 
	{
		this.remoteIp=remoteIp;
		this.adminServer=adminServer;
		this.logger=this.adminServer.getLogger();
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
		Utility.sendMessageToClient(ctx.channel(),logger,remoteIp,"220");
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception 
	{
		logger.debug("server receive:"+msg+"|");
		
	}
	/**
	 * Calls ChannelHandlerContext.fireExceptionCaught(Throwable) to forward to the next ChannelHandler in the ChannelPipeline. Sub-classes may override this method to change behavior.
	 * @param ctx the channel that user input command
	 * @param cause the exception cause  
	 */
	public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) 
    {
		logger.debug(cause.getMessage());
    }
	/**
	 * Close the admin session
	 */
	public void close()
	{
		/*ch.close();
		ch=null;*/		
	}
}
