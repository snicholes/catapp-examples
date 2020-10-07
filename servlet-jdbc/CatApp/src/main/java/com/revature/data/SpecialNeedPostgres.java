package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.SpecialNeed;
import com.revature.utils.ConnectionUtil;

public class SpecialNeedPostgres implements SpecialNeedDAO {
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	
	@Override
	public Integer createNeed(SpecialNeed sn) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "insert into special_need values (default, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, sn.getName());
			
			
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
	public SpecialNeed getNeedById(Integer id) {
		SpecialNeed sn = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select * from special_need where id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				sn = new SpecialNeed();
			
				sn.setId(rs.getInt("id"));
				sn.setName(rs.getString("name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sn;
	}
	
	@Override
	public Set<SpecialNeed> getNeeds() {
		Set<SpecialNeed> needs = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select * from special_need";
			Statement stmt = conn.createStatement();
			
			ResultSet rs = stmt.executeQuery(sql);
			
			needs = new HashSet<>();
			while (rs.next()) {
				SpecialNeed sn = new SpecialNeed();
				sn.setId(rs.getInt("id"));
				sn.setName(rs.getString("name"));
				needs.add(sn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return needs;
	}
	
	@Override
	public void updateNeed(SpecialNeed sn)
	{
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "update special_need set name = ? "
					 + "where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sn.getId());
			
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
	public void deleteNeed(SpecialNeed sn)
	{
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);

			String sql = "delete from special_need where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, sn.getId());
			
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
