package com.revature.data;

import java.util.Set;

import com.revature.beans.Person;
import com.revature.beans.Role;

public interface PersonDAO {
	public Integer createPerson(Person p);//Create
	public Person getPersonById(Integer id);//Read
	public Person getPersonByName(String name);//Read
	public Person getPersonByUsernameAndPassword(String username, String password);
	public Set<Person> getPeopleByRole(Role r);//Read
	//public void updatePersonRole(Role r);//Update
	public void updatePerson(Person p);//Update
	public void deletePerson(Person p);//Delete
}
