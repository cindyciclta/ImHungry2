package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used for mocking requests to yelp
 *
 */
public class MockYelpRequestModel {

	public MockYelpRequestModel() {
		yelp = new YelpRequestModel();
		this.mockStorage = new HashMap <String, List<RestaurantModel>>();
	}
	public YelpRequestModel yelp;
	private Map <String,List<RestaurantModel> > mockStorage;
	private List<RestaurantModel> results;
	public List<RestaurantModel> ExistRequest(String key) {
		if (mockStorage.containsKey(key)) {
			return this.mockStorage.get(key);
		}
		return null;
	}
	
	// mocks a call to request - checks if the request is in cache
	// or caches the request fresh
	public ResponseCodeModel completeTask(String term, int limit) {
 		ResponseCodeModel responseResult = ResponseCodeModel.OK;

		if (ExistRequest(term) != null) {
	 		this.results = new ArrayList<RestaurantModel>();
	 		this.results = ExistRequest(term);
			term = "";
			limit = 5;	
			return responseResult;
		} 
		yelp.checkParameters(term, limit);
		this.mockStorage.put(term, yelp.getResults());
		return yelp.completeTask();	
	}
}
