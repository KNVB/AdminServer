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
package com.util;
import javax.crypto.*;
import java.security.*;
import java.math.BigInteger;
import java.security.interfaces.*;
/**
 * @author Roy Tsang
 *
 */
public class RSA
{
	private KeyPair pair = null;
	private RSAPrivateKey privateKey=null;
	private RSAPublicKey publicKey=null;
	/**
	 * object constructor
	 * @param modulusSize (i.e key size,e.g. 1024 bit)
	 * @throws Exception
	 */
	public RSA(int modulusSize)throws Exception
	{
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA",new org.bouncycastle.jce.provider.BouncyCastleProvider());
        keyGen.initialize(modulusSize, new SecureRandom());		
	 	pair = keyGen.generateKeyPair();
	 	privateKey=(RSAPrivateKey)pair.getPrivate();
		publicKey=(RSAPublicKey)pair.getPublic();
	}
	/**
	 * get Public exponent
	 * @return get public exponent
	 */
	public String getPublicExponent()
	{
		return this.publicKey.getPublicExponent().toString(16);
	}
	/**
	 * get Public Modulus
	 * @return get Public Modulus
	 */
	public String getPublicModulus()
	{
		return this.publicKey.getModulus().toString(16);
	}
	
	/**
	 * get Private Exponent
	 * @return get Private Exponent
	 */
	public String getPrivateExponent()
	{
		return this.privateKey.getPrivateExponent().toString(16);
	}
	/**
	 * get Private Modulus
	 * @return  get Private Modulus
	 */
	public BigInteger getPrivateModulus()
	{
		return this.privateKey.getModulus();
	}
	/**
	 * decode data array
	 * @param cleartext encoded byte array
	 * @return decoded byte array
	 * @throws Exception
	 */
	public byte[] decode(byte[] cleartext)throws Exception
	{
		Cipher rsaCipher;
 		// Create the cipher
		rsaCipher = Cipher.getInstance("RSA",new org.bouncycastle.jce.provider.BouncyCastleProvider());
		rsaCipher.init(Cipher.DECRYPT_MODE, this.privateKey);
		byte[] cleartext1 =rsaCipher.doFinal(cleartext);
		return cleartext1;		
	}	
	/**
	 * encode raw data array
	 * @param data raw data array
	 * @return encoded data array
	 * @throws Exception
	 */
	public byte[] encode(byte[] data) throws Exception
	{
		Cipher rsaCipher;
		rsaCipher = Cipher.getInstance("RSA",new org.bouncycastle.jce.provider.BouncyCastleProvider());
		rsaCipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
		byte[] ciphertext = rsaCipher.doFinal(data);
		return ciphertext;
	}
	/**
	 * Convert byte array to hex decimal string
	 * @param block byte array
	 * @return hex string
	 */
	public String toHexString(byte[] block) 
	{
		StringBuffer buf = new StringBuffer();
		int len = block.length;
		for (int i = 0; i < len; i++) {
		     byte2hex(block[i], buf);
		     if (i < len-1) {
		         buf.append(":");
		     }
		} 
		return buf.toString();
	}
	private void byte2hex(byte b, StringBuffer buf)
	{
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
		                    '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);
	}
	/*public static void main(String[] args) throws Exception 
	{
		Response actionResponse=new Response();
    	actionResponse.setResponseCode(0);
    	actionResponse.setReturnMessage("中文");
    	RSA myRSA=new RSA(1024);
    	byte [] encrypted = myRSA.encode((new JSONObject(actionResponse)).toString().getBytes());
    	System.out.println(new String(encrypted));
    	byte[] secret = myRSA.decode(encrypted);
    	System.out.println(new String(secret)); 
	}*/
}