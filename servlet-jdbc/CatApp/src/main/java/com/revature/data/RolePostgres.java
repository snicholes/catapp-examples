package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Role;
import com.revature.utils.ConnectionUtil;

public class RolePostgres implements RoleDAO {
	
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	
	@Override
	public Integer createRole(Role r) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "INSERT INTO user_role VALUES (default, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			
			pstmt.setString(1, r.getName());
			
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				id = rs.getInt(1);
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return id;
	}
	
	@Override
	public Role getRoleById(Integer id) {
		Role role = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "SELECT * FROM user_role WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				role = new Role();
				role.setId(id);
				role.setName(rs.getString("name"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return role;
	}
	
	@Override
	public Set<Role> getRoles() {
		Set<Role> roles = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "SELECT * FROM user_role";
			Statement stmt = conn.createStatement();
						
			ResultSet rs = stmt.executeQuery(sql);
			
			roles = new HashSet<>();
			
			while (rs.next()) {
				Role role = new Role();
				role.setId(rs.getInt("id"));
				role.setName(rs.getString("name"));
						
				roles.add(role);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 	
		
		return roles;
	}
	
	@Override
	public void updateRole(Role r) {
		
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "UPDATE user_role "
					+ "SET name = ? "
					+ "WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, r.getName());
			pstmt.setInt(2, r.getId());
			
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void deleteRole(Role r) {
		
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "DELETE user_role "
					+ "WHERE name = ? OR "
					+ "WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, r.getName());
			pstmt.setInt(2, r.getId());
						
			int rowsAffected = pstmt.executeUpdate();
			
			if (rowsAffected > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}