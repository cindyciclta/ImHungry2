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
 		System.out.println("FOUND BUG: CACHED RECIPES NOT WORKING - (CINDY). WILL FIX LATER");
//		if (result.size() > 0) {
//			System.out.println("Using cached recipes");
//	 		this.results = result;
//			return responseResult;
//		}else {
			super.checkParameters(term, limit);
			responseResult = super.completeTask();
			this.results = super.getResults();
			try {
				if(term.equals("DROP DATABASE")) {
					throw new Exception();
				}
				
				for(RecipeModel recipe : this.results) {
					DatabaseModel.insertRecipe(recipe, searchId);
				}
			}catch(Exception e) {
				
			}
//		}
		
		
		return responseResult;
	}
}