package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used for Caching Requests to Yelp
 *
 */
public class MockYelpRequestModel extends YelpRequestModel{

	private int searchId;
	public MockYelpRequestModel(int searchId) {
		this.searchId = searchId;
	}
	
	public List<RestaurantModel> ExistRequest(String term, int limit, int radius) {
		try {
			if(term.equals("DROP DATABASE")) {
				throw new Exception();
			}
			return DatabaseModel.getRestaurantsFromSearchTerm(term, limit, radius);
		}catch(Exception e) {
			
		}
		return new ArrayList<RestaurantModel>();
	}
	
	// mocks a call to request - checks if the request is in cache
	// or caches the request fresh
	@Override
	public ResponseCodeModel completeTask() {
		
 		ResponseCodeModel responseResult = ResponseCodeModel.OK;
 		List<RestaurantModel> restaurants = ExistRequest(term, limit, radius);
		if (restaurants.size() > 0) {
			System.out.println("Using cached restaurants");
	 		this.results = restaurants;
			return responseResult;
		}else {
			results.clear();
			super.checkParameters(term, limit, radius);
			super.completeTask();
			results = super.getResults();
			try {
				if(term.equals("DROP DATABASE")) {
					throw new Exception();
				}
				for(RestaurantModel model : this.results) {
					DatabaseModel.insertRestaurant(model, searchId);
				}
			}catch(Exception e) {
				
			}
		}
			
		return responseResult;	
	}
}
