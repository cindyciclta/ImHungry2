package models;

import java.util.List;
import java.util.Map;

public class ResponseModel {
	
	private int searchId; 
	private int userId;
	
	private int limit;
	private int radius;
	
	private String term;
	private ApiCallInterface<RestaurantModel> restaurants  = new MockYelpRequestModel(-1);
	private ApiCallInterface<RecipeModel> recipes = new MockRecipeRequestModel(-1);
	
	public ResponseModel(int userId) {
		this.userId = userId;
	}
	
	public int getSearchId() {
		return searchId;
	}
	
	public String getSearchTerm() {
		return term;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void sort() {
		recipes.sort();
		restaurants.sort();
	}
	
	public Map<String, String> getFormattedDetailedRecipeAt(int i) {
		return recipes.getFormattedDetailsFieldsAt(i);
	}
	
	public Map<String, String> getFormattedDetailedRestaurantAt(int i) {
		return restaurants.getFormattedDetailsFieldsAt(i);
	}
	
	public Map<String, String> getFormattedRecipeResultsAt(int i) {
		return recipes.getFormattedResultsFieldsAt(i);
	}
	
	public Map<String, String> getFormattedRestaurantResultsAt(int i) {
		return restaurants.getFormattedResultsFieldsAt(i);
	}

	
	public int getNumberOfRecipes() {
		return recipes.getResultsSize();
	}
	public int getNumberOfRestaurants() {
		return restaurants.getResultsSize();
	}
	
	public int getNumberOfListItems() {
		return recipes.getListSize() + restaurants.getListSize();
	}
	
	public Map<String, String> getFormattedResultListAt(int i){
		for(int k = 0 ; k < restaurants.getListSize() ; k++) {
			if(restaurants.getFormattedResultsFieldsListAt(k).get("place").equals(
					Integer.toString(i+1))) {
				return restaurants.getFormattedResultsFieldsListAt(k);
			}
		}
		
		for(int k = 0 ; k < recipes.getListSize() ; k++) {
			if(recipes.getFormattedResultsFieldsListAt(k).get("place").equals(
					Integer.toString(i+1))) {
				return recipes.getFormattedResultsFieldsListAt(k);
			}
		}
		
		return null;
	}
	
	public boolean checkParameters(String term, int limit) {
		if(limit < 0) {
			return false;
		}
		
		if(term == null) {
			return false;
		}
		term = term.trim();
		if(term.isEmpty()) {
			return false;
		}
		this.term = term;
		this.limit = limit;
		return true;
	}
	
	public boolean checkParameters(String term, int limit, int radius) {
		boolean checked = checkParameters(term, limit);
		if(!checked || radius < 0) {
			return false;
		}
		this.radius = radius;
		return checked;
	}
	
	public boolean getSearchResults() {
		boolean response = true;
		
		try {
			if(userId < 0) {
				throw new Exception();
			}
			searchId = DatabaseModel.getSearchIdUser(userId, term, limit, radius);
			if(searchId == -1) {
				searchId = DatabaseModel.AddSearchTermToHistory(userId, term, limit, radius);
			}else {
				int idTemp = DatabaseModel.AddSearchTermToHistory(userId, term, limit, radius);
			}
			
			MockRecipeRequestModel edamam = new MockRecipeRequestModel(searchId);
			response = edamam.checkParameters(term, limit);
			ResponseCodeModel responseCode = edamam.completeTask();
			this.recipes = edamam;
			
			MockYelpRequestModel yelp = new MockYelpRequestModel(searchId);
			response = yelp.checkParameters(term, limit,radius);
			responseCode = yelp.completeTask();
			this.restaurants = yelp;
			
		}catch(Exception e) {
			
		}
		
		// TODO: add the google images API here
		return response;
	}
	
	public boolean addToList(int i, String list, String type, boolean value) {
		if(type.equals("restaurant")) {
			if(list.equals("donotshow")) {
				return restaurants.setDoNotShowResult(i, value);
			} else if(list.equals("favorites")) {
				return restaurants.setFavoriteResult(i, value);
			} else {
				return restaurants.setToExploreResult(i, value);
			}
		}else {
			if(list.equals("donotshow")) {
				return recipes.setDoNotShowResult(i, value);
			} else if(list.equals("favorites")) {
				return recipes.setFavoriteResult(i, value);
			} else if(list.equals("toexplore")){
				return recipes.setToExploreResult(i, value);
			}
		}
		return true;
	}
	public boolean addToGroceryList(int i, int userid, String ingredientindex) throws Exception {
		
		List<IngredientModel> list = recipes.getIngredients(i);
		int k = Integer.parseInt(ingredientindex);
		IngredientModel ingredient = list.get(k);
		System.out.println(ingredient.getIngredientName());
		//add to database
		DatabaseModel.InsertIntoGroceryList(userid, ingredient.getIngredientName());

		return true;
	}
}