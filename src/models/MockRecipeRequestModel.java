package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *Used for caching requests to Edamam
 */
public class MockRecipeRequestModel extends EdamamRequestModel{

	private int searchId;
	
	
	public MockRecipeRequestModel(int searchId) {
		this.searchId = searchId;
	}

	public List<RecipeModel> ExistRequest(String term, int limit) {
		try {
			if(term.equals("DROP DATABASE")) {
				throw new Exception();
			}
			
			return DatabaseModel.getRecipesFromSearchTerm(term, limit);
		}catch(Exception e) {
			
		}
		return new ArrayList<RecipeModel>();
	}
	
	@Override
	public ResponseCodeModel completeTask() {
 		ResponseCodeModel responseResult = ResponseCodeModel.OK;
 		List<RecipeModel> result = ExistRequest(term, limit);
 		try {
 			if (result.size() > 0) {
 				System.out.println("Using cached recipes");
 		 		this.results = result;
 			}else {
 				super.checkParameters(term, limit);
 				responseResult = super.completeTask();
 				this.results = super.getResults();
 				if(term.equals("DROP DATABASE")) {
 					throw new Exception();
 				}
 				
 				for(RecipeModel recipe : this.results) {
 					DatabaseModel.insertRecipe(recipe, searchId);
 				}
 			}
 			recreateList();
 			
 			
 		}catch(Exception e) {
 		}
 		
 		return responseResult;
	}
	
	private void recreateList() throws Exception{
		List<RecipeModel> listItems = DatabaseModel.getRecipesInList(searchId);
			
			// add to listItems
			for(RecipeModel item : listItems) {
				this.listItems.add(item);
			}
			
		    // Recorrelate against the list
			for(RecipeModel item : listItems) {
				for(int i = 0 ; i < results.size() ; i++) {
					if(item.equals(results.get(i))) {
						results.get(i).setInFavorites(item.isInFavorites());
						results.get(i).setInDoNotShow(item.isInDoNotShow());
						results.get(i).setInToExplore(item.isInToExplore());
					}
				}
				
			}
	}
	
	@Override
	public boolean setFavoriteResult(int i, boolean value) {
		boolean ret = super.setFavoriteResult(i,  value);
		return ret && updateList(i);
	}
	
	@Override
	public boolean setToExploreResult(int i, boolean value) {
		boolean ret = super.setToExploreResult(i,  value);
		return ret && updateList(i);
	}
	
	@Override
	public boolean setDoNotShowResult(int i, boolean value) {
		boolean ret = super.setDoNotShowResult(i,  value);
		return ret && updateList(i);
	}
	
	private boolean updateList(int i) {
		boolean ret = true;
		try {
			if(!ret) {
				throw new Exception();
			}
			if(!results.get(i).isInToExplore() && !results.get(i).isInFavorites() 
					&& !results.get(i).isInDoNotShow()) {
				ret = DatabaseModel.deleteRecipe(results.get(i), searchId);
			}
			ret = DatabaseModel.insertRecipeIntoList(searchId, results.get(i));
			recreateList();
		}catch(Exception e) {
			
		}
		return ret;
	}
	
	@Override
	public boolean moveUpDownList(int i, int oldPlace, int newPlace, String list) {
		try {
			int itemId = DatabaseModel.getItemIdFromRecipe(listItems.get(i));
			DatabaseModel.updatePlaceOnMoveUpDown(itemId, searchId, list, oldPlace, newPlace,
					"recipe");
			recreateList();
		}catch(Exception e) {
			
			return false;
		}
		return true;
	}
}