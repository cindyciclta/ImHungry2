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
 		try {
 			if (restaurants.size() > 0) {
 				System.out.println("Using cached restaurants");
 		 		this.results = restaurants;
 			}else {
 				results.clear();
 				super.checkParameters(term, limit, radius);
 				super.completeTask();
 				results = super.getResults();
 				if(term.equals("DROP DATABASE")) {
 					throw new Exception();
 				}
 				for(RestaurantModel model : this.results) {
 					DatabaseModel.insertRestaurant(model, searchId);
 				}
 				
 			}
 			List<RestaurantModel> listItems = DatabaseModel.getRestaurantsInList(searchId);
 			
 			// add to listItems
 			for(RestaurantModel item : listItems) {
 				this.listItems.add(item);
 			}
 			
 		    // Recorrelate against the list
 			for(RestaurantModel item : listItems) {
 				for(int i = 0 ; i < results.size() ; i++) {
 					if(item.equals(results.get(i))) {
 						results.get(i).setInFavorites(item.isInFavorites());
 						results.get(i).setInDoNotShow(item.isInDoNotShow());
 						results.get(i).setInToExplore(item.isInToExplore());
 					}
 				}
 				
 			}
 		}catch(Exception e) {
 			
 		}
		
		return responseResult;	
	}
	
	@Override
	public boolean setFavoriteResult(int i, boolean value) {
		boolean ret = super.setFavoriteResult(i,  value);
		return updateList(i) && ret;
	}
	
	@Override
	public boolean setToExploreResult(int i, boolean value) {
		boolean ret = super.setToExploreResult(i,  value);
		return updateList(i) && ret;
	}
	
	@Override
	public boolean setDoNotShowResult(int i, boolean value) {
		boolean ret = super.setDoNotShowResult(i,  value);
		return updateList(i) && ret; 
	}
	
	private boolean updateList(int i) {
		boolean ret = true;
		try {
			if(!ret) {
				throw new Exception();
			}
			if(!results.get(i).isInToExplore() && !results.get(i).isInFavorites() 
					&& !results.get(i).isInDoNotShow()) {
				ret = DatabaseModel.deleteRestaurant(results.get(i), searchId);
			}
			ret = DatabaseModel.insertRestaurantIntoList(searchId, results.get(i));
		}catch(Exception e) {
			
		}
		return ret;
	}
}
