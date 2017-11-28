package com.util;

import org.apache.logging.log4j.Logger;

public class AdminUserManager 
{
	DbOp dbo=null;
	Logger logger=null;
	public AdminUserManager(DbOp dbo,Logger logger) throws Exception
	{
		this.dbo=dbo;
		this.logger=logger;
	}
	public boolean login(String userName,String password)
	{
		return dbo.adminLogin(userName, password);
	}	
}
