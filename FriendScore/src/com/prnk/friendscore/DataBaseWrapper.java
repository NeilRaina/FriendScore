package com.prnk.friendscore;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prnk.friendscore.*;

public class DataBaseWrapper extends SQLiteOpenHelper {

	//Table Names
	public static final String gamesTable = "Games";
	public static final String playersTable = "Players";
	public static final String scoresTable = "Scores";
	public static final String teamsPlayersTable = "TeamsPlayers";
	public static final String teamsGamesTable = "TeamsGames";
	
	//Common Column Names
	public static final String keyId = "Id";
	public static final String keyTeamId = "TeamId";
	public static final String keyGameId = "GameId";
	public static final String keyPlayersId = "PlayersId";
	public static final String keyScoreId = "ScoreId";
	
	//Games Table - Column names
	public static final String keyGameName = "GameName";
	
	//Players Table - Column names
	public static final String keyFirstName = "FirstName";
	public static final String keyLastName = "LastName";
	
	//Scores Table - Column names
	//TODO: Properly define these later, just putting the int score in kills for now
	public static final String keyKills = "Kills";
	public static final String keyDeaths = "Deaths";
	public static final String keyGoalsFor = "GoalsFor";
	public static final String keyGoalsAgainst = "GoalsAgainst";

	//Teams-Games Table - Column names
	public static final String keyTeamName = "TeamName";
	
	//Teams-Players Table - Column names
	//nothing for now

	private static final int databaseVersion = 1;

	// create table statements
	private static final String gamesCreate = "create table " + gamesTable
			+ "(" + keyId + " integer primary key autoincrement, "
			+ keyGameName + " text not null);";
	
	private static final String playersCreate = "create table " + playersTable
			+ "(" + keyId + " integer primary key autoincrement, "
			+ keyFirstName + " text not null, " + keyLastName + " text not null)";
	
	private static final String scoresCreate = "create table " + scoresTable
			+ "(" + keyId + " integer primary key autoincrement, "
			+ keyKills + " integer)";
	
	private static final String teamsPlayersCreate = "create table " + teamsPlayersTable
			+ "(" + keyId + " integer primary key autoincrement, "
			+ keyTeamId + " integer, " + keyPlayersId + " integer)";
	
	private static final String teamsGamesCreate = "create table " + teamsGamesTable
			+ "(" + keyId + " integer primary key autoincrement, "
			+ keyTeamId + " integer, " + keyGameId + " integer, " + keyTeamName + " text"  +")";
	
	public static final String databaseName = "Scoreboard Manager";

	public DataBaseWrapper(Context context) {
		super(context, databaseName, null, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(gamesCreate);
		db.execSQL(playersCreate);
		db.execSQL(scoresCreate);
		db.execSQL(teamsPlayersCreate);
		db.execSQL(teamsGamesCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + gamesTable);
        db.execSQL("DROP TABLE IF EXISTS " + playersTable);
        db.execSQL("DROP TABLE IF EXISTS " + scoresTable);
        db.execSQL("DROP TABLE IF EXISTS " + teamsPlayersTable);
        db.execSQL("DROP TABLE IF EXISTS " + teamsGamesTable);

        // create new tables
        onCreate(db);
	}
	
	/*
	 * Implementing CRUD operations:
	 *  (C)reate
	 *  (R)ead
	 *  (U)pdate
	 *  (D)elete
	 *  Starting with the create operations for every table
	 */
	
	//This function creates a game item in the Games table
	public long createGame(GameObject g){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(keyGameName, g.Title());
	 
	    // insert row
	    long identifier = db.insert(gamesTable, null, values);
	 
	    //insert the teams
	    for (TeamObject t : g.teams) {
	        createTeamGame(g,t);
	    }
	 
	    return identifier;
	}
	
	//This function creates a team/game relation in the teamsGamesTable
	public long createTeamGame(GameObject g,TeamObject t){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(keyTeamId, t.GetId());
	    values.put(keyGameId, g.GetId());
	    values.put(keyScoreId, t.getScoreObject().GetId());
	    values.put(keyTeamName, t.Name());
	 
	    // insert row
	    long identifier = db.insert(teamsGamesTable, null, values);
	 
	    t.getScoreObject().SetID(createScore(t.getScoreObject()));
	    
	    for (PlayerObject p: t.players){
	    	createTeamPlayer(t,p);
	    	p.SetId(createPlayer(p));
	    }
	    
	    return identifier;
	}
	
	//This function creates a score in the Scores table
	public long createScore(ScoreObject s){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(keyKills, s.Score()); 
	    
	    // insert row
	    long identifier = db.insert(scoresTable, null, values);

	    return identifier;
	}
	
	//This function creates a team/player relation in the teamsPlayersTable 
	public long createTeamPlayer(TeamObject t, PlayerObject p){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(keyTeamId, t.GetId()); 
	    values.put(keyPlayersId, p.GetId());
	 
	    // insert row
	    long identifier = db.insert(teamsPlayersTable, null, values);

	    return identifier;
	}
	
	//This function creates a player in the Players table
	public long createPlayer(PlayerObject p){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(keyFirstName, p.GetFirstName()); 
	    values.put(keyLastName, p.GetLastName());
	 
	    // insert row
	    long identifier = db.insert(playersTable, null, values);

	    return identifier;
	}
	
	//This is the start of the READ operations
	


}