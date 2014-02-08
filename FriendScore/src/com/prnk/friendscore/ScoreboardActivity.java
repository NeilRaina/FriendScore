package com.prnk.friendscore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ScoreboardActivity extends Activity {

    private OnClickListener newGameButtonListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(ScoreboardActivity.this, CreateActivity.class);
			startActivity(intent);
		}
    	
    };

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        
        getActionBar().setTitle(R.string.scoreboardTitle);
        
        Button newGameButton = (Button)findViewById(R.id.addNewGame);
        newGameButton.setOnClickListener(newGameButtonListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scoreboard, menu);
        return true;
    }
    
}
