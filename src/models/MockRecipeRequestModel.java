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
 			
 		}catch(Exception e) {
 		}
 		
 		return responseResult;
	}
	
	@Override
	public boolean setFavoriteResult(int i, boolean value) {
		boolean ret = super.setFavoriteResult(i,  value);
		try {
			if(!ret) {
				throw new Exception();
			}
			ret = DatabaseModel.insertRecipeIntoList(searchId, results.get(i));
		}catch(Exception e) {
		}
		return ret;
	}
	
	@Override
	public boolean setToExploreResult(int i, boolean value) {
		boolean ret = super.setToExploreResult(i,  value);
		try {
			if(!ret) {
				throw new Exception();
			}
			ret = DatabaseModel.insertRecipeIntoList(searchId, results.get(i));
		}catch(Exception e) {
			
		}
		return ret;
	}
	
	@Override
	public boolean setDoNotShowResult(int i, boolean value) {
		boolean ret = super.setDoNotShowResult(i,  value);

		try {
			if(!ret) {
				throw new Exception();
			}
			ret = DatabaseModel.insertRecipeIntoList(searchId, results.get(i));
		}catch(Exception e) {
			
		}
		return ret;
	}
}