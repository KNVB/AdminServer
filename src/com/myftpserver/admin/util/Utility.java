package com.myftpserver.admin.util;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.CharsetUtil;

import org.apache.logging.log4j.Logger;

import com.myftpserver.admin.listeners.CommandCompleteListener;
import com.myftpserver.admin.listeners.SessionClosureListener;

public class Utility 
{
	public Utility()
	{
		
	}
	/**
	 * It sends a response message to client
	 * @param ch ftp channel
	 * @param logger  message logger
	 * @param remoteIp client IP address 
	 * @param ftpMessage response message
	 */
	public static void sendMessageToClient(Channel ch, Logger logger,String remoteIp,String ftpMessage) 
	{
		ch.writeAndFlush(Unpooled.copiedBuffer(ftpMessage+"\r\n",CharsetUtil.UTF_8)).addListener(new CommandCompleteListener(logger,remoteIp,ftpMessage));
	}
	/**
	 * It sends a good bye message to client and then close a ftp command channel
	 * @param ch ftp command channel
	 * @param logger message logger
	 * @param remoteIp client IP address
	 * @param goodByeMessage Goodbye message
	 */
	public static void disconnectFromClient(Channel ch, Logger logger,String remoteIp,String goodByeMessage)
	{
		ch.writeAndFlush(Unpooled.copiedBuffer(goodByeMessage+"\r\n",CharsetUtil.UTF_8)).addListener(new SessionClosureListener(null,ch,logger,remoteIp,goodByeMessage));
	}
}
