package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Breed;
import com.revature.beans.Cat;
import com.revature.beans.SpecialNeed;
import com.revature.beans.Status;
import com.revature.utils.ConnectionUtil;

public class CatPostgres implements CatDAO {
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();

	public Integer createCat(Cat c) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "insert into cat values (default, ?, ?, ?, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, c.getName());
			pstmt.setInt(2, c.getAge());
			pstmt.setInt(3, c.getBreed().getId());
			pstmt.setInt(4, c.getStatus().getId());
			
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			
			if (rs.next()) {
				// add the cat's special needs
				int specialNeedsAdded = 0;
				for (SpecialNeed sn : c.getSpecialNeeds()) {
					sql = "insert into cat_special_need values (?, ?)";
					pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, c.getId());
					pstmt.setInt(2, sn.getId());
					pstmt.executeUpdate();
					specialNeedsAdded++;
				}
				if (specialNeedsAdded == c.getSpecialNeeds().size()) {
					id = rs.getInt(1);
					conn.commit();
				} else {
					conn.rollback();
				}
			} else {
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return id;
	}

	public Cat getCatById(Integer id) {
		Cat cat = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select A.id, A.name, age, status_id, status.name as "
					+ "status_name, breed_id, breed_name from " + 
					"(select cat.id, cat.name, age, status_id, breed_id, breed.name "
					+ "as breed_name from cat " + 
					"join breed on breed_id = breed.id) as A " + 
					"join status on status_id = status.id where A.id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				cat = new Cat();
				cat.setId(id);
				cat.setName(rs.getString("name"));
				cat.setAge(rs.getInt("age"));
				
				Breed b = new Breed();
				b.setId(rs.getInt("breed_id"));
				b.setName(rs.getString("breed_name"));
				cat.setBreed(b);
				
				Status s = new Status();
				s.setId(rs.getInt("status_id"));
				s.setName(rs.getString("status_name"));
				cat.setStatus(s);
				
				// get the cat's special needs
				sql = "select * from cat_special_need join special_need on "
						+ "special_need_id = special_need.id where cat_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, cat.getId());
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<SpecialNeed> specialNeeds = new HashSet<>();
				while (rs2.next()) {
					SpecialNeed sn = new SpecialNeed();
					sn.setId(rs2.getInt("id"));
					sn.setName(rs2.getString("name"));
					specialNeeds.add(sn);
				}
				cat.setSpecialNeeds(specialNeeds);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cat;
	}

	public Set<Cat> getCatsByStatus(Status s) {
		Set<Cat> cats = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select A.id, A.name, age, status_id, status.name as "
					+ "status_name, breed_id, breed_name from " + 
					"(select cat.id, cat.name, age, status_id, breed_id, breed.name "
					+ "as breed_name from cat " + 
					"join breed on breed_id = breed.id) as A " + 
					"join status on status_id = status.id where status_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, s.getId());
			
			ResultSet rs = pstmt.executeQuery();
			
			cats = new HashSet<>();
			
			while (rs.next()) {
				Cat cat = new Cat();
				cat.setId(rs.getInt("id"));
				cat.setName(rs.getString("name"));
				cat.setAge(rs.getInt("age"));
				
				Breed b = new Breed();
				b.setId(rs.getInt("breed_id"));
				b.setName(rs.getString("breed_name"));
				cat.setBreed(b);
				
				cat.setStatus(s); // s is the parameter to this method
				
				// get the cat's special needs
				sql = "select * from cat_special_need join special_need on "
						+ "special_need_id = special_need.id where cat_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, cat.getId());
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<SpecialNeed> specialNeeds = new HashSet<>();
				while (rs2.next()) {
					SpecialNeed sn = new SpecialNeed();
					sn.setId(rs2.getInt("id"));
					sn.setName(rs2.getString("name"));
					specialNeeds.add(sn);
				}
				cat.setSpecialNeeds(specialNeeds);
				
				cats.add(cat);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cats;
	}

	public void updateCat(Cat c) {
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "update cat set name = ?, age = ?, breed_id = ?, "
					+ "status_id = ? where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, c.getName());
			pstmt.setInt(2, c.getAge());
			pstmt.setInt(3, c.getBreed().getId());
			pstmt.setInt(4, c.getStatus().getId());
			pstmt.setInt(5, c.getId());
			
			// an executeUpdate call can return the number
			// of rows affected by the statement
			int rowsAffected = pstmt.executeUpdate();
			// this should be 1 since we were updating based on id (unique)
			// so we can just check that it affected more than zero
			if (rowsAffected > 0) {
				// updateSpecialNeeds returns true if the number
				// of special needs in the database after the update
				// is equal to the number of special needs the cat has.
				if (updateSpecialNeeds(c, conn)) {
					conn.commit();
				} else {
					conn.rollback();
				}
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteCat(Cat c) {
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			
			// we need to delete the special needs first so that
			// we don't have orphaned fields (foreign key constraint violation)
			String sql = "delete from cat_special_need where cat_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, c.getId());
			
			int rowsAffected = pstmt.executeUpdate();
			
			// if all of the special needs were properly deleted,
			// we can go on to the next step
			if (rowsAffected == c.getSpecialNeeds().size()) {
				// next, we need to delete any association the
				// cat had with any people from the person_cat table
				sql = "delete from person_cat where cat_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, c.getId());
				
				pstmt.executeUpdate();
				
				// now, we can delete the cat
				sql = "delete from cat where id = ?";
				
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, c.getId());
				
				rowsAffected = pstmt.executeUpdate();
				if (rowsAffected > 0) {
					conn.commit();
				} else {
					conn.rollback();
				}
			} else {
				conn.rollback();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean updateSpecialNeeds(Cat c, Connection conn) throws SQLException {
		int needsInDatabase = 0;
		
		String sql = "select cat_id, special_need_id, name"
				+ " from cat_special_need join special_need on "
				+ "special_need_id = special_need.id where cat_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, c.getId());
		
		ResultSet rs = pstmt.executeQuery();
		
		// this set will end up including needs that are in
		// the database that are no longer to be associated
		// with this cat anymore (after the update)
		Set<SpecialNeed> needsToDelete = new HashSet<>();
		
		// this set will end up including needs that are not
		// yet in the database, but they are on the cat now
		// after the update.
		Set<SpecialNeed> updatedNeeds = c.getSpecialNeeds();
		
		while (rs.next()) {
			SpecialNeed sn = new SpecialNeed();
			sn.setId(rs.getInt("special_need_id"));
			sn.setName(rs.getString("name"));
			
			// if the cat's current special needs do not
			// include this special need, it must be
			// deleted from the database
			if (!updatedNeeds.contains(sn)) { // "if cat DOESN'T contain"
				needsToDelete.add(sn);
			} else { // "if cat DOES contain"
				// if the database already has a certain
				// special need, we won't need to add it
				updatedNeeds.remove(sn);
			}
			
			needsInDatabase++;
		}
		
		for (SpecialNeed sn : needsToDelete) {
			sql = "delete from cat_special_need where cat_id = ? and "
					+ "special_need_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, c.getId());
			pstmt.setInt(2, sn.getId());
			int rowsAffected = pstmt.executeUpdate();
			needsInDatabase -= rowsAffected;
		}
		
		for (SpecialNeed sn : updatedNeeds) {
			sql = "insert into cat_special_need values (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, c.getId());
			pstmt.setInt(2, sn.getId());
			pstmt.executeUpdate();
			needsInDatabase++;
		}
		
		return (needsInDatabase == c.getSpecialNeeds().size());
	}
}
