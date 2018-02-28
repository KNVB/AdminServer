package com.admin.adminObj;

public class BindingAddress 
{
	private String ipAddress="*";
	private boolean bound=true;
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public boolean isBound() {
		return bound;
	}
	public void setBound(boolean bind) {
		this.bound = bind;
	}
}
