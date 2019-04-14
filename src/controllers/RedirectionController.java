package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
import models.ListTypeEnum;
import models.ResponseModel;
import models.SearchTermModel;

@WebServlet("/RedirectionController")
public class RedirectionController extends HttpServlet {
	
	public static Map<Integer, ResponseModel> responses = new HashMap<>();
	public static Map<String, Integer> tokens = new HashMap<>();
	
	private static int index = 0;
	
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//This servlet handles redirection from one page to another. 
		
		String action = request.getParameter("action");
		String index = request.getParameter("index");
		String term = request.getParameter("term");
		String token = request.getParameter("token");
		RequestDispatcher dispatch = null;
		

		request.setAttribute("term", term);
		if(action == null || action.isEmpty() || index == null || index.isEmpty()) {
			dispatch = request.getRequestDispatcher("SearchPageView.jsp");
		} else if (action.equals("addtogrocery")) {
			String ingredientindex = request.getParameter("ingredientindex");
			int userid = tokens.get(token);
			String item = request.getParameter("item");
			int itemint = Integer.parseInt(item);
			int indexint = Integer.parseInt(index);
			try {
				if(indexint < 0) {
					throw new Exception();
				}
				responses.get(indexint).addToGroceryList(itemint, userid, ingredientindex);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if(action.equals("managelist")) { //If it is redirecting to the manage list page, set the attributes accordingly
			String list = request.getParameter("list");
			dispatch = request.getRequestDispatcher("ManageListView.jsp");
			request.removeAttribute("title");
			if(list.equals("donotshow")) {
				request.setAttribute("title", "Do Not Show");
			} else if(list.equals(ListTypeEnum.FAVORITES.type)) {
				request.setAttribute("title", ListTypeEnum.FAVORITES.type);
			} else {
				request.setAttribute("title", "To Explore");
			}
			request.setAttribute("list", list);
			request.setAttribute("response", responses.get(Integer.parseInt(index)));
			request.setAttribute("index", Integer.parseInt(index));
			
			//Call the request to pull data to go back to the results page
			CollageGenerationModel collagemodel = new CollageGenerationModel();
			GoogleImageRequestModel googleimagemodel = new GoogleImageRequestModel(collagemodel);
			googleimagemodel.APIImageSearch(term);
			ArrayList<String> urllist = (ArrayList<String>) collagemodel.getList();
			JSONArray jsArray = new JSONArray (urllist);
			request.setAttribute("length", urllist.size());
			request.setAttribute("jsonarray", jsArray);
			request.setAttribute("token", token);
			
		} else if(action.equals("recipe")) { //If it is redirecting to the recipe page, set the attributes accordingly
			dispatch = request.getRequestDispatcher("DetailedRecipeView.jsp");
			int indexInt = Integer.parseInt(index);
			request.setAttribute("index", indexInt);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			request.setAttribute("item", itemInt);
			request.setAttribute("token", token);
			String reci_page = request.getParameter("reci_page");
			request.setAttribute("reci_page", reci_page);
			request.setAttribute("response", responses.get(indexInt).getFormattedDetailedRecipeAt(itemInt));
			
		} else if(action.equals("restaurant")) { //If it is redirecting to the restaurant page,  set the attributes accordingly
			dispatch = request.getRequestDispatcher("DetailedRestaurantView.jsp");
			int indexInt = Integer.parseInt(index);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			request.setAttribute("index", indexInt);
			request.setAttribute("item", itemInt);
			request.setAttribute("token", token);
			request.setAttribute("response", responses.get(indexInt).getFormattedDetailedRestaurantAt(itemInt));
			
		} else if(action.equals("results")) { //If it is redirecting to the results page,  set the attributes accordingly
			dispatch = request.getRequestDispatcher("ResultsPageView.jsp");
			int indexInt = Integer.parseInt(index);
			ResponseModel rm = responses.get(indexInt);
			rm.sort();
			request.setAttribute("response", rm);
			
			request.setAttribute("index", indexInt);
			Integer id = RedirectionController.tokens.get(token);
			
			CollageGenerationModel collagemodel = new CollageGenerationModel();
			GoogleImageRequestModel googleimagemodel = new GoogleImageRequestModel(collagemodel);
			googleimagemodel.APIImageSearch(term);
			ArrayList<String> urllist = (ArrayList<String>) collagemodel.getList();
			
			JSONArray jsArray = new JSONArray (urllist);
			
			Vector<SearchTermModel> searchHistory = new Vector<>();
			try {
				// Check that ID is not a negative number
				if(id < 0) {
					throw new Exception();
				}
				searchHistory = DatabaseModel.GetSearchHistory(id);
			} catch (Exception e1) {
			}
			
			request.setAttribute("searches", searchHistory);
			request.setAttribute("token", token);
			request.setAttribute("response", rm);
			request.getSession().setAttribute("limit", Integer.toString(rm.getLimit()));
			request.getSession().setAttribute("radius", Integer.toString(rm.getRadius()));
			request.setAttribute("index", indexInt);
			request.setAttribute("term", term);
			request.setAttribute("length", urllist.size());
			request.setAttribute("jsonarray", jsArray);
			request.setAttribute("page", "1");
			
		} else if(action.equals("erase")) { //If the erase button is clicked, update database
			int indexInt = Integer.parseInt(index);
			RedirectionController.removeResponse(indexInt);
			
		} else if(action.equals("addtolist") || action.equals("movetolist")) { //If the addtolist or movetolist button is clicked, update database
			
			int indexInt = Integer.parseInt(index);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			
			responses.get(indexInt).addToList(itemInt, list, type, true);

		} else if(action.equals("removefromlist")) { // If the removefromlist button is clicked, update database
			int indexInt = Integer.parseInt(index);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			responses.get(indexInt).addToList(itemInt, list, type, false);

		}else if(action.equals("moveplaceinlist")) {
			int indexInt = Integer.parseInt(index);
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			int oldPlace = Integer.parseInt(request.getParameter("oldplace"));
			int newPlace = Integer.parseInt(request.getParameter("newplace"));
			responses.get(indexInt).moveUpDownList(itemInt, list, type, oldPlace, newPlace);
		}
		
		
		if (dispatch != null) {
			dispatch.forward(request, response);	
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