package com.admin.handler;

import com.admin.Server;
import com.admin.adminObj.AccessRightEntry;
import com.admin.adminObj.BindingAddress;
import com.admin.adminObj.FtpAdminUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftp.FtpServer;
import com.ftp.FtpServerInfo;
import com.ftp.FtpUserInfo;
import com.util.*;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;


@Sharable
public class AdminSessionHandler<T> extends SimpleChannelInboundHandler<WebSocketFrame>
{
	private boolean isFirstConnect=true;
	private boolean isLoginSuccess=false;
	
	private Logger logger=null;
	private Login login=null;
	private MessageCoder messageCoder;
	private Server adminServer=null;
	private String ftpServerInfoString=new String(),ftpServerId=new String();
	private String responseString,returnCoder=new String(),requestString,actionString;
//	private FtpServer<T>ftpServer=null; 
	private FtpServerInfo ftpServerInfo=null;
	private FtpServerManager ftpServerManager=null;  
	private AdminUserManager adminUserManager=null;
	private ObjectMapper objectMapper = new ObjectMapper();
	private JSONObject requestObj;
	private JSONObject ftpServerObject;
	private Response actionResponse;
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
        String errorMessage;
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
        	requestString=messageCoder.decode(requestString);
        	logger.debug(requestString);
        	requestObj=new JSONObject(requestString);
        	
