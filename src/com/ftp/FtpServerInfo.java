package com.ftp;
import java.util.ArrayList;
import java.util.TreeMap;
public class FtpServerInfo 
{
	private int status=FtpServerStatus.DISABLE;
	private int controlPort=21;
	private String serverId="";
	private String passiveModePortRange="";
	private String description="New Server";
	private boolean passiveModeEnabled=false;
	private ArrayList<String> bindingAddresses=new ArrayList<String>();
	private TreeMap<String,FtpUserInfo> ftpUserInfoList=new TreeMap<String,FtpUserInfo>();
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getControlPort() {
		return controlPort;
	}
	public void setControlPort(int controlPort) {
		this.controlPort = controlPort;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getPassiveModePortRange() {
		return passiveModePortRange;
	}
	public void setPassiveModePortRange(String passiveModePortRange) {
		this.passiveModePortRange = passiveModePortRange;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isPassiveModeEnabled() {
		return passiveModeEnabled;
	}
	public void setPassiveModeEnabled(boolean passiveModeEnabled) {
		this.passiveModeEnabled = passiveModeEnabled;
	}
	public ArrayList<String> getBindingAddresses() {
		return bindingAddresses;
	}
	public void setBindingAddresses(ArrayList<String> bindingAddresses) {
		this.bindingAddresses = bindingAddresses;
	}
	public TreeMap<String,FtpUserInfo> getFtpUserInfoList() {
		return ftpUserInfoList;
	}
	public void setFtpUserInfoList(TreeMap<String,FtpUserInfo> ftpUserInfoList) {
		this.ftpUserInfoList = ftpUserInfoList;
	}
}