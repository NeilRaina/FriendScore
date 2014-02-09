package com.prnk.friendscore;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

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
        
        //List of games that current user is part of
        List<GameObject> games;
        
        //Some method to pull from db the list of games
        //games = GetGamesForUser(int userID)
        
        //populates list of games with some dummy data
        games = this.GetTestGames();
        
        LinearLayout scoreContents = (LinearLayout)findViewById(R.id.scoreBoardContents);
        
        for(GameObject g : games) {
        	scoreContents.addView(CreateHeader(g.Title()));
        	
        	for (TeamObject t : g.teams) {
        		scoreContents.addView(CreateLL("Horizontal", t));
        	}
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scoreboard, menu);
        return true;
    }
    
    //Create linear layout
    public LinearLayout CreateLL(String layout, TeamObject t) {
    	LinearLayout ll = new LinearLayout(this);
    	if(layout.equalsIgnoreCase("vertical")) {
    		ll.setOrientation(LinearLayout.VERTICAL);
    	}
    	else {
    		ll.setOrientation(LinearLayout.HORIZONTAL);
    	}
    	ll.addView(CreateTextView(t.Name()));
		ll.addView(CreateTextView(t.Score()));
    	
    	return ll;
    }
    
    
    //Create a Header Text given a string
    public TextView CreateHeader(String text) {
    	TextView tv = new TextView(this);
    	tv.setText(text);
    	tv.setTextSize(20);
    	return tv;
    }
    
    //Create a TextView given the specified string
    public TextView CreateTextView(String text) {
    	TextView tv = new TextView(this);
    	tv.setText(text);
    	tv.setLayoutParams(AssignParameters());
    	tv.setGravity(0x03);
    	return tv;
    }
    
  //Create a TextView given the specified int
    public TextView CreateTextView(int text) {
    	TextView tv = new TextView(this);
    	tv.setText(Integer.toString(text));
    	tv.setGravity(0x05);
    	tv.setLayoutParams(AssignParameters());
    	return tv;
    }
    
    //Assign the layout parameters for based on params
    public LayoutParams AssignParameters() {
    	LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        llp.setMargins(50, 0, 50, 0); // llp.setMargins(left, top, right, bottom);
        return llp;
    }
    
    public List<GameObject> GetTestGames() {
    	List<GameObject> games = new ArrayList<GameObject>();
    	
    	//fifa singles
    	TeamObject peterfs = new TeamObject("Peter");
    	peterfs.SetScore(2);
    	TeamObject ryanfs = new TeamObject("Ryan");
    	ryanfs.SetScore(8);
    	TeamObject neilfs = new TeamObject("Neil");
    	neilfs.SetScore(8);
    	TeamObject karofs = new TeamObject("Karo");
    	karofs.SetScore(15);
    	
    	//blobs
    	TeamObject peterbl = new TeamObject("Peter");
    	peterbl.SetScore(20);
    	TeamObject ryanbl = new TeamObject("Ryan");
    	ryanbl.SetScore(10);
    	TeamObject neilbl = new TeamObject("Neil");
    	neilbl.SetScore(8);
    	TeamObject karobl = new TeamObject("Karo");
    	karobl.SetScore(10);
    	
    	//nhl
    	TeamObject peternhl = new TeamObject("Peter");
    	peternhl.SetScore(1);
    	TeamObject ryannhl = new TeamObject("Ryan");
    	ryannhl.SetScore(1);
    	
    	//fifa doubles
    	TeamObject peterRyan = new TeamObject("WhiteGuys");
    	TeamObject neilKaro = new TeamObject("BlackGuys");
    	neilKaro.SetScore(2);
    	
    	GameObject fifaSingles = new GameObject("FIFA Singles");
    	fifaSingles.AddTeam(peterfs);
    	fifaSingles.AddTeam(ryanfs);
    	fifaSingles.AddTeam(neilfs);
    	fifaSingles.AddTeam(karofs);
    	
    	GameObject fifaDoubles = new GameObject("FIFA Doubles");
    	fifaDoubles.AddTeam(peterRyan);
    	fifaDoubles.AddTeam(neilKaro);
    	
    	GameObject blobs = new GameObject("Black Ops");
    	blobs.AddTeam(peterbl);
    	blobs.AddTeam(ryanbl);
    	blobs.AddTeam(neilbl);
    	blobs.AddTeam(karobl);
    	
    	GameObject nhl = new GameObject("NHL 2K14");
    	nhl.AddTeam(peternhl);
    	nhl.AddTeam(ryannhl);
    	
    	games.add(fifaSingles);
    	games.add(fifaDoubles);
    	games.add(blobs);
    	games.add(nhl);
    	
    	return games;
    	
    }
    
}
