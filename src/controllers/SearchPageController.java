package controllers;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchPageController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check id is correct
		String token = request.getParameter("token");
		Integer id = RedirectionController.tokens.get(token);
		if(id == null) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignInView.jsp");
			requestDispatcher.forward(request, response);
		}else {
			String action = request.getParameter("action");
			
			// Search action
			if(action == null || action.isEmpty() || action.equals("redirect")) {
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageView.jsp");
				requestDispatcher.forward(request, response);
			} else if(action.equals("search")) {
				// Results action
				String term = request.getParameter("term");
				String limit = request.getParameter("limit");
				if(term == null || limit == null || term.isEmpty() || limit.isEmpty()) {
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageView.jsp");
					requestDispatcher.forward(request, response);	
				}else {
					String decodedValue = URLDecoder.decode(term, "UTF-8");
					term.trim();
					System.out.println(term);
					System.out.println(decodedValue);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("ResultsPageController?action=results&term=" +term+ "&limit=" + limit);
					requestDispatcher.forward(request, response);
				}
			}else {
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageView.jsp");
				requestDispatcher.forward(request, response);
			}
		}
	}

}