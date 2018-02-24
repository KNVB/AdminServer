package com.admin.adminObj;
import java.util.ArrayList;
public class FtpServerInfo 
{
	public int status=0;
	public int controlPort=21;
	public String passiveModePortRange="";
	public String description="New Server";
	public boolean passiveModeEnabled=false;
	public ArrayList<String> bindingAddress=new ArrayList<String>();
	public ArrayList<FtpUserInfo> ftpUserInfoList=new ArrayList<FtpUserInfo>();
}
