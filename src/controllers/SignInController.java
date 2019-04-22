package controllers;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DatabaseModel;
import models.SignInResponseEnum;

@WebServlet("/SignInController")
public class SignInController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	private boolean validSignIn(SignInResponseEnum signIn, String username, String password) {
		
		if(signIn.equals(SignInResponseEnum.DOES_NOT_EXIST)) {
			System.out.println("USER DOES NOT EXIST ENTERED: " + username + " " + password );
			return false;
		}else if(signIn.equals(SignInResponseEnum.INVALID_CREDENTIALS)) {
			System.out.println("INVALID CREDENTIALS ENTERED: " + username + " " + password );
			return false;
		}
		System.out.println("VALID ENTERED: " + username + " " + password );
		return true;
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get username and password
		String username = request.getParameter("username");
	    String password = request.getParameter("password");
	    RequestDispatcher dispatch;
	    
	    try {
	    	if(username == null) {
	    		throw new Exception();
	    	}
	    	SignInResponseEnum signIn = DatabaseModel.signInUser(username, password.toCharArray());
	    	
	    	// Check if valid
		    if(validSignIn(signIn, username, password)) {
		    	int id = signIn.getId();
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
	    	dispatch = request.getRequestDispatcher("SignInView.jsp");
	    }
	    dispatch.forward(request, response);
	}
}
