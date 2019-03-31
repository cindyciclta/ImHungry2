package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DatabaseModel;

@WebServlet("/SignUpController")
public class SignUpController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// Get username and password
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    String confirm = request.getParameter("confirm_password");
	    RequestDispatcher dispatch;
	    
	    // check passwords
	    if(password == null || confirm == null || !password.equals(confirm) || username == null
	    		 || password.trim().equals("")) {
	    	dispatch = request.getRequestDispatcher("SignUpView.jsp");
	    } else {
	    	 // insert into database, get new id, redirect
		    int id = -1;
		    try {
		    	
		    	if(username.trim().equals("")) {
		    		throw new Exception();
		    	}
		    	
		    	boolean created = DatabaseModel.insertUser(username, password.toCharArray());
		    	if(!created) {
		    		dispatch = request.getRequestDispatcher("SignUpView.jsp");
		    	} else {
		    		id = DatabaseModel.signInUser(username, password.toCharArray());
		    		String token = "";
		    		for(int i = 0 ; i < 10 ; i++) {
		    			token += String.valueOf((int)(Math.random() * 9  + 1));
		    		}
		    		RedirectionController.tokens.put(token, id);
		    		
			    	dispatch = request.getRequestDispatcher("SearchPageController");
					request.setAttribute("token", token);
		    	}
		    } catch(Exception e) {
		    	System.out.println(e.getMessage());
		    	dispatch = request.getRequestDispatcher("SignUpView.jsp");
		    }
	    }
		dispatch.forward(request, response);
	}
}