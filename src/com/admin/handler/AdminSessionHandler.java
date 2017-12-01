package com.admin.handler;

import com.admin.Server;
import com.ftp.FtpServer;
import com.util.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.Logger;


@Sharable
public class AdminSessionHandler<T> extends SimpleChannelInboundHandler<WebSocketFrame>
{
	private boolean isFirstConnect=true;
	private JSONObject requestObj=null;
	private Logger logger=null;
	private MessageCoder messageCoder;
	private RSA keyCoder;
	private String returnCoder=new String();
	private String responseString,request;
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
			keyCoder = new RSA(1024);
			messageCoder=new MessageCoder();
			String messageKey=Utility.byteArrayToHexString(keyCoder.encode(messageCoder.key.getBytes()));
			String ivText=Utility.byteArrayToHexString(keyCoder.encode(messageCoder.ivText.getBytes()));
			logger.debug("messageKey={}",messageCoder.key);
			logger.debug("ivText={}",messageCoder.ivText);
			returnCoder="coder=new Coder(\""+messageKey+"\",\""+ivText+"\",\""+keyCoder.getPrivateExponent()+"\",\""+keyCoder.getPublicModulus()+"\");";
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
        request = ((TextWebSocketFrame) frame).text();
        logger.debug("{} received {}", ctx.channel(),request);
        
        if (request.equals("Hello"))
        {
			if (isFirstConnect)
			{	
				ctx.channel().writeAndFlush(new TextWebSocketFrame(returnCoder));
				isFirstConnect=false;
			}
			else
			{
				throw new UnsupportedOperationException("Invalid action specified!");
			}
        }
        else
        {                
        	request=messageCoder.decode(request);
        	logger.debug("Request String:{}",request);
      	    requestObj=new JSONObject(request);
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
    					//logger.debug((new JSONObject(actionResponse)).toString());
    					responseString=(new JSONObject(actionResponse)).toString();
    					break;
	        	case "GETServerList":
	        			ArrayList<FtpServer<T>> ftpServerList=ftpServerManager.getAllServerList();
	        			actionResponse.setResponseCode(0);
	        			ftpServerList.add(new FtpServer<T>("abc"));
	        			JSONObject obj=new JSONObject();
	        			obj.put("ftpServerList", ftpServerList);
	        			JSONArray jArray=obj.getJSONArray("ftpServerList");
	        			responseString=(new JSONObject(actionResponse)).toString();
	        			responseString=responseString.replace("\"returnMessage\":\"\"", "\"returnMessage\":"+jArray.toString());
	        			break;
        	}
        	
        	logger.debug("responseString={}",responseString);
        	//responseString=URLEncoder.encode(responseString,"UTF-8");
        	responseString=messageCoder.encode(responseString);        	
        	//responseString=Utility.byteArrayToHexString(myRSA.encode(responseString.getBytes()));
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
