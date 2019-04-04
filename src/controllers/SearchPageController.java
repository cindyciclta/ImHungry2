package controllers;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.DatabaseModel;
import models.SearchTermModel;

@WebServlet("/SearchPageController")
public class SearchPageController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Check id is correct
		String token = request.getParameter("token");
		RequestDispatcher dispatch;
		
		if(token == null) {
			token = (String)request.getAttribute("token");
		}
		
		Integer id = RedirectionController.tokens.get(token);
		System.out.println(id); // Do not remove this line (causes tests to pass for some reason)
		if(id == null) {
			dispatch = request.getRequestDispatcher("SignInView.jsp");
		} else {
			String action = request.getParameter("action");
			Vector<SearchTermModel> searchHistory = new Vector<>();
			try {
				// Check that ID is not a negative number
				if(id < 0) {
					throw new Exception();
				}
				searchHistory = DatabaseModel.GetSearchHistory(id);
			} catch (Exception e1) {}
			
			// Search action
			if(action == null || action.isEmpty() || action.equals("redirect") || id < 0) {
				dispatch = request.getRequestDispatcher("SearchPageView.jsp");
				request.setAttribute("token", token);
				request.setAttribute("searches", searchHistory);
			} else if(action.equals("search")) {
				// Results action
				String term = request.getParameter("term");
				String limit = request.getParameter("limit");
				String radius = request.getParameter("radius");
				String reci_page = request.getParameter("reci_page");
				String rest_page = request.getParameter("rest_page");
				if(term == null || limit == null || term.isEmpty() || limit.isEmpty()) {
					dispatch = request.getRequestDispatcher("SearchPageView.jsp");
					request.setAttribute("token", token);
					request.setAttribute("searches", searchHistory);
				} else {
					String decodedValue = URLDecoder.decode(term, "UTF-8");
					term.trim();
					System.out.println(term);
					System.out.println("_____________________"+ reci_page + " "+ rest_page);
					dispatch = request.getRequestDispatcher("ResultsPageController?action=results&term=" +term+ "&limit=" + limit + "&radius=" + radius + "&rest_page=" + rest_page + "&reci_page=" + reci_page);
					try {
						int limitt = Integer.parseInt(limit);
						int radiuss = Integer.parseInt(radius);
						int userid = RedirectionController.tokens.get(token);
						boolean responsedb = DatabaseModel.AddSearchToHistory(userid, term, limitt, radiuss);
					} catch (Exception e) {
						dispatch = request.getRequestDispatcher("SearchPageView.jsp");
						request.setAttribute("token", token);
						request.setAttribute("searches", searchHistory);
					}
				}
			} else {
				 dispatch = request.getRequestDispatcher("SearchPageView.jsp");
				 request.setAttribute("token", token);
				 request.setAttribute("searches", searchHistory);
			}
		}
		dispatch.forward(request, response);
	}
}