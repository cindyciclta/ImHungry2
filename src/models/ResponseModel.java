package models;

import java.util.Map;

public class ResponseModel {
	private int limit;
	private int radius;
	
	private String term;
	private ApiCallInterface<RestaurantModel> restaurants  = new YelpRequestModel();
	private ApiCallInterface<RecipeModel> recipes = new EdamamRequestModel();
	private MockRecipeRequestModel mock;
	public ResponseModel() {
		mock = new MockRecipeRequestModel();
	}
	
	public String getSearchTerm() {
		return term;
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
		
		EdamamRequestModel edamam = new EdamamRequestModel();
		response = edamam.checkParameters(term, limit);
		ResponseCodeModel responseCode = edamam.completeTask();
		this.recipes = edamam;
		
		// TODO: add the yelp API here
		YelpRequestModel yelp = new YelpRequestModel();
		response = yelp.checkParameters(term, limit,radius);
		responseCode = yelp.completeTask();
		this.restaurants = yelp;
		
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
			} else {
				return recipes.setToExploreResult(i, value);
			}
		}
	}
}