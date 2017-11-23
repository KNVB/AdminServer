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
	private void handleRequest(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception
	{
		 // Send the uppercase string back.
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
        	request=unescape(new String(myRSA.decode(hexStringToByteArray(request))));
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
	/**
     * Convert hex decimal string to byte array
     * @param data hex decimal string
     * @return byte array
     */
    public static byte[] hexStringToByteArray(String data) {
		int k = 0;
		byte[] results = new byte[data.length() / 2];
		for (int i = 0; i < data.length();) {
			results[k] = (byte) (Character.digit(data.charAt(i++), 16) << 4);
			results[k] += (byte) (Character.digit(data.charAt(i++), 16));
			k++;
		}
 
		return results;
	} 
    /**
     *Unescape javascript escaped string
     *@param src javascript escaped string
     *@return unescaped string
   */
  public static String unescape(String src) 
  	{   
          StringBuffer tmp = new StringBuffer();   
          tmp.ensureCapacity(src.length());   
          int lastPos = 0, pos = 0;   
          char ch;   
          while (lastPos < src.length()) {   
              pos = src.indexOf("%", lastPos);   
              if (pos == lastPos) {   
                  if (src.charAt(pos + 1) == 'u') {   
                      ch = (char) Integer.parseInt(src   
                              .substring(pos + 2, pos + 6), 16);   
                      tmp.append(ch);   
                      lastPos = pos + 6;   
                  } else {   
                      ch = (char) Integer.parseInt(src   
                              .substring(pos + 1, pos + 3), 16);   
                      tmp.append(ch);   
                      lastPos = pos + 3;   
                  }   
              } else {   
                  if (pos == -1) {   
                      tmp.append(src.substring(lastPos));   
                      lastPos = src.length();   
                  } else {   
                      tmp.append(src.substring(lastPos, pos));   
                      lastPos = pos;   
                  }   
              }   
          }   
          return tmp.toString();   
      }
}
