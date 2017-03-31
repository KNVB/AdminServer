package com.myftpserver.admin;

import com.myftpserver.admin.util.Utility;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class SessionHandler extends SimpleChannelInboundHandler<String>
{

	public SessionHandler(AdminServer<Integer> adminServer, String remoteIp) 
	{
		// TODO Auto-generated constructor stub
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx)
	{
	
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String arg1) throws Exception 
	{
		// TODO Auto-generated method stub
		
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
