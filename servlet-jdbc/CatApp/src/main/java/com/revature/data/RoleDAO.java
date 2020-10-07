package com.revature.data;

import java.util.Set;

import com.revature.beans.Role;

public interface RoleDAO {
	public Integer createRole(Role r);//create
	public Role getRoleById(Integer id);//read
	public Set<Role> getRoles();//read
	public void updateRole(Role r); // Update
	public void deleteRole(Role r); // Delete
}
