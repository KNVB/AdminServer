package com.myftpserver.admin;
import com.myftpserver.FtpServer;
import com.myftpserver.admin.util.*;
import com.myftpserver.admin.listeners.ServerBindListener;



import io.netty.channel.ChannelFutureListener;

import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
public class AdminServer<T> extends MyServer<T>
{
	private DbOp dbo;
	private Logger logger;
	private List<FtpServer> serverList=null;
	private static int connectionCount=0;
	public AdminServer(Logger logger) 
	{
		super(MyServer.ACCEPT_SINGLE_CONNECTION);
		try 
		{
			this.logger=logger;
			dbo=new DbOp(logger);
			serverList=dbo.getServerList();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	/**
	 * Check whether the concurrent connection is over the limit
	 * @return true when the concurrent connection is over the limit
	 */
	public synchronized boolean isOverConnectionLimit()
	{
		if (connectionCount<1)
		{	
			connectionCount++;
			return false;
		}
		else			
			return true;
	}
	public void start(ChannelFutureListener bindListener)
	{
		super.start(bindListener);
	}
	/**
	 * Stop the server
	 */
	public void stop()
	{
		super.stop();
		logger.debug("Server shutdown gracefully.");
	}
//-------------------------------------------------------------------------------------------	
	public static void main(String[] args)  
	{
		Scanner scanIn = new Scanner(System.in);
		Logger logger = LogManager.getLogger(AdminServer.class.getName());
		AdminServer<Integer> adminServer=new AdminServer<Integer>(logger);
		adminServer.setServerPort(4466);
		adminServer.setChildHandlers(new CommandChannelInitializer(adminServer,logger));
		adminServer.start(new ServerBindListener(logger,adminServer));
		scanIn.nextInt();
		adminServer.stop();
	}
}