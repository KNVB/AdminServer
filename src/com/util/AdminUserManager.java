package com.util;

import org.apache.logging.log4j.Logger;

public class AdminUserManager 
{
	DbOp dbo=null;
	public AdminUserManager(Logger logger) throws Exception
	{
		dbo=new DbOp(logger);
	}
	public boolean login(String userName,String password)
	{
		return dbo.login( userName, password);
	}
}
