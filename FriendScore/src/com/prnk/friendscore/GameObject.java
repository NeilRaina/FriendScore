package com.prnk.friendscore;

import java.util.ArrayList;

public class GameObject {
	private long id;
	private String title;
	public ArrayList<TeamObject> teams;
	private final String defaultTitle = "New Scoreboard";

	public GameObject() {
		this.title = defaultTitle;
		this.teams = new ArrayList<TeamObject>();
		this.id = 0;
	}
	
	public GameObject(String title) {
		this.title = title;
		this.teams = new ArrayList<TeamObject>();
		this.id = 0;
	}
	
	public String Title() {
		return this.title;
	}
	
	public TeamObject Team(int index) {
		if(index < 0 || index >= teams.size()) {
			return null;
		}
		return this.teams.get(index);
	}
	
	public void SetTitle(String title) {
		title = title.trim();
		if(title.equals("") || title.isEmpty()) {
			this.title = defaultTitle;
		} else {
			this.title = title;
		}
	}
	
	public long GetId(){
		return this.id;
	}
	
	public void SetId(long identifier){
		this.id = identifier;
	}
	
	public void AddTeam(TeamObject team) {
		this.teams.add(team);
	}
	
	public void SetTeams(ArrayList<TeamObject> teams) {
		this.teams = teams;;
	}
	
	public void RemoveTeam(int index) {
		this.teams.remove(index);
	}
	
	public int TeamIndex(TeamObject team) {
		return this.teams.indexOf(team);
	}

	public TeamObject GetTeamByName(String name) {
		for(TeamObject team : teams) {
			if(team.Name().equals(name)) {
				return team;
			}
		}
		return null;
	}
	
	public void RemoveSelectedTeams() {
		ArrayList<TeamObject> newTeams = new ArrayList<TeamObject>();
		for(TeamObject team : teams) {
			if(!team.Selected()) {
				newTeams.add(team);
			}
		}
		this.teams = newTeams;
	}
	
	public boolean ContainsTeamName(String name) {
		for(TeamObject team : teams) {
			if(team.Name().equals(name)) {
				return true;
			}
		}
		return false;
	}
}
