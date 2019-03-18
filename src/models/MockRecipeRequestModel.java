package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Not production code, used for mocking requests
 * To edamam
 *
 */
public class MockRecipeRequestModel{

	public MockRecipeRequestModel() {
		this.mockStorage = new HashMap <String, List<RecipeModel>>();
		this.edamam = new EdamamRequestModel();
	}

	
	public final String URL_LINK = "https://www.allrecipes.com/search/results/?wt=";
	
	private List<RecipeModel> results;
	
	private Map <String,List<RecipeModel> > mockStorage;

	public EdamamRequestModel edamam;
	public List<RecipeModel> ExistRequest(String key) {
		if (mockStorage.containsKey(key)) {
			return this.mockStorage.get(key);
		}
		return null;
	}
	

	public ResponseCodeModel completeTask(String term, int limit) {
 		ResponseCodeModel responseResult = ResponseCodeModel.OK;

		if (ExistRequest(term) != null) {
	 		this.results = new ArrayList<RecipeModel>();
	 		this.results = ExistRequest(term);
			term = "";
			limit = 5;	
			return responseResult;
		}
		edamam.checkParameters(term, limit);
		mockStorage.put(term, edamam.getResults());
		return edamam.completeTask();
	}

	
}
