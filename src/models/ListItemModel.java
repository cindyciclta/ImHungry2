package models;

/**
 * Existance of class is deviation from design. Used to 
 * remove session storage from JS. Subclassed by 
 * RestaurantModel and RecipeModel.
 * @author veda
 *
 */
public class ListItemModel {
	
	private boolean inFavorites;
	
	private boolean inToExplore;
	
	private boolean inDoNotShow;
	
	private int order;
	private boolean inGrocerylist;
	
	public ListItemModel() {
		inFavorites = false;
		inToExplore = false;
		inDoNotShow = false;
	}

	public boolean isInFavorites() {
		return inFavorites;
	}

	public void setInFavorites(boolean inFavorites) {
		this.inFavorites = inFavorites;
		if(this.inFavorites) {
			this.inToExplore = false;
			this.inDoNotShow = false;
		}
	}

	public boolean isInToExplore() {
		return inToExplore;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public int getOrder() {return order;}

	public void setInToExplore(boolean inToExplore) {
		this.inToExplore = inToExplore;
		if(this.inToExplore) {
			this.inDoNotShow = false;
			this.inFavorites = false;
		}
	}

	public boolean isInDoNotShow() {
		return inDoNotShow;
	}

	public void setInDoNotShow(boolean inDoNotShow) {
		this.inDoNotShow = inDoNotShow;
		if(this.inDoNotShow) {
			this.inFavorites = false;
			this.inToExplore = false;
		}
	}
	public boolean isinGrocerylist() {
		return inGrocerylist;
	}
	public void setGroceryList(boolean inGrocerylist) {
		this.inGrocerylist = inGrocerylist;
	}
}