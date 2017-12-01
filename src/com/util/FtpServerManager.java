package com.util;
import com.ftp.FtpServer;
import java.util.ArrayList;
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
	public <T> ArrayList<FtpServer<T>> getAllServerList()
	{
		ArrayList<FtpServer<T>> serverList=dbo.getAllServerList();
		return serverList;
	}	
}
