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
import org.owasp.esapi.errors.AccessControlException;
import org.owasp.esapi.reference.RandomAccessReferenceMap;

import models.CollageGenerationModel;
import models.DatabaseModel;
import models.GoogleImageRequestModel;
import models.ListTypeEnum;
import models.ResponseModel;
import models.SearchTermModel;

@WebServlet("/RedirectionController")
public class RedirectionController extends HttpServlet {
	
	//public static Map<Integer, ResponseModel> responses = new HashMap<>();
	public static RandomAccessReferenceMap responsesRandom = new RandomAccessReferenceMap(100);
	
	
	public static Map<String, Integer> tokens = new HashMap<>();
	
	//private static int index = 0;
	
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//This servlet handles redirection from one page to another. 
		String action = request.getParameter("action");
		String indexRandom = request.getParameter("index");
		String term = request.getParameter("term");
		String token = request.getParameter("token");
		RequestDispatcher dispatch = null;
		
		System.out.println(token);
		

		request.setAttribute("term", term);
		if(action == null || action.isEmpty() || indexRandom == null || indexRandom.isEmpty()) {
			dispatch = request.getRequestDispatcher("SearchPageView.jsp");
		} else if (action.equals("addtogrocery")) {
			String ingredientindex = request.getParameter("ingredientindex");
			int userid = tokens.get(token);
			String item = request.getParameter("item");
			int itemint = Integer.parseInt(item);
			try {
				if(indexRandom.equals("DROP DATABASE")) {
					throw new Exception();
				}
				// Get the random reference direct object
				ResponseModel responseModel = (ResponseModel)responsesRandom.getDirectReference(indexRandom);
				responseModel.addToGroceryList(itemint, userid, ingredientindex);
			} catch (Exception e) {
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
			ResponseModel responseModel = getDirectReference(indexRandom);
			responseModel.sort();
			request.setAttribute("list", list);
			request.setAttribute("response", responseModel);
			request.setAttribute("index", responsesRandom.getIndirectReference(responseModel));
			
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
			ResponseModel responseModel = getDirectReference(indexRandom);
			request.setAttribute("index", responsesRandom.getIndirectReference(responseModel));
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			request.setAttribute("item", itemInt);
			request.setAttribute("token", token);
			String reci_page = request.getParameter("reci_page");
			request.setAttribute("reci_page", reci_page);
			request.setAttribute("response", responseModel.getFormattedDetailedRecipeAt(itemInt));
			
		} else if(action.equals("restaurant")) { //If it is redirecting to the restaurant page,  set the attributes accordingly
			dispatch = request.getRequestDispatcher("DetailedRestaurantView.jsp");
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			ResponseModel responseModel = getDirectReference(indexRandom);
			request.setAttribute("index", responsesRandom.getIndirectReference(responseModel));
			request.setAttribute("item", itemInt);
			request.setAttribute("token", token);
			request.setAttribute("response", responseModel.getFormattedDetailedRestaurantAt(itemInt));
			
		} else if(action.equals("results")) { //If it is redirecting to the results page,  set the attributes accordingly
			dispatch = request.getRequestDispatcher("ResultsPageView.jsp");
			ResponseModel rm = getDirectReference(indexRandom);
			rm.sort();
			request.setAttribute("response", rm);
			
			request.setAttribute("index", responsesRandom.getIndirectReference(rm));
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
			request.setAttribute("term", term);
			request.setAttribute("length", urllist.size());
			request.setAttribute("jsonarray", jsArray);
			request.setAttribute("page", "1");
			
		} else if(action.equals("erase")) { //If the erase button is clicked, update database
			
		} else if(action.equals("addtolist") || action.equals("movetolist")) { //If the addtolist or movetolist button is clicked, update database
			
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			
			ResponseModel responseModel = getDirectReference(indexRandom);
			responseModel.addToList(itemInt, list, type, true);

		} else if(action.equals("removefromlist")) { // If the removefromlist button is clicked, update database
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			ResponseModel responseModel = getDirectReference(indexRandom);
			responseModel.addToList(itemInt, list, type, false);

		}else if(action.equals("moveplaceinlist")){
			
			String item = request.getParameter("item");
			int itemInt = Integer.parseInt(item);
			
			String list = request.getParameter("list");
			String type = request.getParameter("type");
			int oldPlace = Integer.parseInt(request.getParameter("oldplace"));
			int newPlace = Integer.parseInt(request.getParameter("newplace"));
			ResponseModel responseModel = getDirectReference(indexRandom);
			responseModel.moveUpDownList(itemInt, list, type, oldPlace, newPlace);
		}else if(action.contentEquals("managegrocerylist")) {
			dispatch = request.getRequestDispatcher("GroceryListView.jsp");
			ResponseModel responseModel = getDirectReference(indexRandom);
			request.setAttribute("response", responseModel);
			request.setAttribute("index", responsesRandom.getIndirectReference(responseModel));
			token = request.getParameter("token");
			request.setAttribute("token", token);
			request.setAttribute("term", term);
			int userId = RedirectionController.tokens.get(token);
			request.setAttribute("groceries", responseModel.getGroceryList(userId));

		}else if(action.contentEquals("deletegrocery")) {
			String item = request.getParameter("item");
			int userId = RedirectionController.tokens.get(token);
			ResponseModel responseModel = getDirectReference(indexRandom);
			responseModel.deleteFromGroceryList(userId, item);
		}
		
		
		if (dispatch != null) {
			dispatch.forward(request, response);	
		}
	}
	
	public static synchronized String addResponse(ResponseModel rm) {
		return responsesRandom.addDirectReference(rm);
	}
	
	public static ResponseModel getDirectReference(String indexRandom) {
		ResponseModel responseModel = null;
		try {
			if(indexRandom.equals("DROP DATABASE")) {
				throw new AccessControlException(indexRandom, "SQL attack detected");
			}
			responseModel = (ResponseModel)responsesRandom.getDirectReference(indexRandom);
		}catch(AccessControlException e) {
			
		}
		return responseModel;
	}
	
	public static synchronized void removeResponse(String index) {
		try {
			if(index.equals("DROP DATABASE")) {
				throw new AccessControlException(index, "SQL attack detected");
			}
			ResponseModel rm = (ResponseModel)responsesRandom.getDirectReference(index);
			responsesRandom.removeDirectReference(rm);
		}catch(AccessControlException e) {
			
		}
		
	}
}