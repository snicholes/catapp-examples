package com.revature.data;

import java.util.Set;

import com.revature.beans.Cat;
import com.revature.beans.Status;

public interface CatDAO {
	// CRUD operations
	public Integer createCat(Cat c); // Create
	public Cat getCatById(Integer id); // Read
	public Set<Cat> getCatsByStatus(Status s); // Read
	public void updateCat(Cat c); // Update
	public void deleteCat(Cat c); // Delete
}
