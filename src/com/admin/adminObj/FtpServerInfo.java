package com.admin.adminObj;
import java.util.ArrayList;
public class FtpServerInfo 
{
	private int status=0;
	private int controlPort=21;
	private String serverId="";
	private String passiveModePortRange="";
	private String description="New Server";
	private boolean passiveModeEnabled=false;
	private ArrayList<BindingAddress> bindingAddresses=new ArrayList<BindingAddress>();
	private ArrayList<FtpUserInfo> ftpUserInfoList=new ArrayList<FtpUserInfo>();
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
	public ArrayList<BindingAddress> getBindingAddresses() {
		return bindingAddresses;
	}
	public void setBindingAddresses(ArrayList<BindingAddress> bindingAddresses) {
		this.bindingAddresses = bindingAddresses;
	}
	public ArrayList<FtpUserInfo> getFtpUserInfoList() {
		return ftpUserInfoList;
	}
	public void setFtpUserInfoList(ArrayList<FtpUserInfo> ftpUserInfoList) {
		this.ftpUserInfoList = ftpUserInfoList;
	}
}
