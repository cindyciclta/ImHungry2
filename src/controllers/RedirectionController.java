package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

public class RedirectionController extends HttpServlet {
	
	/**
	 * 
	 */
	public static Map<Integer, ResponseModel> responses = new HashMap<>();
	private static int index = 0;
	
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//This servlet handles redirection from one page to another. 
		
		String action = request.getParameter("action");
		String index = request.getParameter("index");
		String term = request.getParameter("term");
		

		request.setAttribute("term", term);
		if(action == null || action.isEmpty() || index == null || index.isEmpty()) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("SearchPageView.jsp");
			requestDispatcher.forward(request, response);
			
		}else if(action.equals("managelist")) { //If it is redirecting to the manage list page, set the attributes accordingly
			System.out.println("managing list section");
			String list = request.getParameter("list");
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ManageListView.jsp");
			request.removeAttribute("title");
			if(list.equals("donotshow")) {
				request.setAttribute("title", "Do Not Show");
			}else if(list.equals("favorites")) {
				request.setAttribute("title", "Favorites");
			}else {
				request.setAttribute("title", "To Explore");
			}
			request.setAttribute("response", responses.get(Integer.parseInt(index)));
			request.setAttribute("index", Integer.parseInt(index));
			
			//Call the request to pull data to go back to the results page
			CollageGenerationModel collagemodel = new CollageGenerationModel();
			GoogleImageRequestModel googleimagemodel = new GoogleImageRequestModel(collagemodel);
			googleimagemodel.APIImageSearch(term);
			ArrayList<String> urllist = (ArrayList<String>) collagemodel.getList();
			JSONArray jsArray = new JSONArray (urllist);
			System.out.println("usjdof "+ urllist.size() );
			request.setAttribute("length", urllist.size());
			request.setAttribute("jsonarray", jsArray);
			
			requestDispatcher.forward(request, response);
		}else if(action.equals("recipe")) { //If it is redirecting to the recipe page, set the attributes accordingly
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("DetailedRecipeView.jsp");
			int indexInt = Integer.parseInt(index);
			request.setAttribute("index", indexInt);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			request.setAttribute("item", itemInt);
			request.setAttribute("response", responses.get(indexInt).getFormattedDetailedRecipeAt(itemInt));
			requestDispatcher.forward(request, response);
			
		}else if(action.equals("restaurant")) { //If it is redirecting to the restaurant page,  set the attributes accordingly
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("DetailedRestaurantView.jsp");
			int indexInt = Integer.parseInt(index);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			request.setAttribute("index", indexInt);
			request.setAttribute("item", itemInt);
			request.setAttribute("response", responses.get(indexInt).getFormattedDetailedRestaurantAt(itemInt));
			requestDispatcher.forward(request, response);
			
		}else if(action.equals("results")) { //If it is redirecting to the results page,  set the attributes accordingly
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ResultsPageView.jsp");
			ResponseModel rm = responses.get(Integer.parseInt(index));
			rm.sort();
			request.setAttribute("response", rm);
			int indexInt = Integer.parseInt(index);
			request.setAttribute("index", indexInt);
			
			//Call the request to pull data to go back to the results page
			CollageGenerationModel collagemodel = new CollageGenerationModel();
			GoogleImageRequestModel googleimagemodel = new GoogleImageRequestModel(collagemodel);
			googleimagemodel.APIImageSearch(term);
			ArrayList<String> urllist = (ArrayList<String>) collagemodel.getList();
			JSONArray jsArray = new JSONArray (urllist);

			request.setAttribute("length", urllist.size());
			request.setAttribute("jsonarray", jsArray);
			
			requestDispatcher.forward(request, response);
			
		}else if(action.equals("erase")) { //If the erase button is clicked, update database
			int indexInt = Integer.parseInt(index);
			RedirectionController.removeResponse(indexInt);
			
		}else if(action.equals("addtolist") || action.equals("movetolist")) { //If the addtolist or movetolist button is clicked, update database
			
			int indexInt = Integer.parseInt(index);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			
			responses.get(indexInt).addToList(itemInt, list, type, true);
		}else if(action.equals("removefromlist")) { // If the removefromlist button is clicked, update database
			int indexInt = Integer.parseInt(index);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			responses.get(indexInt).addToList(itemInt, list, type, false);
		}
	}
	
	public static synchronized int addResponse(ResponseModel rm) {
		responses.put(++index, rm);
		return index;
	}
	
	public static synchronized void removeResponse(int index) {
		responses.remove(index);
	}
	
	
	
}