package com.admin.handler;

import com.admin.Server;
import com.ftp.FtpServer;
import com.util.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;


@Sharable
public class AdminSessionHandler<T> extends SimpleChannelInboundHandler<WebSocketFrame>
{
	private boolean isFirstConnect=true;
	private JSONObject requestObj=null;
	private Logger logger=null;
	private MessageCoder messageCoder;
	
	private String returnCoder=new String();
	private String responseString,requestString;
	private Server adminServer=null;
	private FtpServerManager ftpServerManager=null;  
	private AdminUserManager adminUserManager=null;
	public AdminSessionHandler(Server adminServer)
	{
		this.adminServer=adminServer;
		this.logger=adminServer.getLogger();
		this.adminUserManager=adminServer.getAdminUserManager();
		this.ftpServerManager=adminServer.getFtpServerManager();
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx)
            throws java.lang.Exception
    {
		try 
		{
			messageCoder=new MessageCoder();
			logger.debug("messageKey={}",messageCoder.key);
			logger.debug("ivText={}",messageCoder.ivText);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
    }
	@Override
	public void channelInactive(ChannelHandlerContext ctx)
            throws java.lang.Exception
    {
		logger.info("Channel Inactivated");
		
    }
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame)
			throws Exception 
	{
		logger.info("Data Received");
		if (frame instanceof TextWebSocketFrame) {
			 handleRequest(ctx, frame);
	     } else {
	         String message = "unsupported frame type: " + frame.getClass().getName();
	         throw new UnsupportedOperationException(message);
	     }	
	}
	private void handleRequest(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception
	{
        requestString = ((TextWebSocketFrame) frame).text();
        logger.debug("{} received {}", ctx.channel(),requestString);
        if (isFirstConnect)
        {
        	KeyCoder keyCoder=new KeyCoder(requestString);
        	responseString ="{\"messageKey\":\""+messageCoder.key+"\",";
    		responseString+="\"ivText\":\""+messageCoder.ivText+"\"}";
    		responseString =keyCoder.encode(responseString);
    		ctx.channel().writeAndFlush(new TextWebSocketFrame(responseString));
    		isFirstConnect=false;
        }
        else
        {
        	requestObj=new JSONObject(messageCoder.decode(requestString));
        	logger.debug("Request action:{}",requestObj.get("action"));
        	Response actionResponse=new Response();
			actionResponse.setAction((String)requestObj.get("action"));
        	switch ((String)requestObj.get("action"))
        	{
	        	case "LOGIN":
    					String userName=(String)requestObj.get("userName");
    					String password=(String)requestObj.get("password");
    					logger.debug("user name={},password={}",userName,password);
    					if (adminUserManager.login(userName, password))
        				{
        					actionResponse.setResponseCode(0);
        					actionResponse.setReturnMessage("Login success");
        				}
    					else
    					{
        					actionResponse.setResponseCode(-1);
        					actionResponse.setReturnMessage("Login failure");
    					}
    					break;
	        	case "GetServerList":
	        			ArrayList<FtpServer<T>> ftpServerList=ftpServerManager.getAllServerList();
	        			actionResponse.setResponseCode(0);
	        			//ftpServerList.add(new FtpServer<T>("abc"));
	        			actionResponse.setReturnObjects("ftpServerList",ftpServerList);
	        			break;
	        	case "GetAddServerPageData":
	        			ArrayList<String> localIpList=Utility.getAllLocalIp();
	        			actionResponse.setResponseCode(0);
	        			actionResponse.setReturnObjects("localIpList",localIpList);
	        			break;
        	}
        	responseString=(new JSONObject(actionResponse)).toString();
        	logger.debug("responseString={}",responseString);
        	responseString=messageCoder.encode(responseString);        	
        	ctx.channel().writeAndFlush(new TextWebSocketFrame(responseString));
        }
	}
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) 
	{
        cause.printStackTrace();
        ctx.close();
    }
}
