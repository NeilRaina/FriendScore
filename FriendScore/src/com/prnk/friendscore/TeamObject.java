package com.prnk.friendscore;

public class TeamObject {
	private String name;
	private ScoreObject score;
	
	public TeamObject() {
		this.name = "@string/new_team";
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
	
	public void SetScore(int score) {
		this.score.SetScore(score);
	}
}
