package com.util;
import com.admin.adminObj.FtpServerInfo;
import com.ftp.FtpServer;
import java.util.ArrayList;
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
	public TreeMap<String,FtpServerInfo>  getAllServerList()
	{
		TreeMap<String,FtpServerInfo> serverList=dbo.getAllServerList();
		return serverList;
	}	
}
