package com.admin;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.util.DbOp;
import com.util.ServerTemplate;
/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 * @author SITO3
 *
 */
public class Server 
{
	private DbOp dbo=null;
	private int port=4466;
	private ServerTemplate<Integer> st=null;
	private static Logger logger=null;
	
	public Server()
	{
		try {
			logger = LogManager.getLogger(Server.class.getName());
			dbo=new DbOp(logger);
			st=new ServerTemplate<Integer>(ServerTemplate.ACCEPT_SINGLE_CONNECTION,logger);
			st.setServerPort(port);
			st.setHandlers(new LoggingHandler(LogLevel.INFO));
			st.setChildHandlers(new ServerInitializer(logger,dbo));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void start()
	{
		if (st!=null)
			st.start();
	}
	public void stop() throws Exception
	{
		if (st!=null)
			st.stop();
		if (dbo!=null)
			dbo.close();
	}
	public static void main(String[] args) 
	{
		Server s=new Server();
		s.start();
		
	}

}
