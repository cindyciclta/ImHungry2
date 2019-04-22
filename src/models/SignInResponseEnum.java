package models;

public enum SignInResponseEnum {
	INVALID_CREDENTIALS, DOES_NOT_EXIST, VALID;
	public void setId(int id) {
		this.id = id;
	}
	private int id = -1;
	public int getId() {return id;}
}
