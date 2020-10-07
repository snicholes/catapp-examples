package com.revature.data;

import java.util.Set;

import com.revature.beans.SpecialNeed;

public interface SpecialNeedDAO {
	public Integer createNeed(SpecialNeed sn); // create
	public SpecialNeed getNeedById(Integer id); // read
	public Set<SpecialNeed> getNeeds(); // read
	//public Set<SpecialNeed> getNeedByName(String name); // read
	public void updateNeed(SpecialNeed sn); // update
	public void deleteNeed(SpecialNeed sn); // delete
}

/* Breakout Room: Special Need
 * Elaina Comstock, 
 * Jonah Marks, 
 * Gregory Jackson, 
 * Fredi S., 
 * musi, 
 * Brent */
