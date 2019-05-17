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
	public int addFtpServer(FtpServerInfo ftpServerInfo) throws AdminServerException
	{
		int result=-1;
		result = dbo.addFtpServer(ftpServerInfo);
		return result;
	}
	public int delFtpServer(String ftpServerId)
	{
		int result=-1;
		try 
		{
			result = dbo.delFtpServer(ftpServerId);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	public <T> int updateFtpServerInfo(FtpServerInfo ftpServerInfo)throws AdminServerException
	{
		int result=-1;
		result = dbo.updateFtpServerInfo(ftpServerInfo);
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
