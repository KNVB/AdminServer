package com.util;
import java.util.HashMap;
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
public class Response 
{
	private String action;
	private int responseCode=0;
	private String returnMessage="";
	private HashMap<String,Object> returnObjects=new HashMap<String,Object>();
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public String getReturnMessage() {
		return returnMessage;
	}
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}
	@SuppressWarnings("rawtypes")
	public HashMap getReturnObjects() {
		return returnObjects;
	}
	public void setReturnObjects(String key, Object returnObjects) {
		this.returnObjects.put(key, returnObjects);
	}
	
	/*public String toJSONString()
	{
		String result;
		result ="{\"action\":\""+action+"\",";
		result+="\"responseCode\":"+responseCode+",";
		result+="\"returnMessage\":\""+returnMessage+"\"}";
		return result;
	}*/
}
