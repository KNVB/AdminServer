package com.admin.handler;

import com.admin.Server;
import com.admin.adminObj.AccessRightEntry;
import com.admin.adminObj.FtpServerInfo;
import com.admin.adminObj.FtpUserInfo;
import com.ftp.FtpServer;
import com.util.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONObject;
import org.apache.logging.log4j.Logger;


@Sharable
public class AdminSessionHandler<T> extends SimpleChannelInboundHandler<WebSocketFrame>
{
	private boolean isFirstConnect=true;
	private JSONObject requestObj=null;
	private Logger logger=null;
	private MessageCoder messageCoder;
	private Server adminServer=null;
	private String returnCoder=new String();
	private String responseString,requestString;
	
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
	        	case "Login":
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
	        	case "GetRemoteDir":
	        			ArrayList<FileSystemObject> dirList=Utility.getSubFolderOnly((String)requestObj.get("physicalDir"));
	        			actionResponse.setResponseCode(0);
	        			actionResponse.setReturnObjects("dirList",dirList);
	        			actionResponse.setReturnObjects("userId",(String)requestObj.get("userId"));
	        			actionResponse.setReturnObjects("accessRightId",(String)requestObj.get("accessRightId"));
	        			break;
	        	case "GetServerList":
	        			ArrayList<FtpServer<T>> ftpServerList=ftpServerManager.getAllServerList();
	        			actionResponse.setResponseCode(0);
	        			//ftpServerList.add(new FtpServer<T>("abc"));
	        			actionResponse.setReturnObjects("ftpServerList",ftpServerList);
	        			break;
	        	case "GetUniqueId":
	        			String uniqueId=Utility.getUniqueId();
	        			actionResponse.setResponseCode(0);
	        			actionResponse.setReturnObjects("uniqueId", uniqueId);
	        			break;
	        	case "GetInitialFtpServerInfo":
	        			FtpServerInfo ftpServerInfo=new FtpServerInfo();
	        			AccessRightEntry accessRightEntry=new AccessRightEntry();
	        			FtpUserInfo ftpUserInfo =new FtpUserInfo();
	        			ArrayList<String> localIpList=Utility.getAllLocalIp();
	        			Hashtable<String,FtpUserInfo> ftpUserInfoList=new Hashtable<String,FtpUserInfo>();
	        			Hashtable<String,Boolean>bindingAddressList=new Hashtable<String,Boolean>();
	        			Hashtable<String,AccessRightEntry> accessRightEntries=new Hashtable<String,AccessRightEntry>();
	        			
	        			bindingAddressList.put("*",false);
	        			for (String ipAddress : localIpList)
	        			{
	        				bindingAddressList.put(ipAddress,false);
	        			}
	        			ftpUserInfo.setPassword("");
	        			ftpUserInfo.setUserName("anonymous");
	        			accessRightEntry.setEntryId(Utility.getUniqueId());
	        			accessRightEntries.put(accessRightEntry.getEntryId(),accessRightEntry);
	        			ftpUserInfo.setAccessRightEntries(accessRightEntries);
	        			ftpUserInfoList.put(ftpUserInfo.getUserId(),ftpUserInfo);

	        			ftpUserInfo =new FtpUserInfo();
	        			ftpUserInfo.setUserId(Utility.getUniqueId());
	        			ftpUserInfo.setPassword("密碼");
	        			ftpUserInfo.setUserName("陳大文");
	        			accessRightEntries=new Hashtable<String,AccessRightEntry>();
	        			accessRightEntries.put(accessRightEntry.getEntryId(),accessRightEntry);
	        			accessRightEntry=new AccessRightEntry();
	        			accessRightEntry.setPhysicalDir("C:\\");
	        			accessRightEntry.setEntryId(Utility.getUniqueId());
	        			accessRightEntries.put(accessRightEntry.getEntryId(),accessRightEntry);
	        			ftpUserInfo.setAccessRightEntries(accessRightEntries);
	        			ftpUserInfoList.put(ftpUserInfo.getUserId(),ftpUserInfo);
	 
	        			ftpServerInfo.setServerId(Utility.getUniqueId());
	        			ftpServerInfo.setBindingAddresses(bindingAddressList);
	        			ftpServerInfo.setFtpUserInfoList(ftpUserInfoList);
	        			actionResponse.setResponseCode(0);
	        			actionResponse.setReturnObjects("ftpServerInfo",ftpServerInfo);      			
	        			
	        			break;
	        	/*		
	        	case "GetBindingAccess":
	        			ArrayList<String> localIpList=Utility.getAllLocalIp();
	        			actionResponse.setResponseCode(0);
	        			actionResponse.setReturnObjects("bindingIpList",localIpList);
	        			break;*/
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
