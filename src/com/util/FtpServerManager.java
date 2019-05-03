package com.util;
import com.ftp.FtpServer;
import com.ftp.FtpServerInfo;

import java.sql.SQLException;
import java.util.TreeMap;
import org.apache.logging.log4j.Logger;

public class FtpServerManager 
{
	DbOp dbo=null;
	Logger logger=null;
	public FtpServerManager(DbOp dbo,Logger logger) throws Exception
	{
		this.dbo=dbo;
		this.logger=logger;
	}
	public <T> int addFtpServer(FtpServer<T> ftpServer)
	{
		int result=-1;
		try 
		{
			result = dbo.addFtpServer(ftpServer);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	public TreeMap<String,FtpServerInfo>  getAllServerList()
	{
		TreeMap<String,FtpServerInfo> serverList=dbo.getAllServerList();
		return serverList;
	}
	public FtpServerInfo getFtpServerInfo(String serverId)
	{
		FtpServerInfo ftpServerInfo=dbo.getFtpServerInfo(serverId);
		return ftpServerInfo;
	}
}
