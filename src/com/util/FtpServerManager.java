package com.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.ftp.FtpServer;

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
