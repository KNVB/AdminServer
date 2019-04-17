package com.ftp;
import java.util.TreeMap;

import com.admin.adminObj.AccessRightEntry;
public class FtpUserInfo 
{
	private String userId="0";
	private String userName="";
	private String password="";
	private boolean enabled=false;
	private TreeMap<String,AccessRightEntry>accessRightEntries=new TreeMap<String,AccessRightEntry>();
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public TreeMap<String,AccessRightEntry> getAccessRightEntries() {
		return accessRightEntries;
	}
	public void setAccessRightEntries(TreeMap<String,AccessRightEntry> accessRightEntries) {
		this.accessRightEntries = accessRightEntries;
	}}
