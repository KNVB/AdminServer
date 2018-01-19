package com.util;

public class FileSystemObject 
{
	private String type,pathName;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPathName() {
		return pathName;
	}
	public void setPathName(String pathName) {
		this.pathName = pathName;
	}
	public FileSystemObject(String type,String pathName)
	{
		this.type=type;
		this.pathName=pathName;
	}
}