    		Response actionResponse=new Response();
        	if (isLoginSuccess)
        	{
        		actionString=(String)requestObj.get("action");
        		actionResponse.setAction(actionString);
        		switch (actionString)
        		{
        			case "AddFtpServer":
        					ftpServerObject=requestObj.getJSONObject("ObjectMap").getJSONObject("ftpServerInfo");
        					ftpServerInfoString=ftpServerObject.toString();
        				
							logger.debug(ftpServerInfoString);
							ftpServerInfo=objectMapper.readValue(ftpServerInfoString, FtpServerInfo.class);
							ftpServerInfo.setServerId(Utility.getUniqueId());
							try
							{
								actionResponse.setResponseCode(ftpServerManager.addFtpServer(ftpServerInfo));
								actionResponse.setReturnObjects("ftpServerId",ftpServerInfo.getServerId());
							}
							catch (AdminServerException err)
							{
								errorMessage=err.getMessage();
								String []temp=errorMessage.split(",");
								actionResponse.setResponseCode(Integer.parseInt(temp[1]));
								actionResponse.setReturnMessage(temp[0]);
							}
							break;
        			case "DelFTPServer":
        					ftpServerId=requestObj.getJSONObject("ObjectMap").getString("ftpServerId");
        					actionResponse.setResponseCode(ftpServerManager.delFtpServer(ftpServerId));
							actionResponse.setReturnObjects("ftpServerId",ftpServerId);
        					break;
					case "GetAdminUserList":
							TreeMap<String,FtpAdminUserInfo>adminUserList=adminUserManager.getAdminUserList();
							actionResponse.setResponseCode(0);
							actionResponse.setReturnObjects("adminUserList",adminUserList);
							break;
					case "GetRemoteDir":
							ArrayList<FileSystemObject> dirList=Utility.getSubFolderOnly((String)requestObj.get("physicalDir"));
							actionResponse.setResponseCode(0);
							actionResponse.setReturnObjects("dirList",dirList);
							actionResponse.setReturnObjects("userId",(String)requestObj.get("userId"));
							actionResponse.setReturnObjects("accessRightId",(String)requestObj.get("userId"));
							break;
					case "GetFTPServerInfo":
							String serverId=requestObj.getJSONObject("ObjectMap").getString("serverId");
							ftpServerInfo=ftpServerManager.getFtpServerInfo(serverId);
							if (ftpServerInfo==null)
							{	
								actionResponse.setResponseCode(-1);
								actionResponse.setReturnObjects("ftpServerInfo",null);
							}
							else
							{	
								actionResponse.setResponseCode(0);
								actionResponse.setReturnObjects("ftpServerInfo",ftpServerInfo);
							}
							break;
					case "GetFTPServerList":
							TreeMap<String,FtpServerInfo>  ftpServerList=ftpServerManager.getAllServerList();
							actionResponse.setResponseCode(0);
							//ftpServerList.add(new FtpServer<T>("abc"));
							actionResponse.setReturnObjects("ftpServerList",ftpServerList);
							break;
					case "GetUniqueId":
							String uniqueId=Utility.getUniqueId();
							actionResponse.setResponseCode(0);
							actionResponse.setReturnObjects("uniqueId", uniqueId);
							break;
					/*
					 case "GetInitialFtpServerInfo":
		        			BindingAddress bindingAddress=new BindingAddress();
		        			FtpServerInfo ftpServerInfo=new FtpServerInfo();
		        			AccessRightEntry accessRightEntry=new AccessRightEntry();
		        			FtpUserInfo ftpUserInfo =new FtpUserInfo();
		        			ArrayList<String> localIpList=Utility.getAllLocalIp();
		        			TreeMap<String,FtpUserInfo> ftpUserInfoList=new TreeMap<String,FtpUserInfo>();
		        			ArrayList<BindingAddress>bindingAddressList=new ArrayList<BindingAddress>();
		        			TreeMap<String,AccessRightEntry> accessRightEntries=new TreeMap<String,AccessRightEntry>();
		        			
		        			bindingAddress.setBound(true);
		        			bindingAddress.setIpAddress("*");
		        			bindingAddressList.add(bindingAddress);
		        			
		        			for (String ipAddress : localIpList)
		        			{
		        				bindingAddress=new BindingAddress();
			        			bindingAddress.setBound(false);
			        			bindingAddress.setIpAddress(ipAddress);
			        			bindingAddressList.add(bindingAddress);
		        			}
		        			
		        			ftpUserInfo.setPassword("");
		        			ftpUserInfo.setUserName("anonymous");
		        			//accessRightEntry.setEntryId(Utility.getUniqueId());
		        			accessRightEntries.put(accessRightEntry.getEntryId(),accessRightEntry);
		        			ftpUserInfo.setAccessRightEntries(accessRightEntries);
		        			ftpUserInfoList.put(ftpUserInfo.getUserId(),ftpUserInfo);
		
		        			ftpUserInfo =new FtpUserInfo();
		        			ftpUserInfo.setUserId(Utility.getUniqueId());
		        			ftpUserInfo.setPassword("密碼");
		        			ftpUserInfo.setUserName("陳大文");
		        			accessRightEntries=new TreeMap<String,AccessRightEntry>();
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
		        			
		        			break;*/
					
					case "GetIPAddressList":
							ArrayList<String> localIpList=Utility.getAllLocalIp();
							actionResponse.setResponseCode(0);
							actionResponse.setReturnObjects("ipAddressList",localIpList);
							break;
					case "SaveFTPServerNetworkProperties":
	    					ftpServerObject=requestObj.getJSONObject("ObjectMap").getJSONObject("ftpServerInfo");
	    					ftpServerInfoString=ftpServerObject.toString();
					
	    					logger.debug(ftpServerInfoString);
	    					ftpServerInfo=objectMapper.readValue(ftpServerInfoString, FtpServerInfo.class);
	    					actionResponse.setResponseCode(ftpServerManager.updateFtpServerInfo(ftpServerInfo));
	    					break;
				}
				sendResponse(ctx,actionResponse);
        	}
        	else
        	{
        		login=objectMapper.readValue(requestString, Login.class);
        		logger.debug("user name={},password={}",login.userName,login.password);
        		boolean loginSuccess=adminUserManager.login(login.userName, login.password);
				if (loginSuccess)
				{
					isLoginSuccess=true;
					actionResponse.setResponseCode(0);
					actionResponse.setReturnMessage("Login success");
					actionResponse.setReturnObjects("ftpServerList",ftpServerManager.getAllServerList());
					actionResponse.setReturnObjects("ipAddressList",Utility.getAllLocalIp());
					sendResponse(ctx,actionResponse);
				}
				else
				{
					actionResponse.setResponseCode(-1);
					actionResponse.setReturnMessage("Login failure");
					sendResponse(ctx,actionResponse);
					ctx.close();
				}
        	}
        }
	}
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) 
	{
        cause.printStackTrace();
        ctx.close();
    }
	private void sendResponse(ChannelHandlerContext ctx,Response actionResponse) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, JsonProcessingException
	{
		responseString=objectMapper.writeValueAsString(actionResponse);
    	logger.debug("responseString={}",responseString);
    	responseString=messageCoder.encode(responseString);        	
    	ctx.channel().writeAndFlush(new TextWebSocketFrame(responseString));
	}
}
