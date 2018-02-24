package com.admin.adminObj;

public class AccessRightEntry 
{
	private String entryId="";
	private String permission="";
	private String virtualDir="/";
	private String physicalDir="/";
	public String getEntryId() {
		return entryId;
	}
	public void setEntryId(String entryId) {
		this.entryId = entryId;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getVirtualDir() {
		return virtualDir;
	}
	public void setVirtualDir(String virtualDir) {
		this.virtualDir = virtualDir;
	}
	public String getPhysicalDir() {
		return physicalDir;
	}
	public void setPhysicalDir(String physicalDir) {
		this.physicalDir = physicalDir;
	}	
}
