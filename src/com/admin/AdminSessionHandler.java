package com.admin;
import com.util.*;

import java.util.Locale;
import org.apache.logging.log4j.Logger;
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
	public AdminSessionHandler(Logger logger)
	{
		
		this.logger=logger;
		RSA myRSA;
		try {
			myRSA = new RSA(1024);
		   returnEncoder ="function Encoder(){\n";
	       returnEncoder+="setMaxDigits(130);\n";
	       returnEncoder+="key = new RSAKeyPair(\""+myRSA.getPublicExponent()+"\",\""+myRSA.getPublicModulus()+"\");\n";
	       returnEncoder+="}\n";	
	       returnEncoder+="Encoder.prototype.encode=function(data)\n";
	       returnEncoder+="{\n";
	       returnEncoder+="		 return encryptedString(key,data);\n";
	       returnEncoder+="}\n";							 							 
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void channelActive(ChannelHandlerContext ctx)
            throws java.lang.Exception
    {
		logger.info("Channel Activated");
		
    }
	@Override
	public void channelInactive(ChannelHandlerContext ctx)
            throws java.lang.Exception
    {
		logger.info("Channel In Activated");
		
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
	private void handleRequest(ChannelHandlerContext ctx, WebSocketFrame frame)
	{
		 // Send the uppercase string back.
        String request = ((TextWebSocketFrame) frame).text();
        logger.debug("{} received {}", ctx.channel(), request);
        if (isFirstConnect)
        {	
        	ctx.channel().writeAndFlush(new TextWebSocketFrame(returnEncoder));
        	isFirstConnect=false;
		}
        else
        {
        	ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
        }
	}
}
