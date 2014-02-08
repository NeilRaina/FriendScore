package com.prnk.friendscore;

import android.app.Activity;
import android.os.Bundle;

public class CreateActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create);
		
		getActionBar().setTitle(R.string.scoreboardNewGame);
	}

}
