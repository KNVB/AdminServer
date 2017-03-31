package com.myftpserver.admin;

import java.net.InetSocketAddress;

import org.apache.logging.log4j.Logger;

import com.myftpserver.admin.AdminServer;
import com.myftpserver.admin.listeners.SessionClosureListener;
import com.myftpserver.admin.util.Utility;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LineBasedFrameDecoder;

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
			ch.pipeline().addLast("frameDecoder",new LineBasedFrameDecoder(1024));
			ch.pipeline().addLast("adminSessionHandler",new SessionHandler(adminServer,remoteIp));
		}
	}

}
