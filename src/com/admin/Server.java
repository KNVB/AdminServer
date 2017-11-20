package com.admin;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.util.ServerTemplate;




public class Server 
{
	private int port=4466;
	private ServerTemplate st=null;
	private static Logger logger=null;
	
	public Server()
	{
		logger = LogManager.getLogger(Server.class.getName());
		st=new ServerTemplate(ServerTemplate.ACCEPT_SINGLE_CONNECTION,logger);
		st.setServerPort(port);
	}
	public void start()
	{
		if (st!=null)
			st.start();
	}
	public void stop()
	{
		if (st!=null)
			st.stop();
	}
	public static void main(String[] args) 
	{
		
		
	}

}
