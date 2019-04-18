package com.util;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.Logger;

import com.admin.adminObj.FtpAdminUserInfo;
import com.ftp.FtpServer;
import com.ftp.FtpServerInfo;

//import com.myftpserver.server.FtpServer;
/*
 * Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 * @author SITO3
 *
 */
public class DbOp {
	private Logger logger = null;
	private Connection dbConn = null;
	private String jdbcURL = new String();
	private String jdbcDriver = new String();
	/**
	 * Database object,initialize db connection
	 * @param logger Message logger
	 * @throws Exception
	 */
	public DbOp(Logger logger) throws Exception 
	{
		jdbcDriver = "org.sqlite.JDBC";
		jdbcURL = "jdbc:sqlite:admin.db";
		this.logger=logger;
		Class.forName(jdbcDriver);
		dbConn = DriverManager.getConnection(jdbcURL);
	}
	public <T>int addFtpServer(FtpServer<T> ftpServer) throws SQLException
	{
		int result=-1;
		ResultSet rs = null;
		PreparedStatement stmt=null;
		logger.debug(ftpServer.getBindingAddresses().toString());
		String sql="select * from server_binding where binding_address in ";
		
		String addressList=new String();
		for (String address : ftpServer.getBindingAddresses())
		{
			addressList+="'"+address+"',";
		}
		addressList=addressList.substring(0, addressList.length()-1);
		sql+="("+addressList+") and control_port=?";
		logger.debug(sql);
		try
		{
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1,ftpServer.getControlPort());					
			rs=stmt.executeQuery();
			if (rs.next())
			{
				result=1; //Some a ftp server bind the specified address and port already.
			}
			else
			{
				rs.close();
				stmt.close();
				dbConn.setAutoCommit(false);
				sql="insert into server (server_id,status,description) values ( ?,?,?)";
				stmt = dbConn.prepareStatement(sql);
				stmt.setString(1, ftpServer.getServerId());
				stmt.setInt(2,ftpServer.getStatus());
				stmt.setString(3, ftpServer.getDescription());
				stmt.executeUpdate();
				sql="insert into server_binding (server_id,binding_address,control_port) values (?,?,?)";
				for (String address : ftpServer.getBindingAddresses())
				{
					stmt.close();
					stmt = dbConn.prepareStatement(sql);
					stmt.setString(1, ftpServer.getServerId());
					stmt.setString(2, address);
					stmt.setInt(3,ftpServer.getControlPort());
					stmt.executeUpdate();
				}				
				dbConn.commit();
				dbConn.setAutoCommit(true);
				result=0;//The specified address and port is available.
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			releaseResource(rs, stmt);
		}		
		return result;
	}
	public TreeMap<String,FtpAdminUserInfo> getAdminUserList()
	{
		ResultSet rs = null;
		PreparedStatement stmt=null;
		FtpAdminUserInfo adminUserInfo;
		String sql="select * from admin_user";
		TreeMap<String,FtpAdminUserInfo>  adminUserList=new TreeMap<String,FtpAdminUserInfo>();
		try 
		{
			stmt = dbConn.prepareStatement(sql);
			stmt.setInt(1, 1);
			rs=stmt.executeQuery();
			while (rs.next()) 
			{
				adminUserInfo=new FtpAdminUserInfo();
				adminUserInfo.setUserId(rs.getString("admin_id"));
				adminUserInfo.setUserName(rs.getString("username"));
				adminUserInfo.setPassword(rs.getString("password"));
				adminUserInfo.setEnabled(rs.getBoolean("active"));
				adminUserList.put(rs.getString("admin_id"), adminUserInfo);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			releaseResource(rs, stmt);
		}		
		return adminUserList;
	}
	public TreeMap<String,FtpServerInfo> getAllServerList()
	{
		ResultSet rs = null;
		PreparedStatement stmt=null;
		String preServerId=new String();
		String sql="select * from server a inner join server_binding b on a.server_id=b.server_id ";
		FtpServerInfo ftpServerInfo=null;
		ArrayList<String>addressList=null; 
		TreeMap<String,FtpServerInfo>  serverList=new TreeMap<String,FtpServerInfo>();
		try 
		{
			stmt = dbConn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while (rs.next())
			{
				//rs.getString("config_json")
				
				if (!preServerId.equals(rs.getString("server_id")))
				{	
					if (ftpServerInfo!=null)
					{	
						ftpServerInfo.setBindingAddresses(addressList);
						serverList.put(rs.getString("server_id"),ftpServerInfo);
					}
					ftpServerInfo=new FtpServerInfo();
					ftpServerInfo.setServerId(rs.getString("server_id"));
					ftpServerInfo.setControlPort(rs.getInt("control_port"));
					ftpServerInfo.setStatus(rs.getInt("status"));
					ftpServerInfo.setDescription(rs.getString("description"));
					preServerId=rs.getString("server_id");
					addressList=new ArrayList<String>();
				}
				addressList.add(rs.getString("binding_address"));
			}
			if ((addressList!=null) && (addressList.size()>0))
			{
				ftpServerInfo.setBindingAddresses(addressList);
				serverList.put(ftpServerInfo.getServerId(),ftpServerInfo);
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			releaseResource(rs, stmt);
		}		
		return serverList;
	}
	public boolean adminLogin(String userName, String password) 
	{
		boolean loginResult=false;
		ResultSet rs = null;
		PreparedStatement stmt=null;
		String sql="select * from admin_user where username=? and password=? and active=?";
		try 
		{
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, userName);
			stmt.setString(2, password);
			stmt.setInt(3, 1);
			rs=stmt.executeQuery();
			if (rs.next()) 
			{
				loginResult=true;
				logger.debug("Login success");
			}
			else
			{
				logger.debug("Login Failure");				
			}			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			releaseResource(rs, stmt);
		}		
		return loginResult;
		
	}
	/**
	 * Close db connection
	 * @throws Exception
	 */
	public void close() throws Exception 
	{
		dbConn.close();
		dbConn = null;
	}
	/**
	 * Release resource for 
	 * @param r ResultSet object
	 * @param s PreparedStatement object
	 */
	private void releaseResource(ResultSet r, PreparedStatement s) 
	{
		if (r != null) 
		{
			try 
			{
				r.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		if (s != null) 
		{
			try 
			{
				s.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		r = null;
		s = null;
	}		
}
