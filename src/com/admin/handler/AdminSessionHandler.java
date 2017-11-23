package com.admin.handler;
import com.util.*;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;




import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
@Sharable
public class AdminSessionHandler extends SimpleChannelInboundHandler<WebSocketFrame>
{
	private boolean isFirstConnect=true;
	private static Logger logger=null;
	private String returnEncoder=new String();
	private String responseString,request;
	private RSA myRSA;
	private JSONObject requestObj=null;
	public AdminSessionHandler(Logger logger)
	{
		
		this.logger=logger;

	}
	@Override
	public void channelActive(ChannelHandlerContext ctx)
            throws java.lang.Exception
    {
		try 
		{
		   myRSA = new RSA(1024);
		   returnEncoder ="function Encoder(){\n";
	       returnEncoder+="setMaxDigits(130);\n";
	       returnEncoder+="key = new RSAKeyPair(\""+myRSA.getPublicExponent()+"\",\"\",\""+myRSA.getPublicModulus()+"\");\n";
	       returnEncoder+="}\n";	
	       returnEncoder+="Encoder.prototype.encode=function(data)\n";
	       returnEncoder+="{\n";
	       returnEncoder+="		 return encryptedString(key,data);\n";
	       returnEncoder+="}\n";							 							 
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
				ctx.channel().writeAndFlush(new TextWebSocketFrame(returnEncoder));
				isFirstConnect=false;
			}
			else
			{
				throw new UnsupportedOperationException("Invalid action specified!");
			}
        }
        else
        {                
        	request=Utility.unescape(new String(myRSA.decode(Utility.hexStringToByteArray(request))));
        	requestObj=new JSONObject(request);
        	logger.debug("Request action:{}",requestObj.get("action"));
        	Response actionResponse=new Response();
			actionResponse.setAction((String)requestObj.get("action"));	
        	switch ((String)requestObj.get("action"))
        	{
	        	case "LOGIN":actionResponse.setResponseCode(0);
	        				 actionResponse.setReturnMessage("Login success");
	        				 responseString=(new JSONObject(actionResponse)).toString();
	        				break;
        	}
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
