package com.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.encoders.Base64;

public class KeyCoder 
{
	Cipher rsaCipher;
	public KeyCoder(String publicKey) throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException
	{
		rsaCipher = Cipher.getInstance("RSA");
		byte[] publicBytes = Base64.decode(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		rsaCipher.init(Cipher.ENCRYPT_MODE,keyFactory.generatePublic(keySpec));
	}
	public String encode(String plainText) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException
	{
		String result;
		result=new String(Base64.encode(rsaCipher.doFinal(plainText.getBytes("UTF-8"))));
		return result;
	}
}
