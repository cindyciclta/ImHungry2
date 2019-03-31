package controllers;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import models.CollageGenerationModel;
import models.GoogleImageRequestModel;
import models.ResponseModel;

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
			String page = request.getParameter("page");
			if(token.isEmpty()) {
				dispatch = request.getRequestDispatcher("SignInView.jsp");
			} else if(term == null || limit == null || term.isEmpty() || limit.isEmpty()) {
				dispatch = request.getRequestDispatcher("SearchPageView.jsp");
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

					request.setAttribute("token", token);
					request.setAttribute("response", rm);
					request.getSession().setAttribute("limit", limit);
					request.getSession().setAttribute("radius", radius);
					request.setAttribute("index", indexInt);
					request.setAttribute("term", term);
					request.setAttribute("length", urllist.size());
					request.setAttribute("jsonarray", jsArray);
					request.setAttribute("page", page);
				}
			}
		} else {
			dispatch = request.getRequestDispatcher("SignInView.jsp");
		}
		dispatch.forward(request, response);
	}
}