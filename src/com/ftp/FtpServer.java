package com.ftp;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.admin.Server;
import com.admin.ServerInitializer;
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
 * @param <T>
 * @param <T>
 *
 */
public class FtpServer<T>
{
	private DbOp dbo=null;
	private int port;
	private ServerTemplate<T> st=null;
	private static Logger logger=null;
	private String description=null;
	
	public FtpServer(String jsonString) 
	{
		description=jsonString;
		/*try {
			logger = LogManager.getLogger(Server.class.getName());
			dbo=new DbOp(logger);
			st=new ServerTemplate<T>(ServerTemplate.ACCEPT_SINGLE_CONNECTION,logger);
			st.setServerPort(port);
			st.setHandlers(new LoggingHandler(LogLevel.INFO));
			st.setChildHandlers(new ServerInitializer(logger,dbo));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}*/
	}
	public int getPort() {
		return port;
	}
	public String getDescription() {
		return description;
	}
}
