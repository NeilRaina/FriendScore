package com.prnk.friendscore;

public class TeamObject {
	private int id;
	private String name;
	private ScoreObject score;
	private final String defaultString = "Name";
	
	public TeamObject() {
		this.name = defaultString;
		this.score = new ScoreObject();
	}
	
	public TeamObject(String name) {
		this.name = name;
	}
	
	public String Name() {
		return this.name;
	}
	
	public int Score() {
		return this.score.Score();
	}
	
	public void SetName(String name) {
		this.name = name;
	}
	
	public int GetId(){
		return this.id;
	}
	
	public void SetID(int identifier){
		this.id = identifier;
	}
	
	public void SetScore(int score) {
		this.score.SetScore(score);
	}
}
