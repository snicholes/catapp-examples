package com.revature.services;

import java.util.Set;

import com.revature.beans.Breed;
import com.revature.beans.Cat;
import com.revature.beans.Person;
import com.revature.beans.SpecialNeed;
import com.revature.beans.Status;
import com.revature.data.BreedDAO;
import com.revature.data.CatDAO;
import com.revature.data.PersonDAO;
import com.revature.data.SpecialNeedDAO;
import com.revature.data.StatusDAO;

public class CatService {
	private CatDAO catDao;
	private BreedDAO breedDao;
	private SpecialNeedDAO specialNeedDao;
	private PersonDAO personDao;
	private StatusDAO statusDao;
	
	public CatService(CatDAO cd, BreedDAO bd, SpecialNeedDAO snd, PersonDAO pd, StatusDAO sd) {
		catDao = cd;
		breedDao = bd;
		specialNeedDao = snd;
		personDao = pd;
		statusDao = sd;
	}
	
	public Integer addCat(Cat c) {
		return catDao.createCat(c); 
	}
	
	public Cat getCatById(Integer id) {
		return catDao.getCatById(id);
	}
	
	public Set<Cat> getCatsByStatus(Status s) {
		return catDao.getCatsByStatus(s);
	}
	
	public void updateCat(Cat c) {
		catDao.updateCat(c);
	}
	
	public void deleteCat(Cat c) {
		catDao.deleteCat(c);
	}
	
	public Integer addBreed(Breed b) {
		return breedDao.createBreed(b);
	}
	
	public Set<Breed> getBreeds() {
		return breedDao.getBreeds();
	}
	
	public Integer addSpecialNeed(SpecialNeed sn) {
		return specialNeedDao.createNeed(sn);
	}
	
	public Set<SpecialNeed> getNeeds() {
		return specialNeedDao.getNeeds();
	}
	
	public Integer addStatus(Status s) {
		return statusDao.createStatus(s);
	}
	
	public Set<Status> getStatuses() {
		return statusDao.getStatuses();
	}
	
	public void adoptCat(Cat c, Person p) {
		Status s = statusDao.getStatusByName("Adopted");
		c.setStatus(s);
		catDao.updateCat(c);
		
		Set<Cat> cats = p.getCats();
		cats.add(c);
		p.setCats(cats);
		personDao.updatePerson(p);
	}
}
