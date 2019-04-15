package models;

import java.util.HashMap;
import java.util.Map;

/**
 * Existance of class is deviation from design. Used to 
 * remove session storage from JS. Subclassed by 
 * RestaurantModel and RecipeModel.
 * @author veda
 *
 */
public class ListItemModel{
	
	private Map<ListTypeEnum, Boolean> listAddition;
	
	private int order;
	private boolean inGrocerylist;
	
	public ListItemModel() {
		listAddition = new HashMap<>();
		listAddition.put(ListTypeEnum.DONOTSHOW, false);
		listAddition.put(ListTypeEnum.FAVORITES, false);
		listAddition.put(ListTypeEnum.TOEXPLORE, false);
	}
	
	public boolean isInList(ListTypeEnum list) {
		return listAddition.get(list);
	}
	
	public void setListItem(ListItemModel other) {
		listAddition.clear();
		for(ListTypeEnum list : ListTypeEnum.values()) {
			listAddition.put(list, other.listAddition.get(list));
		}
	}
	
	public boolean inNoList() {
		for(ListTypeEnum list : ListTypeEnum.values()) {
			if(listAddition.get(list)) {
				return false;
			}
		}
		return true;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public Map<String, String> getFormattedFieldsForResultsPage() {
		Map<String, String> results = new HashMap<>();
		String modifier = "";
		for(ListTypeEnum enumVal : ListTypeEnum.values()) {
			if(isInList(enumVal)) {
				modifier = enumVal.type;
			}
		}
		results.put("modifier", modifier);
		return results;
	}
	
	public int getOrder() {return order;}
	
	public void setInList(ListTypeEnum list, boolean value) {
		if(value) {
			this.listAddition.clear();
			for(ListTypeEnum enumVal : ListTypeEnum.values()) {
				if(enumVal.equals(list)) {
					listAddition.put(list, true);
				}else {
					listAddition.put(enumVal, false);
				}
			}
		}else {
			listAddition.remove(list);
			listAddition.put(list, value);
		}
	}
	
	
	public boolean isinGrocerylist() {
		return inGrocerylist;
	}
	public void setGroceryList(boolean inGrocerylist) {
		this.inGrocerylist = inGrocerylist;
	}
	
	
}