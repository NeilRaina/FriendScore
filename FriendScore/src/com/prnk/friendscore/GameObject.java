package com.prnk.friendscore;

import java.util.ArrayList;

public class GameObject {
	private int id;
	private String title;
	public ArrayList<TeamObject> teams;
	private final String defaultTitle = "Title";

	public GameObject() {
		this.title = defaultTitle;
		this.teams = new ArrayList<TeamObject>();
	}
	
	public GameObject(String title) {
		this.title = title;
		this.teams = new ArrayList<TeamObject>();
	}
	
	public String Title() {
		return this.title;
	}
	
	public TeamObject Team(int index) {
		return this.teams.get(index);
	}
	
	public void SetTitle(String title) {
		this.title = title;
	}
	
	public int GetId(){
		return this.id;
	}
	
	public void SetID(int identifier){
		this.id = identifier;
	}
	
	public void AddTeam(TeamObject team) {
		this.teams.add(team);
	}
	
	public void RemoveTeam(int index) {
		this.teams.remove(index);
	}
	
	public int TeamIndex(TeamObject team) {
		return this.teams.indexOf(team);
	}

}
