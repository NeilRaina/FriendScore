package com.prnk.friendscore;

public class PlayerObject {
	private int id;
	private String firstName;
	private String lastName;
	private final String defaultString = "default";
	
	public PlayerObject(){
		this.firstName = defaultString;
		this.lastName = defaultString;
	}
	
	public PlayerObject(String first, String last){
		this.firstName = first;
		this.lastName = last;
	}
	
	public int GetId(){
		return this.id;
	}

	public void SetId(int id){
		this.id = id;
	}
	
	public void SetName(String first, String last){
		this.firstName = first;
		this.lastName = last;
	}
	
	public String GetFirstName(){
		return this.firstName;
	}
	
	public String GetLastName(){
		return this.lastName;
	}
}
