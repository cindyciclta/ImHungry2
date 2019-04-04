package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DatabaseModel;

@WebServlet("/SignInController")
public class SignInController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get username and password
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    RequestDispatcher dispatch;
	    
	    int id = -1;
	    
	    try {
	    	id = DatabaseModel.signInUser(username, password.toCharArray());
	    	
	    	// Check if valid
		    if(id != -1 && !username.trim().equals("") && !password.trim().equals("")) {
		    	String token = "";
	    		for(int i = 0 ; i < 10 ; i++) {
	    			token += String.valueOf((int)(Math.random() * 9  + 1));
	    		}
	    		RedirectionController.tokens.put(token, id);
	    		
		    	dispatch = request.getRequestDispatcher("SearchPageController");
				request.setAttribute("token", token);
		    }
		    else {
		    	dispatch = request.getRequestDispatcher("SignInView.jsp");
		    }
	    } catch(Exception e) {
	    	System.out.println("exception signing in: " + e.getLocalizedMessage());
	    	e.printStackTrace();
	    	dispatch = request.getRequestDispatcher("SignInView.jsp");
	    }
	    dispatch.forward(request, response);
	}
}
