package com.revature.data;

import java.util.Set;

import com.revature.beans.Status;

public interface StatusDAO {
	public Integer createStatus(Status s);
	public Status getStatusByName(String name);
	public Set<Status> getStatuses();
}
