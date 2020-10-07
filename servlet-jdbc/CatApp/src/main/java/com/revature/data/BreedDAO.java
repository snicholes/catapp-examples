package com.revature.data;

import java.util.Set;

import com.revature.beans.Breed;

public interface BreedDAO {
	public Integer createBreed(Breed b); // Create
	public Breed getBreedById(Integer id); // Read
	public Integer getBreedIDByName(String name);//Read
	public Set<Breed> getBreeds(); //read
	public void updateBreed(Breed b); // Update
	public void deleteBreed(Breed b); // Delete
}
