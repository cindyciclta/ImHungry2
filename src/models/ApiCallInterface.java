package models;

import java.util.Map;

/**
 * An interface implemented by all API calls
 * 
 * T refers to the results objected within the 
 * ApiCallInterface
 */
public interface ApiCallInterface<T> {
	
	// Deviation no results
	public ResponseCodeModel completeTask();
	
	public int getResultsSize();
	
	public Map<String, String> getFormattedResultsFieldsAt(int i);
	
	public Map<String, String> getFormattedDetailsFieldsAt(int i);
	
	public boolean setFavoriteResult(int i, boolean value);
	public boolean setToExploreResult(int i , boolean value);
	public boolean setDoNotShowResult(int i, boolean value);
	
	public void sort();
}
