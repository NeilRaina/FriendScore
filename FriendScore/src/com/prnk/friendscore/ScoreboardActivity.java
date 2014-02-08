package com.prnk.friendscore;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ScoreboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard);
        getActionBar().setTitle(R.string.scoreboardTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scoreboard, menu);
        return true;
    }
    
}
