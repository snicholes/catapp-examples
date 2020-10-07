package com.revature.delegates;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Breed;
import com.revature.beans.Role;
import com.revature.beans.SpecialNeed;
import com.revature.beans.Status;
import com.revature.data.BreedPostgres;
import com.revature.data.CatPostgres;
import com.revature.data.PersonPostgres;
import com.revature.data.RolePostgres;
import com.revature.data.SpecialNeedPostgres;
import com.revature.data.StatusPostgres;
import com.revature.services.CatService;
import com.revature.services.PersonService;

/*
 * Endpoints:
 *  /employee/status - (GET) returns all of the statuses that a cat can have.
 *  (for cat creation)
 *          - (POST) creates a new status
 *  /employee/breed - (GET) returns all of the breeds that a cat can have.
 *  (for cat creation)
 *         - (POST) creates a new breed
 *  /employee/specialneed - (GET) returns all of the special needs a cat can have.
 *               - (POST) creates a new special need
 *  /employee/role - (GET) returns all of the roles a person can have.
 *  
 */
public class EmployeeDelegate implements FrontControllerDelegate {
	private PersonService pServ = new PersonService(new PersonPostgres(), 
			new RolePostgres(), new CatPostgres());
	private CatService cServ = new CatService(new CatPostgres(), new BreedPostgres(),
			new SpecialNeedPostgres(), new PersonPostgres(), new StatusPostgres());
	private ObjectMapper om = new ObjectMapper();
	
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = (String) req.getAttribute("path");
		
		if (path.contains("status")) {
			switch (req.getMethod()) {
				case "GET":
					Set<Status> statusset = cServ.getStatuses();
					resp.getWriter().write(om.writeValueAsString(statusset));
					break;
				case "POST":
					Status s = om.readValue(req.getInputStream(), Status.class);
					cServ.addStatus(s);
					break;
				default:
					resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					break;
			}
		}
		else if (path.contains("breed")) {
			switch (req.getMethod()) {
				case "GET":
					Set<Breed> breedset = cServ.getBreeds();
					resp.getWriter().write(om.writeValueAsString(breedset));
					break;
				case "POST":
					Breed b = om.readValue(req.getInputStream(), Breed.class);
					cServ.addBreed(b);
					break;
				default:
					resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					break;
			}
		} else if (path.contains("specialneed")) {
			switch (req.getMethod()) {
				case "GET":
					Set<SpecialNeed> specialneedset = cServ.getNeeds();
					resp.getWriter().write(om.writeValueAsString(specialneedset));
					break;
				case "POST":
					SpecialNeed sn = om.readValue(req.getInputStream(), SpecialNeed.class);
					cServ.addSpecialNeed(sn);
					break;
				default:
					resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					break;
			}
		} else if (path.contains("role")) {
			switch (req.getMethod()) {
				case "GET":
					Set<Role> rolls = pServ.getRoles();
					resp.getWriter().write(om.writeValueAsString(rolls));
					break;
				default:
					resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					break;
			}
		} else {
			resp.sendError(404);
		}
	}
}
