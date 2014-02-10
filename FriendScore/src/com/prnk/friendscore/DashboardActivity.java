package com.prnk.friendscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DashboardActivity extends ListActivity {
	private DataBaseWrapper dbwrapper;
	private ScoreboardListAdapter adapter;
	private List<GameObject> games;

	public final int textSize = 18;
	
    private OnClickListener newGameButtonListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(DashboardActivity.this, EditActivity.class);
			startActivityForResult(intent, 0);
		}
    	
    };
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        getActionBar().setTitle(R.string.dashboardTitle);
        dbwrapper = new DataBaseWrapper(this);
        
        Button newGameButton = (Button)findViewById(R.id.addNewGame);
        newGameButton.setOnClickListener(newGameButtonListener);

        GetGames();
        SetupListView();
    }
	
	private void SetupListView() {
		getListView().setItemsCanFocus(false);
		getListView().setTextFilterEnabled(true);
		adapter = new ScoreboardListAdapter(this, R.layout.scoreboard, games);
		ListView scoreboardListView = getListView();
		scoreboardListView.setAdapter(adapter);
	}
	
	public void GetGames() {
		this.games = this.GetTestGames();
		//TODO: Use database
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		//Returned from other activity (ie create/edit), redraw scoreboards
		GetGames();
		adapter.games = this.games;
		adapter.notifyDataSetChanged();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scoreboard, menu);
        return true;
    }

    //Create the relative layout
    public RelativeLayout CreateRelativeLayoutForTeam(TeamObject team, boolean first) {
    	RelativeLayout layout = new RelativeLayout(this);

		layout.addView(CreateTextView(team.Name(), textSize, true, first));
    	layout.addView(CreateTextView(Integer.toString(team.Score()), textSize, false, first));
    	
    	return layout;
    }
    
    //Create a TextView given the specified string
    public TextView CreateTextView(String text, int textSize, boolean alignLeft, boolean first) {
    	TextView tv = new TextView(this);
    	tv.setText(text);
    	tv.setTextSize(textSize);
    	if(first) tv.setTypeface(Typeface.DEFAULT_BOLD);
    	tv.setLayoutParams(AssignParameters(alignLeft, first));
    	return tv;
    }

    //Assign the layout parameters for based on params
    public RelativeLayout.LayoutParams AssignParameters(boolean alignLeft, boolean first) {
    	RelativeLayout.LayoutParams rlp = 
    			new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 
    											RelativeLayout.LayoutParams.WRAP_CONTENT);
    	rlp.setMargins(10, first ? 10 : 0, 0, 0);
        rlp.addRule(alignLeft ? RelativeLayout.ALIGN_PARENT_LEFT : RelativeLayout.ALIGN_PARENT_RIGHT);
        return rlp;
    }
    
    public List<GameObject> GetTestGames() {
    	List<GameObject> games = new ArrayList<GameObject>();
    	games = dbwrapper.getAllGameObjects();
    	if(games.isEmpty()) {
    		//create all test games and insert into database
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
	    	
	    	for(GameObject game : games) {
	    		dbwrapper.createGame(game);
	    	}
    	}
    	return games;
    }
    
	  
	private class ScoreboardListAdapter extends ArrayAdapter<GameObject> {
		private List<GameObject> games;
		private Context context;
		
		public ScoreboardListAdapter(Context context, int gameResourceId, List<GameObject> games) {
			super(context, gameResourceId, games);
			this.context = context;
			this.games = games;
		}
		
		private class ViewHolder {
			TextView gameNameTextView;
			LinearLayout teamListView;
		}
		
		@Override
		public int getCount() {
			if(games.isEmpty()) {
				return 1;
			}
			return games.size();
		}
		
		@Override
		public boolean isEmpty () {
			return games.isEmpty();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(isEmpty()) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = inflater.inflate(R.layout.empty_game_list_item, parent, false);
			    convertView.setTag(null);
			    return convertView;
			}
			
			ViewHolder holder;
			if(convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			    convertView = inflater.inflate(R.layout.scoreboard, parent, false);
			    holder = new ViewHolder();
			    holder.gameNameTextView = (TextView) convertView.findViewById(R.id.titleHeader);
			    holder.teamListView = (LinearLayout) convertView.findViewById(R.id.scoreboardTeamList);
			    ImageButton editButton = (ImageButton) convertView.findViewById(R.id.editScoreboard);
			    editButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(DashboardActivity.this, EditActivity.class);
						Bundle b = new Bundle();
						TextView titleView = (TextView) ((View)v.getParent()).findViewById(R.id.titleHeader);
						String gameTitle = titleView.getText().toString();
						b.putInt("EDIT_GAME_ID", (int) GameObject.GetGameByName(games, gameTitle).GetId());
						intent.putExtras(b);
						startActivityForResult(intent, 0);
					}
				});
			    convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				if(holder == null) {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				    convertView = inflater.inflate(R.layout.scoreboard, parent, false);
				    holder = new ViewHolder();
				    holder.gameNameTextView = (TextView) convertView.findViewById(R.id.titleHeader);
				    holder.teamListView = (LinearLayout) convertView.findViewById(R.id.scoreboardTeamList);
				    ImageButton editButton = (ImageButton) convertView.findViewById(R.id.editScoreboard);
				    editButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(DashboardActivity.this, EditActivity.class);
							Bundle b = new Bundle();
							TextView titleView = (TextView) ((View)v.getParent()).findViewById(R.id.titleHeader);
							String gameTitle = titleView.getText().toString();
							b.putInt("EDIT_GAME_ID", (int) GameObject.GetGameByName(games, gameTitle).GetId());
							intent.putExtras(b);
							startActivityForResult(intent, 0);
						}
					});

				    holder.gameNameTextView.setText(games.get(position).Title());
				    convertView.setTag(holder);
				}
			}

			GameObject game = this.games.get(position);
			if(game != null) {
			    holder.gameNameTextView.setText(game.Title());
				//populate teams and scores
			    holder.teamListView.removeAllViews();
			    boolean first = true;
			    Collections.sort(game.teams);
			    for(TeamObject team : game.teams) {
			    	holder.teamListView.addView(CreateRelativeLayoutForTeam(team, first));
			    	first = false;
			    }
			}
		    return convertView;
		}
	}
}
