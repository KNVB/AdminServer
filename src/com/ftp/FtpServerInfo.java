package com.ftp;
import java.util.ArrayList;
import java.util.TreeMap;
public class FtpServerInfo 
{
	private ArrayList<String> bindingAddresses=new ArrayList<String>();
	private int controlPort=21;
	private String description="New Server";
	private TreeMap<String,FtpUserInfo> ftpUserInfoList=new TreeMap<String,FtpUserInfo>();
	private ArrayList<String>passiveModePortRange=new ArrayList<String>();
	private boolean passiveModeEnabled=false;
	private String serverId="";
	private int status=FtpServerStatus.DISABLE;

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
	public ArrayList<String> getPassiveModePortRange() {
		return passiveModePortRange;
	}
	public void setPassiveModePortRange(ArrayList<String> passiveModePortRange) {
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
