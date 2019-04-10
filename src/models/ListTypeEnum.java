package models;

public enum ListTypeEnum {
	
	FAVORITES("favorites"), TOEXPLORE("toexplore"), DONOTSHOW("donotshow");
	
	private ListTypeEnum(String type) {
		this.type = type;
	}
	
	public String type;

}
