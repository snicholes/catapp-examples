package com.revature.services;

import java.util.Set;

import com.revature.beans.Cat;
import com.revature.beans.Person;
import com.revature.beans.Role;
import com.revature.data.CatDAO;
import com.revature.data.PersonDAO;
import com.revature.data.RoleDAO;

public class PersonService {
	private PersonDAO personDao;
	private RoleDAO roleDao;
	private CatDAO catDao;
	
	public PersonService(PersonDAO pd, RoleDAO rd, CatDAO cd) {
		personDao = pd;
		roleDao = rd;
		catDao = cd;
	}
	
	public Integer registerAccount(Person p) {
		
		return personDao.createPerson(p);	
	}
	
	public Person logIn(String username, String password) {
		Person p = personDao.getPersonByName(username);
		if (password == p.getPassword())
			return p;
		else
			return null;		
	}
	
	public Person findPersonByName(String username) {
		return personDao.getPersonByName(username);
	}
	
	public Person findPersonById(Integer id) {
		return personDao.getPersonById(id);
	}
	
	public void updatePassword(Person p, String password) {
		p.setPassword(password);
		personDao.updatePerson(p);
	}
	
	public void deleteAccount(Person p) {
		// We may also wish to delete their cats from the database.
		Set<Cat> cats = p.getCats();
		for (Cat cat : cats)
			catDao.deleteCat(cat);
		// We can do that here, or we can do it at the DAO level.
		personDao.deletePerson(p);
	}
	
	public Set<Role> getRoles() {
		return roleDao.getRoles();
	}
}
