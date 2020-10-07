package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Status;
import com.revature.utils.ConnectionUtil;

public class StatusPostgres implements StatusDAO {
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

	@Override
	public Integer createStatus(Status s) {
		Integer id = 0;
		
		try(Connection conn = cu.getConnection())
		{
			conn.setAutoCommit(false);
			String sql = "insert into status values (default, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, s.getName());
			
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if(rs.next()) 
			{
				id=rs.getInt(1);
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public Status getStatusByName(String name) {
		Status status = null;
		
		try(Connection conn = cu.getConnection())
		{
			String sql = "select name, id from status where name = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) 
			{
				status = new Status();
				status.setId(rs.getInt("id"));
				status.setName(rs.getString("name"));
			}
			
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
		return status;
	}

	@Override
	public Set<Status> getStatuses() {
		Set<Status> statuses = null;
		try(Connection conn = cu.getConnection())
		{
			String sql = "select * from status";
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			statuses = new HashSet<>();
			while(rs.next()) 
			{
				Status status = new Status();
				status.setId(rs.getInt("id"));
				status.setName(rs.getString("name"));
				statuses.add(status);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return statuses;
	}

}
