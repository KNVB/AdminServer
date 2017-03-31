package com.myftpserver;

import com.myftpserver.admin.util.MyServer;

public class FtpServer<T> extends MyServer<T> 
{
	public FtpServer(String config_json) 
	{
		super(MyServer.ACCEPT_MULTI_CONNECTION);
	}
}
