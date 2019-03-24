package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DatabaseModel;

public class SignInController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get username and password
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    
	    int id = -1;
	    
	    try {
	    	id = DatabaseModel.signInUser(username, password.toCharArray());
	    	
	    	// Check if valid
		    if(id != -1) {
		    	String token = "";
	    		for(int i = 0 ; i < 100 ; i++) {
	    			token += String.valueOf((int)(Math.random() * 9  + 1));
	    		}
	    		RedirectionController.tokens.put(token, id);
	    		
		    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageController");
				request.setAttribute("id", id);
		    	requestDispatcher.forward(request, response);
		    }else {
		    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignInView.jsp");
				requestDispatcher.forward(request, response);
		    }
		    
	    }catch(Exception e) {
	    	e.printStackTrace();
	    	RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignInView.jsp");
			requestDispatcher.forward(request, response);
	    }
	
	}
}
