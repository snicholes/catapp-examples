package com.revature.delegates;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.beans.Person;
import com.revature.data.CatPostgres;
import com.revature.data.PersonPostgres;
import com.revature.data.RolePostgres;
import com.revature.services.PersonService;

/*
 * Endpoints:
 *  /user/login - (POST) log in a user
 *  			- (DELETE) log out a user
 *  /user - (POST) register a user
 * 	/user/:id - (GET) get user by id
 *            - (PUT) update a user
 *            - (DELETE) deletes a user
 */
public class LoginDelegate implements FrontControllerDelegate {
	private PersonService pServ = 
			new PersonService(new PersonPostgres(), new RolePostgres(), new CatPostgres());
	private ObjectMapper om = new ObjectMapper();
	
	@Override
	public void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = (String) req.getAttribute("path");
		
		if (path == null || path.equals("")) {
			if ("POST".equals(req.getMethod())) {
				// register a user
				Person p = (Person) om.readValue(req.getInputStream(), Person.class);
				p.setId(pServ.registerAccount(p));
				if (p.getId() == 0) {
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				} else {
					resp.getWriter().write(om.writeValueAsString(p));
					resp.setStatus(HttpServletResponse.SC_CREATED);
				}
			} else {
				resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			}
		} else if (path.contains("login")) {
			if ("POST".equals(req.getMethod()))
				logIn(req, resp);
			else if ("DELETE".equals(req.getMethod()))
				req.getSession().invalidate();
			else
				resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		} else {
			userWithId(req, resp, Integer.valueOf(path));
		}
	}
	
	private void logIn(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String username = req.getParameter("user");
		String password = req.getParameter("pass");
		
		Person p = pServ.findPersonByName(username);
		if (p != null) {
			if (p.getPassword().equals(password)) {
				req.getSession().setAttribute("person", p);
				resp.getWriter().write(om.writeValueAsString(p));
			} else {
				resp.sendError(404, "Incorrect password.");
			}
		} else {
			resp.sendError(404, "No user found with that username.");
		}
	}
	
	private void userWithId(HttpServletRequest req, HttpServletResponse resp, Integer id) throws IOException {
		switch (req.getMethod()) {
			case "GET":
				Person p = pServ.findPersonById(id);
				if (p != null) {
					resp.getWriter().write(om.writeValueAsString(p));
				} else {
					resp.sendError(404, "Person not found.");
				}
				break;
			case "PUT":
				String password = req.getParameter("pass");
				Person person = (Person) req.getSession().getAttribute("person");
				if (person != null)
					pServ.updatePassword(person, password);
				else
					resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				break;
			case "DELETE":
				Person user = om.readValue(req.getInputStream(), Person.class);
				pServ.deleteAccount(user);
				break;
			default:
				resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				break;
		}
	}

}
