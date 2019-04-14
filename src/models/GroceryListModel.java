package models;

import java.util.ArrayList;
import java.util.List;

public class GroceryListModel {
	private List<String> items = new ArrayList<String>();
	public void additem(String item) {
		items.add(item);
	}
	public String getItem(int index) {
		return items.get(index);
	}
	public int getSize() {return items.size();}
	
}
