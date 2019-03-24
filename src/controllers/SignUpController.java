package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DatabaseModel;

public class SignUpController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		// Get username and password
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    String confirm = request.getParameter("confirm_password");
	    
	    // check passwords
	    if(password == null) {
	    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignUpView.jsp");
			requestDispatcher.forward(request, response);
	    }else if(confirm == null) {
	    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignUpView.jsp");
			requestDispatcher.forward(request, response);
	    }else if(!password.equals(confirm)) {
	    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignUpView.jsp");
			requestDispatcher.forward(request, response);
	    }else {
	    	 // insert into database, get new id, redirect
		    int id = -1;
		    try {
		    	boolean created = DatabaseModel.insertUser(username, password.toCharArray());
		    	if(!created) {
		    		RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignUpView.jsp");
					requestDispatcher.forward(request, response);
		    	}else {
		    		id = DatabaseModel.signInUser(username, password.toCharArray());
		    		String token = "";
		    		for(int i = 0 ; i < 10 ; i++) {
		    			token += String.valueOf((int)(Math.random() * 9  + 1));
		    		}
		    		RedirectionController.tokens.put(token, id);
		    		
		    		
			    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageController");
					request.setAttribute("token", token);
			    	requestDispatcher.forward(request, response);
		    	}
		    }catch(Exception e) {
		    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignUpView.jsp");
				requestDispatcher.forward(request, response);
		    }
	    }
	}
}
