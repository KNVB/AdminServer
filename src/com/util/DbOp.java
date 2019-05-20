package com.util;
import java.util.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.admin.adminObj.FtpAdminUserInfo;
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
	public int addFtpServer(FtpServerInfo ftpServerInfo) throws AdminServerException
	{
		int result=-1;
		if (isAddressAndPortAvailable(ftpServerInfo))
		{
			String sql;
			int startPort,endPort;
			PreparedStatement stmt=null;
			try
			{
				dbConn.setAutoCommit(false);
				if (ftpServerInfo.isPassiveModeEnabled())
				{
					isPassivePortAvailable(ftpServerInfo);
					String[] temp;
					sql="insert into passive_port_range (server_id,start,end) values(?,?,?)";
					for(String portRange : ftpServerInfo.getPassiveModePortRange())
					{
						stmt = dbConn.prepareStatement(sql);
						temp=portRange.split("-");
						startPort=Integer.parseInt(temp[0]);
						endPort=Integer.parseInt(temp[1]);
						stmt.setString(1,ftpServerInfo.getServerId());
						stmt.setInt(2, startPort);
						stmt.setInt(3, endPort);
						stmt.executeUpdate();
						stmt.close();
					}
					
				}
				
				sql="insert into server (server_id,status,description,control_port,support_passive_mode) values (?,?,?,?,?)";
				stmt = dbConn.prepareStatement(sql);
				stmt.setString(1, ftpServerInfo.getServerId());
				stmt.setInt(2,ftpServerInfo.getStatus());
				stmt.setString(3, ftpServerInfo.getDescription());
				stmt.setInt(4,ftpServerInfo.getControlPort());
				if (ftpServerInfo.isPassiveModeEnabled())
				{	
					stmt.setInt(5,1);
				}
				else
				{	
					stmt.setInt(5,0);
				}
				stmt.executeUpdate();
				sql="insert into server_binding (server_id,binding_address) values (?,?)";
				for (String address : ftpServerInfo.getBindingAddresses())
				{
					stmt.close();
					stmt = dbConn.prepareStatement(sql);
					stmt.setString(1, ftpServerInfo.getServerId());
					stmt.setString(2, address);
					stmt.executeUpdate();
				}
				dbConn.commit();
				dbConn.setAutoCommit(true);
				result=0;
			}
			catch(NumberFormatException e)
			{
				String temp=e.getMessage();
				temp=temp.substring(temp.indexOf("\""));
				throw new AdminServerException(temp+",6");
				//throw new AdminServerException("Invalid port no.:"+temp);
			}
			catch (SQLException e) 
			{
				throw new AdminServerException(e.getMessage());
			}
			finally
			{
				releaseResource(null, stmt);
			}
		}
		else
			result=1;
		return result;
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
	public int delFtpServer(String ftpServerId)throws SQLException
	{
		int result=-1;
		ResultSet rs = null;
		PreparedStatement stmt=null;
		String sql="select * from server where server.server_id=?";
		try
		{
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, ftpServerId);
			rs=stmt.executeQuery();
			if (rs.next())
			{
				rs.close();
				stmt.close();
				dbConn.setAutoCommit(false);
				sql="delete from server_binding where server_id=?";
				stmt = dbConn.prepareStatement(sql);
				stmt.setString(1, ftpServerId);
				stmt.executeUpdate();
				stmt.close();
				
				sql="delete from server where server_id=?";
				stmt = dbConn.prepareStatement(sql);
				stmt.setString(1, ftpServerId);
				stmt.executeUpdate();
				stmt.close();
				
				dbConn.commit();
				dbConn.setAutoCommit(true);
				result=0;//The FTP server is deleted.
			}
			else
			{
				result=1;
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
		String sql="select * from server a "; 
		sql+=" inner join server_binding b on a.server_id=b.server_id"; 
		sql+=" left join passive_port_range c on a.server_id=c.server_id"; 
		sql+=" order by a.server_id";	
		
		FtpServerInfo ftpServerInfo=null;
		ArrayList<String>addressList=null;
		ArrayList<String> passiveModePortRange=null;
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
						if (passiveModePortRange!=null)
							ftpServerInfo.setPassiveModePortRange(passiveModePortRange);
						serverList.put(ftpServerInfo.getServerId(),ftpServerInfo);
					}
					ftpServerInfo=new FtpServerInfo();
				
					ftpServerInfo.setServerId(rs.getString("server_id"));
					ftpServerInfo.setControlPort(rs.getInt("control_port"));
					ftpServerInfo.setStatus(rs.getInt("status"));
					ftpServerInfo.setDescription(rs.getString("description"));
					if (rs.getInt("support_passive_mode")==1)
					{	
						ftpServerInfo.setPassiveModeEnabled(true);
						passiveModePortRange=new ArrayList<String>();
					}
					else
					{	
						ftpServerInfo.setPassiveModeEnabled(false);
						passiveModePortRange=null;
					}
					preServerId=rs.getString("server_id");
					addressList=new ArrayList<String>();
				}
				addressList.add(rs.getString("binding_address"));
				if (rs.getInt("support_passive_mode")==1)
				{
					passiveModePortRange.add(rs.getString("start")+"-"+rs.getString("end"));
				}
			}
			if (passiveModePortRange!=null)
				ftpServerInfo.setPassiveModePortRange(passiveModePortRange);
			if ((addressList!=null) && (addressList.size()>0))
			{
				ftpServerInfo.setBindingAddresses(addressList);
				serverList.put(ftpServerInfo.getServerId(),ftpServerInfo);
			}
			logger.debug("No. of FTP server :"+serverList.size());
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
	public FtpServerInfo getFtpServerInfo(String serverId)
	{
		ResultSet rs = null;
		PreparedStatement stmt=null;
		String sql="select * from server a inner join server_binding b on a.server_id=? and a.server_id=b.server_id order by a.server_id";
		FtpServerInfo ftpServerInfo=null;
		ArrayList<String>addressList=null;
		try 
		{
			stmt = dbConn.prepareStatement(sql);
			stmt.setString(1, serverId);
			rs=stmt.executeQuery();
			if (rs.next())
			{
				ftpServerInfo=new FtpServerInfo();
				ftpServerInfo.setServerId(rs.getString("server_id"));
				ftpServerInfo.setControlPort(rs.getInt("control_port"));
				ftpServerInfo.setStatus(rs.getInt("status"));
				ftpServerInfo.setDescription(rs.getString("description"));
				if (rs.getInt("support_passive_mode")==1)
				{	
					ftpServerInfo.setPassiveModeEnabled(true);
					//ftpServerInfo.setPassiveModePortRange(rs.getString("passive_mode_port_range"));
				}
				else
				{	
					ftpServerInfo.setPassiveModeEnabled(false);
					//ftpServerInfo.setPassiveModePortRange(null);
				}
				addressList=new ArrayList<String>();
				addressList.add(rs.getString("binding_address"));
				while (rs.next())
				{
					addressList.add(rs.getString("binding_address"));
				}
				ftpServerInfo.setBindingAddresses(addressList);
				
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
		return ftpServerInfo;
	}
	private boolean isAddressAndPortAvailable(FtpServerInfo ftpServerInfo)throws AdminServerException
	{
		boolean hasWildCardAddress=false,result=false;
		ResultSet rs = null;
		PreparedStatement stmt=null;
		logger.debug(ftpServerInfo.getBindingAddresses()==null);
		String sql="select server.server_id from server inner join server_binding on server.control_port=? and server.server_id !=? and server.server_id = server_binding.server_id ";
		String addressList=new String();
		for (String address : ftpServerInfo.getBindingAddresses())
		{
			if (address.equals("*"))
			{	
				hasWildCardAddress=true;
				break;
			}
			else
				addressList+="'"+address+"',";
		}
		if (!hasWildCardAddress)
		{
			addressList+="'*'";
			sql+=" and server_binding.binding_address in("+addressList+")";
		}
		logger.debug(sql);
		try
		{
			stmt = dbConn.prepareStatement(sql);
			logger.debug("Requested control port:"+ftpServerInfo.getControlPort()+",server id="+ftpServerInfo.getServerId());	
			stmt.setInt(1,ftpServerInfo.getControlPort());
			stmt.setString(2,ftpServerInfo.getServerId());
			rs=stmt.executeQuery();
			if (rs.next())
			{
				result=false;
			}
			else
			{
				result=true;
			}
		}
		catch (SQLException e) 
		{
			throw new AdminServerException(e.getMessage());
		}
		finally
		{
			releaseResource(rs, stmt);
		}
		return result;
	}
	private void isPassivePortAvailable(FtpServerInfo ftpServerInfo) throws AdminServerException
	{
		int startPort,endPort;
		ResultSet rs = null;
		PreparedStatement stmt=null;
		String sql=new String();
		try
		{
			for(String portRange : ftpServerInfo.getPassiveModePortRange())
			{
				String[] temp=portRange.split("-");
				startPort=Integer.parseInt(temp[0]);
				endPort=Integer.parseInt(temp[1]);
				if (endPort < startPort)
				{
					throw new AdminServerException(portRange+",2");
					//throw new AdminServerException("Invalid Port Range:"+port);
				}
				else
				{
					logger.debug("server id="+ftpServerInfo.getServerId()+",start Port="+startPort+",End Port="+endPort);
					sql="select * from passive_port_range where server_id!=? ";
					sql+=" and (start<=? and end>=?) or (start<=? and end>=?) or (start>=? and end<=?)";
					logger.debug(sql);
					stmt = dbConn.prepareStatement(sql);
					stmt.setString(1,ftpServerInfo.getServerId());
					stmt.setInt(2, startPort);
					stmt.setInt(3, startPort);
					stmt.setInt(4, endPort);
					stmt.setInt(5, endPort);
					stmt.setInt(6, startPort);
					stmt.setInt(7, endPort);

					rs=stmt.executeQuery();
					if (rs.next())
					{
						throw new AdminServerException(portRange+",3");
						//throw new AdminServerException("Passive Port Range:"+port+" is not available.");
					}
				}
			}
		}
		catch(NumberFormatException e)
		{
			String temp=e.getMessage();
			temp=temp.substring(temp.indexOf("\""));
			throw new AdminServerException(temp+",6");
			//throw new AdminServerException("Invalid port no.:"+temp);
		}
		catch (SQLException e) 
		{
			throw new AdminServerException(e.getMessage());
		}
		finally
		{
			releaseResource(rs, stmt);
		}
	}
	public int updateFtpServerInfo(FtpServerInfo ftpServerInfo)throws AdminServerException
	{
		int result=-1;
		return result;
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
	public static void main(String[] args) 
	{
		Logger  logger = LogManager.getLogger(DbOp.class.getName());
		int returnCode=-1;
		try {
			DbOp dbo=new DbOp(logger);
			ArrayList<String>bindingAddresses=new ArrayList<String>();
			ArrayList<String>passiveModePortRange=new ArrayList<String>();
			bindingAddresses.add("169.254.21.133");
			//bindingAddresses.add("*");
			passiveModePortRange.add("30-33");
			//passiveModePortRange.add("1700-1801");
			//passiveModePortRange.add("8990-8999");
			//passiveModePortRange.add("31-33");
			FtpServerInfo ftpServerInfo=new FtpServerInfo();
			ftpServerInfo.setDescription("Passive Server");
			ftpServerInfo.setServerId("4");
			ftpServerInfo.setControlPort(21);
			ftpServerInfo.setBindingAddresses(bindingAddresses);
			ftpServerInfo.setPassiveModePortRange(passiveModePortRange);
			ftpServerInfo.setPassiveModeEnabled(true);
			logger.debug("isAddressAndPortAvailable="+dbo.isAddressAndPortAvailable(ftpServerInfo));
			//dbo.isAddressAndPortAvailable(ftpServerInfo);
			//dbo.isPassivePortsAvailable(ftpServerInfo);
			logger.debug(dbo.addFtpServer(ftpServerInfo));
		}		
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.debug(e.getMessage());
		}
		
	}	
}
