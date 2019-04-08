package controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import models.CollageGenerationModel;
import models.DatabaseModel;
import models.GoogleImageRequestModel;
import models.ResponseModel;
import models.SearchTermModel;

@WebServlet("/ResultsPageController")
public class ResultsPageController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String action = request.getParameter("action");
		String index = request.getParameter("index");
		RequestDispatcher dispatch;
		
		if(action == null || action.isEmpty() || action.equals("search")) {
			if(index != null && !index.isEmpty()) {
				RedirectionController.removeResponse(Integer.parseInt(index));
			}
			
			String token = request.getParameter("token");
			if(token == null) {
				dispatch = request.getRequestDispatcher("SignInView.jsp");
			} else {
				dispatch = request.getRequestDispatcher("SearchPageController");
				request.setAttribute("token", token);
			}
		} else if(action.equals("results")) {
			
			String term = request.getParameter("term");
			String limit = request.getParameter("limit");
			String radius = request.getParameter("radius"); //Maybe need to add a check to see if empty
			String token = request.getParameter("token");
			String reci_page = request.getParameter("reci_page");
			String rest_page = request.getParameter("rest_page");
			if(token.isEmpty()) {
				dispatch = request.getRequestDispatcher("SignInView.jsp");
			} else if(term == null || limit == null || term.isEmpty() || limit.isEmpty()) {
				dispatch = request.getRequestDispatcher("SearchPageController");
				request.setAttribute("token", token);
			} else {
				int limitInteger = Integer.parseInt(limit);
				ResponseModel rm = new ResponseModel();
				CollageGenerationModel collagemodel = new CollageGenerationModel();
				GoogleImageRequestModel googleimagemodel = new GoogleImageRequestModel(collagemodel);
	
				googleimagemodel.APIImageSearch(term);
				
				if(!rm.checkParameters(term, limitInteger, Integer.parseInt(radius)) || !rm.getSearchResults()) {
					dispatch = request.getRequestDispatcher("SearchPageView.jsp");
					request.setAttribute("token", token);
				} else {
					dispatch = request.getRequestDispatcher("ResultsPageView.jsp");
					int indexInt = RedirectionController.addResponse(rm);
					
					ArrayList<String> urllist = (ArrayList<String>) collagemodel.getList();
					
					JSONArray jsArray = new JSONArray (urllist);
					
					Integer id = RedirectionController.tokens.get(token);
					Vector<SearchTermModel> searchHistory = new Vector<>();
					try {
						// Check that ID is not a negative number
						if(id < 0) {
							throw new Exception();
						}
						searchHistory = DatabaseModel.GetSearchHistory(id);
						boolean responsedb = DatabaseModel.AddSearchToHistory(id, term, Integer.parseInt(limit), Integer.parseInt(radius), urllist);
					} catch (Exception e1) {
						System.out.println("Search history exception: " + e1.getLocalizedMessage());
					}
					request.setAttribute("searches", searchHistory);
					request.setAttribute("token", token);
					request.setAttribute("response", rm);
					request.getSession().setAttribute("limit", limit);
					request.getSession().setAttribute("radius", radius);
					request.setAttribute("index", indexInt);
					request.setAttribute("term", term);
					request.setAttribute("length", urllist.size());
					request.setAttribute("jsonarray", jsArray);
					request.setAttribute("reci_page", reci_page);
					request.setAttribute("rest_page", rest_page);
				}
			}
		} else {
			dispatch = request.getRequestDispatcher("SignInView.jsp");
		}
		dispatch.forward(request, response);
	}
}