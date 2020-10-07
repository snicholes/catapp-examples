package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Breed;
import com.revature.utils.ConnectionUtil;

public class BreedPostgres implements BreedDAO {
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	
	@Override
	public Integer createBreed(Breed b) {
		Integer id = 0;
		
		try(Connection conn = cu.getConnection())
		{
			conn.setAutoCommit(false);
			String sql = "insert into breed values (default, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, b.getName());
			
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
	public Breed getBreedById(Integer id) {
		Breed breed = null;
		
		try(Connection conn = cu.getConnection())
		{
			String sql = "select name, id from breed where id = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) 
			{
				breed = new Breed();
				breed.setId(rs.getInt("id"));
				breed.setName(rs.getNString("name"));
			}
			
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
		return breed;
	}

	@Override
	public Integer getBreedIDByName(String name) {
		Integer id = 0;
		
		try(Connection conn = cu.getConnection())
		{
			String sql = "select name, id from breed where name = ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,name);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) 
			{
				id = rs.getInt("id");
			}
			
			
		}catch(Exception e) 
		{
			e.printStackTrace();
		}
		
		return id;
	}

	@Override
	public Set<Breed> getBreeds() {
		Set<Breed> breeds = null;
		try(Connection conn = cu.getConnection())
		{
			String sql = "select * from breed";
			
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			breeds = new HashSet<>();
			while(rs.next()) 
			{
				Breed breed = new Breed();
				breed.setId(rs.getInt("id"));
				breed.setName(rs.getString("name"));
				breeds.add(breed);
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return breeds;
	}
	
	@Override
	public void updateBreed(Breed b) {
		try(Connection conn = cu.getConnection())
		{
			conn.setAutoCommit(false);
			String sql = "update breed set name = ? where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, b.getName());
			pstmt.setInt(2, b.getId());
			
			int rowsAffected = pstmt.executeUpdate();
			if(rowsAffected > 0) 
			{	
				conn.commit();
			}else {
				conn.rollback();
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void deleteBreed(Breed b) {
		try(Connection conn = cu.getConnection())
		{
			conn.setAutoCommit(false);
			String sql = "delete from breed where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, b.getId());
			
			int rowsAffected = pstmt.executeUpdate();
			if(rowsAffected > 0) 
			{	
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}

}
