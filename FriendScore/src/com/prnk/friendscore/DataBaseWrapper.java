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
	//TODO: Properly define these, just creating a few for starters
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
		db.execSQL(teamsPlayersCreate);
		db.execSQL(teamsGamesCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + gamesTable);
        db.execSQL("DROP TABLE IF EXISTS " + playersTable);
        db.execSQL("DROP TABLE IF EXISTS " + teamsPlayersTable);
        db.execSQL("DROP TABLE IF EXISTS " + teamsGamesTable);

        // create new tables
        onCreate(db);
	}
	
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
	
	//This function creates a Team/Game relation in the teamsGamesTable
	public long createTeamGame(GameObject g,TeamObject t){
		SQLiteDatabase db = this.getWritableDatabase();
		 
	    ContentValues values = new ContentValues();
	    values.put(keyTeamId, t.GetId());
	    values.put(keyGameId, g.GetId());
	    values.put(keyScoreId, t.getScoreObject().GetId());
	    values.put(keyTeamName, t.Name());
	 
	    // insert row
	    long identifier = db.insert(teamsGamesTable, null, values);
	 
	    //TODO: here insert the Players and score into appropriate tables
	    return identifier;
	}

}