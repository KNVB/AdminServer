package com.admin;


import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import com.util.ServerTemplate;




public class Server 
{
	private int port=4466;
	private ServerTemplate<Integer> st=null;
	private static Logger logger=null;
	
	public Server()
	{
		// Configure SSL.
        final SslContext sslCtx;
        SelfSignedCertificate ssc;
		try {
//			ssc = new SelfSignedCertificate();
//			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			logger = LogManager.getLogger(Server.class.getName());
			st=new ServerTemplate<Integer>(ServerTemplate.ACCEPT_SINGLE_CONNECTION,logger);
			st.setServerPort(port);
			st.setHandlers(new LoggingHandler(LogLevel.INFO));
			//st.setChildHandlers(new ServerInitializer(logger,sslCtx));
			st.setChildHandlers(new ServerInitializer(logger,null));
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
	public void stop()
	{
		if (st!=null)
			st.stop();
	}
	public static void main(String[] args) 
	{
		Server s=new Server();
		s.start();
		
	}

}
