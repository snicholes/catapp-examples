package com.revature.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.revature.beans.Cat;
import com.revature.beans.Person;
import com.revature.beans.Role;
import com.revature.utils.ConnectionUtil;

public class PersonPostgres implements PersonDAO {
	private ConnectionUtil cu = ConnectionUtil.getConnectionUtil();
	private CatDAO catDao = new CatPostgres();
	
	@Override
	public Integer createPerson(Person p) {
		Integer id = 0;
		
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "insert into person values (default, ?, ?, ?)";
			String[] keys = {"id"};
			PreparedStatement pstmt = conn.prepareStatement(sql, keys);
			pstmt.setString(1, p.getUsername());
			pstmt.setInt(3, p.getRole().getId());
			pstmt.setString(2, p.getPassword());
			
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

	public Person getPersonById(Integer id) {
		Person person = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select person.id, person.username, person.passwd, "
					+ "user_role_id, user_role.name as "
					+ "role_name from person join user_role on "
					+ "person.id = user_role.id where person.id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				person = new Person();
				person.setId(id);
				person.setPassword(rs.getString("passwd"));
				person.setUsername(rs.getString("username"));
				
				Role r = new Role();
				r.setId(rs.getInt("user_role_id"));
				r.setName(rs.getString("role_name"));
				person.setRole(r);
				
				// get the person's cats
				sql = "select * from person_cat join cat on "
						+ "cat_id = cat.id where person_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, id);
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<Cat> setOfCats = new HashSet<>();
				while (rs2.next()) {
					Cat catPersonOwns = catDao.getCatById(rs.getInt("cat_id"));
					setOfCats.add(catPersonOwns);
				}
				person.setCats(setOfCats);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return person;
	}

	@Override
	public Person getPersonByName(String name) {
		Person person = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select person.id, person.username, person.passwd, "
					+ "user_role_id, user_role.name as "
					+ "role_name from person join user_role on "
					+ "person.id = user_role.id where username = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				person = new Person();
				person.setId(rs.getInt("id"));
				person.setPassword(rs.getString("passwd"));
				person.setUsername(rs.getString("username"));
				
				Role r = new Role();
				r.setId(rs.getInt("user_role_id"));
				r.setName(rs.getString("role_name"));
				person.setRole(r);
				
				// get the person's cats
				sql = "select * from person_cat join cat on "
						+ "cat_id = cat.id where person_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, person.getId());
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<Cat> setOfCats = new HashSet<>();
				while (rs2.next()) {
					Cat catPersonOwns = catDao.getCatById(rs2.getInt("cat_id"));
					setOfCats.add(catPersonOwns);
				}
				person.setCats(setOfCats);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return person;
	}

	@Override
	public Person getPersonByUsernameAndPassword(String username, String password) {
		Person person = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select person.id, person.username, person.passwd, "
					+ "user_role_id, user_role.name as "
					+ "role_name from person join user_role on "
					+ "person.id = user_role.id where username = ? and "
					+ "passwd = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			ResultSet rs = pstmt.executeQuery();
			
			if (rs.next()) {
				person = new Person();
				person.setId(rs.getInt("id"));
				person.setPassword(rs.getString("passwd"));
				person.setUsername(rs.getString("username"));
				
				Role r = new Role();
				r.setId(rs.getInt("user_role_id"));
				r.setName(rs.getString("role_name"));
				person.setRole(r);
				
				// get the person's cats
				sql = "select * from person_cat join cat on "
						+ "cat_id = cat.id where person_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, person.getId());
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<Cat> setOfCats = new HashSet<>();
				while (rs2.next()) {
					Cat catPersonOwns = catDao.getCatById(rs.getInt("cat_id"));
					setOfCats.add(catPersonOwns);
				}
				person.setCats(setOfCats);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return person;
	}

	@Override
	public Set<Person> getPeopleByRole(Role r) {
		Set<Person> people = null;
		
		try (Connection conn = cu.getConnection()) {
			String sql = "select person.id, person.username, person.passwd, "
					+ "user_role_id, user_role.name as "
					+ "role_name from person join user_role on "
					+ "person.id = user_role.id where user_role_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, r.getId());
			
			ResultSet rs = pstmt.executeQuery();
			
			people = new HashSet<>();
			
			while (rs.next()) {
				Person person = new Person();
				person.setId(rs.getInt("id"));
				person.setPassword(rs.getString("passwd"));
				person.setUsername(rs.getString("username"));
				
				person.setRole(r);
				
				// get the person's cats
				sql = "select * from person_cat join cat on "
						+ "cat_id = cat.id where person_id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, person.getId());
				
				ResultSet rs2 = pstmt.executeQuery();
				
				Set<Cat> setOfCats = new HashSet<>();
				while (rs2.next()) {
					Cat catPersonOwns = catDao.getCatById(rs.getInt("cat_id"));
					setOfCats.add(catPersonOwns);
				}
				person.setCats(setOfCats);
				
				people.add(person);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return people;
	}

	@Override
	public void updatePerson(Person p) {
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			String sql = "update person set username = ?, passwd = ?, "
					+ "user_role_id = ? where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, p.getUsername());
			pstmt.setString(2, p.getPassword());
			pstmt.setInt(3, p.getRole().getId());
			pstmt.setInt(4, p.getId());
			
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				if (addPersonCats(p, conn)) {
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

	@Override
	public void deletePerson(Person p) {
		try (Connection conn = cu.getConnection()) {
			conn.setAutoCommit(false);
			
			// first, we will delete the cats from the database.
			// this will also take care of removing them from
			// the person_cat table.
			for (Cat cat : p.getCats()) {
				catDao.deleteCat(cat);
			}
			
			// now, we can delete the person
			String sql = "delete from person where id = ?";
			
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p.getId());
			
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
	
	private boolean addPersonCats(Person p, Connection conn) throws SQLException {
		// this will represent which cats need to be added
		// to the person_cat link table
		Set<Integer> catIds = new HashSet<>();
		
		// so first we will grab all of the ids of the cats
		// that the updated person has
		for (Cat cat : p.getCats()) {
			catIds.add(cat.getId());
		}
		
		String sql = "select * from person_cat where person_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, p.getId());
		
		ResultSet rs = pstmt.executeQuery();
		
		// this will allow us to keep track of how many cats are in
		// the database so that we can compare at the end and ensure
		// that the correct amount of cats is there
		int catsInDatabase = 0;
		while (rs.next()) {
			Integer currentId = rs.getInt("cat_id");
			
			// if the database already has the cat, we remove it from the set
			if (catIds.contains(currentId)) {
				catIds.remove(currentId);
			}
			catsInDatabase++;
		}
		
		// now we are only left with cats that weren't already
		// in the person_cat table with this owner, so we can
		// go ahead and insert those!
		for (Integer catId : catIds) {
			sql = "insert into person_cat values (?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, p.getId());
			pstmt.setInt(2, catId);
			
			pstmt.executeUpdate();
			catsInDatabase++;
		}
		
		return catsInDatabase == p.getCats().size();
	}
}