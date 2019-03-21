package com.admin.adminObj;

public class FtpAdminUserInfo 
{
	private String userId="0";
	private String userName="";
	private String password="";
	private boolean enabled=false;
	public FtpAdminUserInfo() 
	{
	}
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
}
