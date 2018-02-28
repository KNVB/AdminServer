package com.admin.adminObj;
import java.util.Hashtable;
public class FtpUserInfo 
{
	private String userId="0";
	private String userName="";
	private String password="";
	private boolean enabled=false;
	private Hashtable<String,AccessRightEntry>accessRightEntries=new Hashtable<String,AccessRightEntry>();
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
	public Hashtable<String,AccessRightEntry> getAccessRightEntries() {
		return accessRightEntries;
	}
	public void setAccessRightEntries(Hashtable<String,AccessRightEntry> accessRightEntries) {
		this.accessRightEntries = accessRightEntries;
	}}
