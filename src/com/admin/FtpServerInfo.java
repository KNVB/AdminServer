package com.admin;

public class FtpServerInfo 
{
	public String description="New String";
	public String[] bindingAddress;
	public int controlPort=21;
	public boolean passiveModeEnabled=false;
	public String passiveModePortRange="";
	public FtpUserInfo[] ftpUserInfoList;
	public int status=0;
}
