package com.prnk.friendscore;

public class ScoreObject {
	private long id;
	private int score;
	private final int defaultScore = 0;
	
	public ScoreObject() {
		this.score = defaultScore;
	}
	
	public int Score() {
		return this.score;
	}
	
	public long GetId(){
		return this.id;
	}
	
	public void SetId(long identifier){
		this.id = identifier;
	}
	
	public void IncrementScore() {
		this.score++;
	}
	
	public void SetScore(int score) {
		this.score = score;
	}
	
	public void DecrementScore() {
		this.score--;
	}
	
	public void ResetScore() {
		this.score = defaultScore;
	}
}
