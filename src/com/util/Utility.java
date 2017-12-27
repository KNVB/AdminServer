package com.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

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
 *
 */
public class Utility 
{
	public Utility()
	{
		
	}
	/**
	 * Get all local IP
	 * @return list of local IP address
	 * @throws SocketException
	 */
	public static ArrayList<String> getAllLocalIp()throws SocketException 
	{	
		int index;
		String temp;
		ArrayList<String> addrList = new ArrayList<String>();
	    Enumeration<NetworkInterface> enumNI = NetworkInterface.getNetworkInterfaces();
	    while ( enumNI.hasMoreElements() ){
	        NetworkInterface ifc = enumNI.nextElement();
	        if( ifc.isUp() && !ifc.isLoopback() ){
	            Enumeration<InetAddress>enumAdds = ifc.getInetAddresses();
	            while ( enumAdds.hasMoreElements() ){
	                InetAddress addr = enumAdds.nextElement();
	                temp=addr.getHostAddress();
	                index=temp.indexOf("%");
	                if (index>-1)
	                	temp=temp.substring(0,index);
	                addrList.add(temp);
	            }
	        }
	    }
		return addrList;
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
  //Converting a bytes array to string of hex character
    public static String byteArrayToHexString(byte[] b) {
     int len = b.length;
     String data = new String();

     for (int i = 0; i < len; i++){
     data += Integer.toHexString((b[i] >> 4) & 0xf);
     data += Integer.toHexString(b[i] & 0xf);
     }
     return data;
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
