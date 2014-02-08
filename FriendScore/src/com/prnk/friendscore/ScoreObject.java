package com.prnk.friendscore;

public class ScoreObject {
	private int score;
	private final int defaultScore = 0;
	
	public ScoreObject() {
		this.score = defaultScore;
	}
	
	public int Score() {
		return this.score;
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
