package com.admin;

import java.util.Locale;
import org.apache.logging.log4j.Logger;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class AdminSessionHandler extends SimpleChannelInboundHandler<WebSocketFrame>
{
	private static Logger logger=null;
	public AdminSessionHandler(Logger logger)
	{
		this.logger=logger;
	}
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame)
			throws Exception {
		if (frame instanceof TextWebSocketFrame) {
	         // Send the uppercase string back.
	         String request = ((TextWebSocketFrame) frame).text();
	         logger.debug("{} received {}", ctx.channel(), request);
	         ctx.channel().writeAndFlush(new TextWebSocketFrame(request.toUpperCase(Locale.US)));
	     } else {
	         String message = "unsupported frame type: " + frame.getClass().getName();
	         throw new UnsupportedOperationException(message);
	     }
		
	}
	
}
