package com.prnk.friendscore;

import java.util.ArrayList;


public class TeamObject implements Comparable<TeamObject>{
	
	private long id;
	private String name;
	private ScoreObject score;
	public ArrayList<PlayerObject> players;
	private final String defaultString = "Name";
	private boolean selected;
	
	public TeamObject() {
		this.name = defaultString;
		this.score = new ScoreObject();
	}
	
	public TeamObject(String name) {
		this.name = name;
		this.score = new ScoreObject();
	}
	
	public String Name() {
		return this.name;
	}
	
	public int Score() {
		return this.score.Score();
	}
	
	public ScoreObject GetScoreObject() {
		return this.score;
	}
	
	public void SetScoreObject(ScoreObject s) {
		this.score = s;
	}
	
	public void SetName(String name) {
		this.name = name;
	}
	
	public long GetId(){
		return this.id;
	}
	
	public void SetId(long identifier){
		this.id = identifier;
	}
	
	public void SetScore(int score) {
		this.score.SetScore(score);
	}
	
	public void SetPlayers(ArrayList<PlayerObject> p){
		this.players = p;
	}
	
	public boolean Selected() {
		return this.selected;
	}
	
	public void SetSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int compareTo(TeamObject o)
	{
	     return(o.Score() - this.Score());
	}
}
