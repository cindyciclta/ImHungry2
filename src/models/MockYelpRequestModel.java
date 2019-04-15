package models;

import java.util.ArrayList;
import java.util.Collections;
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
 			recreateList();
 			
 		}catch(Exception e) {
 			
 		}
		
		return responseResult;	
	}
	
	
	@Override
	public void recreateList() throws Exception{
		List<RestaurantModel> listItems = DatabaseModel.getRestaurantsInList(searchId);
		this.listItems.clear();
			
			// add to listItems
			for(RestaurantModel item : listItems) {
				this.listItems.add(item);
			}
			
		    // Recorrelate against the list
			for(RestaurantModel item : listItems) {
				for(int i = 0 ; i < results.size() ; i++) {
					if(item.equals(results.get(i))) {
						results.get(i).setListItem(item);
					}
				}
			}
			Collections.sort(this.results);
	}
	
	public boolean updateList(int i) {
		boolean ret = true;
		try {
			if(i < 0) {
				ret = false;
				throw new Exception();
				
			}
			if(results.get(i).inNoList()) {
				ret = DatabaseModel.deleteRestaurant(results.get(i), searchId);
			}else {
				ret = DatabaseModel.insertRestaurantIntoList(searchId, results.get(i));
			}
			recreateList();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	@Override
	public boolean setListResult(int i, boolean value, ListTypeEnum list) {
		boolean ret = super.setListResult(i,  value, list);
		if(!ret) {
			return false;
		}
		return updateList(i);
	}

	@Override
	public boolean moveUpDownList(int i, int oldPlace, int newPlace, String list) {
		boolean ret = true;
		try {
			if(i < 0) {
				ret = false;
				throw new Exception();
			}
			int itemId = DatabaseModel.getItemIdFromRestaurant(listItems.get(i));
			DatabaseModel.updatePlaceOnMoveUpDown(itemId, searchId, list, oldPlace, newPlace,
					"restaurant");
			recreateList();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return ret;
	}
}
