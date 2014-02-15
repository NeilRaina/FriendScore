package com.prnk.friendscore;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
			+ keyTeamId + " integer, " + keyGameId + " integer, " + keyTeamName + " text," + keyScoreId + " integer" +")";
	
	public static final String databaseName = "Scoreboard Manager";

	private Context context;
	
	public DataBaseWrapper(Context context) {
		super(context, databaseName, null, databaseVersion);
		this.context = context;
	}
	
	public void DeleteDatabase() {
		context.deleteDatabase(databaseName);
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
	    
	    g.SetId(identifier);
	 
	    //insert the teams
	    for (TeamObject t : g.teams) {
	        createTeamGame(g,t);
	    }
	 
	    return identifier;
	}
	
	//This function creates a team/game relation in the teamsGamesTable
	public long createTeamGame(GameObject g,TeamObject t){
		SQLiteDatabase db = this.getWritableDatabase();
		 
		 t.GetScoreObject().SetId(createScore(t.GetScoreObject()));
		
	    ContentValues values = new ContentValues();
	    values.put(keyGameId, g.GetId());
	    values.put(keyScoreId, t.GetScoreObject().GetId());
	    values.put(keyTeamName, t.Name());
	 
	    // insert row
	    long identifier = db.insert(teamsGamesTable, null, values);
	 
	    t.SetId(identifier);
	    
	    for (PlayerObject p: t.players) {
	    	p.SetId(createPlayer(p));
	    	createTeamPlayer(t,p);
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
	
	/*
	 * This is the start of the READ operations
	 */
	
	//This function gets all game objects from the Games table
	public ArrayList<GameObject> getAllGameObjects() {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<GameObject> games = new ArrayList<GameObject>();
		 
	    String selectQuery = "SELECT  * FROM " + gamesTable;
	 
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(selectQuery, null);

	    if (c.moveToFirst()) {
	        do {
	    	    GameObject game = new GameObject();
	    	    game.SetId(c.getInt(c.getColumnIndex(keyId)));
	    	    game.SetTitle(c.getString(c.getColumnIndex(keyGameName)));
	    	    game.SetTeams(getTeamsList(game.GetId()));
	            games.add(game);
	        } while (c.moveToNext());
	    }

	    return games;
	}
	
	//This function gets a single game object from the Games table
	public GameObject getGameObject(long gameId) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT  * FROM " + gamesTable + " WHERE "
	            + keyId + " = " + gameId;
	 
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	 
	    GameObject game = new GameObject();
	    game.SetId(c.getInt(c.getColumnIndex(keyId)));
	    game.SetTitle(c.getString(c.getColumnIndex(keyGameName)));
	    game.SetTeams(getTeamsList(game.GetId()));
	    
	    return game;
	}
	
	//This function gets all the teams from a certain game from teamGamesTable
	public ArrayList<TeamObject> getTeamsList(long gameId) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    ArrayList<TeamObject> teams = new ArrayList<TeamObject>();
	 
	    String teamsQuery = "SELECT  * FROM " + teamsGamesTable + " WHERE "
	            + keyGameId + " = " + gameId;
	 
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(teamsQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            TeamObject team = new TeamObject();
	            team.SetId(c.getInt((c.getColumnIndex(keyId))));
	            team.SetName(c.getString(c.getColumnIndex(keyTeamName)));
	            team.SetScoreObject(getScoreObject(c.getInt(c.getColumnIndex(keyScoreId))));
	            team.SetPlayers(getPlayersList(team.GetId()));
	       
	            // adding to teams list
	            teams.add(team);
	        } while (c.moveToNext());
	    }
	 
	    return teams;
	}
	
	//This function gets all the players from a certain team from teamPlayersTable
	public ArrayList<PlayerObject> getPlayersList(long teamID) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    ArrayList<PlayerObject> players = new ArrayList<PlayerObject>();
	 
        String playersQuery = "SELECT  * FROM " + teamsPlayersTable + " WHERE "
	            + keyTeamId + " = " + teamID;
        
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(playersQuery, null);
	 
	    // looping through all rows and adding to list
	    if (c.moveToFirst()) {
	        do {
	            PlayerObject player = new PlayerObject();
	            player = getPlayerObject(c.getInt((c.getColumnIndex(keyPlayersId))));
	            
	            // adding to teams list
	            players.add(player);
	        } while (c.moveToNext());
	    }
	 
	    return players;
	}
		
	
	//This function gets a single score object from the Scores table
	public ScoreObject getScoreObject(long scoreId) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT  * FROM " + scoresTable + " WHERE "
	            + keyId + " = " + scoreId;
	 
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	 
	    ScoreObject score = new ScoreObject();
	    score.SetId(c.getInt(c.getColumnIndex(keyId)));
	    score.SetScore(c.getInt(c.getColumnIndex(keyKills)));
	 
	    return score;
	}
	
	//This function gets a single player object from the Players table
	public PlayerObject getPlayerObject(long playerId) {
	    SQLiteDatabase db = this.getReadableDatabase();
	 
	    String selectQuery = "SELECT  * FROM " + playersTable + " WHERE "
	            + keyId + " = " + playerId;
	 
	    //Log.e(LOG, selectQuery);
	 
	    Cursor c = db.rawQuery(selectQuery, null);
	 
	    if (c != null)
	        c.moveToFirst();
	 
	    PlayerObject player = new PlayerObject();
	    player.SetId(c.getInt(c.getColumnIndex(keyId)));
	    player.SetName(c.getString(c.getColumnIndex(keyFirstName)), c.getString(c.getColumnIndex(keyLastName)));	    
	 
	    return player;
	}

	/*
	 * This is the start of the UPDATE operations
	 */
	//Update a row in GameTable corresponding to game name
	public int updateGameName(GameObject g) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(keyGameName, g.Title());
	 
	    // updating row
	    return db.update(gamesTable, values, keyId + " = ?",
	            new String[] { String.valueOf(g.GetId()) });
	}
	
	//Update a row in teamGameTable corresponding to team name
	public int updateTeamName(TeamObject t) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(keyTeamName, t.Name());
	 
	    // updating row
	    return db.update(teamsGamesTable, values, keyId + " = ?",
	            new String[] { String.valueOf(t.GetId()) });
	}
		
	//Update a row in scoresTable
	public int updateScore(ScoreObject s) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(keyKills, s.Score());
	 
	    // updating row
	    return db.update(scoresTable, values, keyId + " = ?",
	            new String[] { String.valueOf(s.GetId()) });
	}
		
	
	/*
	 * This is the start of the DELETE operations
	 */
	//Delete a team -> removes row(s) in teamGamesTable, teamPlayerTable, playerTable
	//NOTE: Assumption made that each team's players are only on that team
	public void deleteTeam(long teamID) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(teamsGamesTable, keyTeamId + " = ?",
	            new String[] { String.valueOf(teamID) });
	    
	    String playersQuery = "SELECT  * FROM " + teamsPlayersTable + " WHERE "
	            + keyTeamId + " = " + teamID;
        	 
	    Cursor c = db.rawQuery(playersQuery, null);
	 
	    // looping through all rows and deleting player
	    if (c.moveToFirst()) {
	        do {
	        	this.deletePlayer(c.getLong((c.getColumnIndex(keyPlayersId))));
	        } while (c.moveToNext());
	    }
	    
	    db.delete(teamsPlayersTable, keyTeamId + " = ?",
	            new String[] { String.valueOf(teamID) });
	}
	
	//Delete a row in the scoresTable
	public void deleteScore(long scoreID) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(scoresTable, keyId + " = ?",
	            new String[] { String.valueOf(scoreID) });
	}
	
	//Delete a row in the playersTable
	public void deletePlayer(long playerID) {
	    SQLiteDatabase db = this.getWritableDatabase();
	    db.delete(playersTable, keyId + " = ?",
	            new String[] { String.valueOf(playerID) });
	}


}