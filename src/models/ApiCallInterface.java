package models;

import java.util.List;
import java.util.Map;

/**
 * An interface implemented by all API calls
 * 
 * T refers to the results objected within the 
 * ApiCallInterface
 */
public interface ApiCallInterface<T> {

	public ResponseCodeModel completeTask();
	
	public int getResultsSize();
	
	public Map<String, String> getFormattedResultsFieldsAt(int i);
	
	public Map<String, String> getFormattedDetailsFieldsAt(int i);
	
	public boolean setListResult(int i, boolean value, ListTypeEnum list);
	public boolean setGroceryListResult(int i, boolean value);
	
	public boolean moveUpDownList(int i, int oldPlace, int newPlace, String list);
	
	public int getListSize();
	public Map<String, String> getFormattedResultsFieldsListAt(int i);
	
	public void sort();

	public List<IngredientModel> getIngredients(int i);
	
	public List<T> getListItems();
	
	public void recreateList() throws Exception;

}
