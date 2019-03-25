package controllers;
import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DatabaseModel;

public class SearchPageController extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check id is correct
		String token = request.getParameter("token");
		
		if(token == null) {
			token = (String)request.getAttribute("token");
		}
		
		Integer id = RedirectionController.tokens.get(token);
		if(id == null) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("SignInView.jsp");
			requestDispatcher.forward(request, response);
		}else {
			String action = request.getParameter("action");
			
			// Search action
			if(action == null || action.isEmpty() || action.equals("redirect")) {
			  RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageView.jsp");
			  request.setAttribute("token", token);
			  requestDispatcher.forward(request, response);
			} else if(action.equals("search")) {
				// Results action
				String term = request.getParameter("term");
				String limit = request.getParameter("limit");
				String radius = request.getParameter("radius");
				String page = request.getParameter("page");
				if(term == null || limit == null || term.isEmpty() || limit.isEmpty()) {
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageView.jsp");
					request.setAttribute("token", token);
					requestDispatcher.forward(request, response);	
				}
				String decodedValue = URLDecoder.decode(term, "UTF-8");
				term.trim();
				System.out.println(term);
				System.out.println(decodedValue);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("ResultsPageController?action=results&term=" +term+ "&limit=" + limit + "&radius=" + radius + "&page=" + page);
					requestDispatcher.forward(request, response);
	
				int limitt = Integer.parseInt(limit);
				int radiuss = Integer.parseInt(radius);
				int userid = RedirectionController.tokens.get(token);
				boolean responsedb;
				try {
					responsedb = DatabaseModel.AddSearchToHistory(userid, term, limitt, radiuss);
					Vector<String> searchhistory;
					
					searchhistory = DatabaseModel.GetSearchHistory(userid);
					if (searchhistory != null) {
						System.out.println("searchhsitory " + searchhistory.get(0));
					} else {
						System.out.println("searchhsitory NULL");
					}
					if (!responsedb) {
						System.out.println("Unable to add to search history");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
				
			}else {
				 RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageView.jsp");
				 request.setAttribute("token", token);
				 requestDispatcher.forward(request, response);
			}
		}
	}

}