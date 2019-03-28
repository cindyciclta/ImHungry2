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
}