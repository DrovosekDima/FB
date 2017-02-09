package com.egor.drovosek.kursv01.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.graphics.Bitmap;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.ByteArrayOutputStream;
import android.graphics.BitmapFactory;
import android.util.Log;

import static com.egor.drovosek.kursv01.DB.Schema.*;

/**
 * Created by Drovosek on 31/01/2017.
 */

public class FootballDBHelper extends SQLiteOpenHelper
{
        Context _context;
        private String TRACE = "FootballDBHelper";


        public FootballDBHelper(Context context)
        {
            super(context, Schema.DATABASE_NAME, null, Schema.DATABASE_VERSION);
            _context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            //db.execSQL(CREATE_TABLE_TEAMS);
            db.execSQL(CREATE_TABLE_MATCHES);
            db.execSQL(CREATE_TABLE_PLAYERS);
            db.execSQL(CREATE_TABLE_GOALS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            if (oldVersion < newVersion)
            {
                //this.DeleteTables();
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
                //db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYERS);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
                onCreate(db);
            }
        }

        public void DeleteTables()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCHES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
            db.close();
        }

        public void ClearTables()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_MATCHES, null, null);
            db.delete(TABLE_TEAMS, null, null);
            db.close();
        }

        public void ClearTable(String tableName)
        {
             SQLiteDatabase db = this.getWritableDatabase();
             db.delete(tableName, null, null);
             db.close();
        }

        /*---------------------------------------------
          возвращает курсор со всеми коммандами за
          определенный год
          "select * from teams where season=inSeason"
         ---------------------------------------------*/
        public Cursor getAllTeams(int season) throws SQLException
        {
            SQLiteDatabase db = this.getReadableDatabase();

            String selectQuery = "SELECT  * FROM " + TABLE_TEAMS +
                                 " WHERE " + TEAMS_SEASON + "=" + String.valueOf(season);

            Cursor mCursor = db.rawQuery(selectQuery, null);

            return mCursor;
        }

          /*---------------------------------------------
        возвращает курсор со всеми матчами за определенный год

          SELECT m.round, HOME.title, GUEST.title, m.score_home, m.score_guest, m.datem, m.place
          FROM matches AS m
          JOIN teams AS HOME ON m.home_team_id=HOME.T_ID
          JOIN teams AS GUEST ON m.guest_team_id=GUEST.T_ID
          where m.season=2016;
        ---------------------------------------------*/
        public Cursor getMatchesSeason(int season) throws SQLException
        {
            SQLiteDatabase db = this.getReadableDatabase();

            /*String selectQuery = "SELECT m.round, HOME.title AS home_title, GUEST.title AS guest_title, m.score_home, m.score_guest, m.datem, m.location "
                    + "FROM "
                    + TABLE_MATCHES + " AS m "
                    + " JOIN " + TABLE_TEAMS + " AS HOME ON m." + MATCHES_HOME_TEAM_ID + "=HOME." + TEAMS_M_ID
                    + " JOIN " + TABLE_TEAMS + " AS GUEST ON m." + MATCHES_HOME_TEAM_ID + "=GUEST." + TEAMS_M_ID
                    + " WHERE m." + MATCHES_SEASON + "=" + String.valueOf(season);*/

            String selectQuery = "SELECT m.round, HOME.title AS home_title, GUEST.title AS guest_title, m.score_home, m.score_guest, m.datem, m.location FROM matches AS m JOIN teams AS HOME ON m.home_team_id=HOME.T_ID JOIN teams AS GUEST ON m.guest_team_id=GUEST.T_ID where m.season="+ String.valueOf(season)+";";

            Cursor mCursor = db.rawQuery(selectQuery, null);

         return mCursor;

        }

    /*---------------------------------------------
  возвращает курсор со всеми матчами за определенный год и тур

    SELECT m.round, HOME.title, GUEST.title, m.score_home, m.score_guest, m.datem, m.place
    FROM matches AS m
    JOIN teams AS HOME ON m.home_team_id=HOME.T_ID
    JOIN teams AS GUEST ON m.guest_team_id=GUEST.T_ID
    where m.season=2016 AND m.round=1;
  ---------------------------------------------*/
    public Cursor getMatchesSeasonRound(int season, int round) throws SQLException
    {
        SQLiteDatabase db = this.getReadableDatabase();

            /*String selectQuery = "SELECT m.round, HOME.title AS home_title, GUEST.title AS guest_title, m.score_home, m.score_guest, m.datem, m.location "
                    + "FROM "
                    + TABLE_MATCHES + " AS m "
                    + " JOIN " + TABLE_TEAMS + " AS HOME ON m." + MATCHES_HOME_TEAM_ID + "=HOME." + TEAMS_M_ID
                    + " JOIN " + TABLE_TEAMS + " AS GUEST ON m." + MATCHES_HOME_TEAM_ID + "=GUEST." + TEAMS_M_ID
                    + " WHERE m." + MATCHES_SEASON + "=" + String.valueOf(season);*/

        String selectQuery = "SELECT m.round, HOME.title AS home_title, GUEST.title AS guest_title, m.score_home, m.score_guest, m.datem, m.location FROM matches AS m JOIN teams AS HOME ON m.home_team_id=HOME.T_ID JOIN teams AS GUEST ON m.guest_team_id=GUEST.T_ID where m.season="+  String.valueOf(season) +" AND m.round=" + String.valueOf(round)+";";

        Cursor mCursor = db.rawQuery(selectQuery, null);

        return mCursor;

    }

    public Cursor getBestPlayers(int season) throws SQLException
    {
        SQLiteDatabase db = this.getReadableDatabase();

        /*
            SELECT p.first_name, p.second_name, t.title AS teamName, COUNT(g.match_id) AS numberOfGoals
            FROM players AS p
            JOIN goals AS g ON p.P_ID=g.player_id
            JOIN matches AS m ON g.match_id=m.M_ID AND m.season=2016
            JOIN teams AS t ON p.team_id=t.T_ID
            GROUP BY p.second_name
            ORDER BY numberOfGoals;
         */

        String selectQuery =
                "SELECT p.first_name AS first_name, p.second_name AS second_name, t.title AS teamName, COUNT(g.match_id) AS numberOfGoals " +
                " FROM players AS p " +
                " JOIN goals AS g ON p.P_ID=g.player_id " +
                " JOIN matches AS m ON g.match_id=m.M_ID AND m.season=2016 " +
                " JOIN teams AS t ON p.team_id=t.T_ID " +
                " GROUP BY p.second_name " +
                " ORDER BY numberOfGoals DESC;";

        Cursor mCursor = db.rawQuery(selectQuery, null);

        return mCursor;

    }

        /*---------------------------------------------
        возвращает уникальное ID комманды
         ---------------------------------------------*/
        public int getTeamID(String inTeamName) throws SQLException
        {
            SQLiteDatabase db = this.getReadableDatabase();

            String selectQuery = "SELECT  " + TEAMS_M_ID + " FROM " + TABLE_TEAMS +
                    " WHERE " + TEAMS_TITLE + "='" + inTeamName + "'";

            Cursor mCursor = db.rawQuery(selectQuery, null);
            if (mCursor !=null) {
                mCursor.moveToFirst();
                return mCursor.getInt(mCursor.getColumnIndex(Schema.TEAMS_M_ID));
            }
            else
               return -1;
        }

        /*-------------------------------------------------------
            возвращает уникальное ID игрока по имени и фамилии
        -------------------------------------------------------*/
    public int getPlayerID(String inFirstName, String inSecondName) throws SQLException
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  " + PLAYERS_P_ID + " FROM " + TABLE_PLAYERS +
                " WHERE " + PLAYERS_FIRST_NAME + "='" + inFirstName + "'" +
                " AND " + PLAYERS_SECOND_NAME + "='" + inSecondName + "'";

        Cursor mCursor = db.rawQuery(selectQuery, null);
        if (mCursor !=null) {
            mCursor.moveToFirst();
            if(mCursor.getCount()>0) {
                int colIndex = mCursor.getColumnIndex(PLAYERS_P_ID);
                return mCursor.getInt(colIndex);
            }
            else
                return -1;
        }
        else
            return -1;
    }

    /*-------------------------------------------------------
            возвращает уникальное ID матча по ID домашней комманды
            и дате
        -------------------------------------------------------*/
    public int getMatchID(int inHomeTeamID, String inDateAndTime) throws SQLException
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  " + MATCHES_M_ID + " FROM " + TABLE_MATCHES +
                " WHERE " + MATCHES_HOME_TEAM_ID + "=" + inHomeTeamID +
                " AND " + MATCHES_DATEM + "='" + inDateAndTime + "'";

        Cursor mCursor = db.rawQuery(selectQuery, null);
        if (mCursor !=null) {
            mCursor.moveToFirst();
            return mCursor.getInt(mCursor.getColumnIndex(Schema.MATCHES_M_ID));
        }
        else
            return -1;
    }

    /*--------------------------------------------------------------------------------------
        Возвращает количество матчей в определенном туре данного сезона
       SELECT COUNT(round) as matchesinround FROM matches WHERE season=2016 and round=1;
     -------------------------------------------------------------------------------------*/
        public int getCountRoundsInSeason(int season, int round) throws SQLException
        {
            SQLiteDatabase db = this.getReadableDatabase();

            /*String selectQuery = "SELECT  " + TEAMS_M_ID + " FROM " + TABLE_TEAMS +
                    " WHERE " + TEAMS_TITLE + "='" + inTeamName + "'";*/
            String selectQuery = "SELECT COUNT(round) as numberofrounds FROM matches WHERE season="+String.valueOf(season)+" and round="+String.valueOf(round)+";";

            Cursor mCursor = db.rawQuery(selectQuery, null);
            if (mCursor !=null&& mCursor.getCount()>1) {
                mCursor.moveToFirst();
                return mCursor.getInt(mCursor.getColumnIndex("numberofrounds"));
            }
            else
                return 0;
        }

        public void addTeam(ContentValues _teamValue)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            long rowID = db.insert(TABLE_TEAMS, null, _teamValue);
            if (rowID < 0)
                Log.v(TRACE, "addTeam FAILED");
        }

        public ContentValues createTeamValue(  String inTitle,
                                            String inCity,
                                            byte[] inLogo,
                                            String inPath,
                                            int inSeason )
        {
            ContentValues teamValue = new ContentValues();
            teamValue.put(TEAMS_TITLE, inTitle);
            teamValue.put(TEAMS_CITY, inCity);
            teamValue.put(TEAMS_EMBLEM, inLogo);
            teamValue.put(TEAMS_PATH, inPath);
            teamValue.put(TEAMS_SEASON, inSeason);
            teamValue.put(TEAMS_WIN, 0);
            teamValue.put(TEAMS_DRAW, 0);
            teamValue.put(TEAMS_LOST, 0);

            return teamValue;
        }
           /* как конвертировать JPEG to BLOB
            Bitmap image = BitmapFactory.decodeResource(_context.getResources(), R.drawable.pushups);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer=out.toByteArray();
           */

        public ContentValues createMatchValue( String inHomeTeam,
                                           String inGuestTeam,
                                           String inScoreHome,
                                           String inScoreGuest,
                                           String inSeason,
                                           String inRound,
                                           String inDateAndTime,
                                           String inLocation,
                                           String inStatus)
        {

        ContentValues teamValue = new ContentValues();

        teamValue.put(MATCHES_HOME_TEAM_ID, getTeamID(inHomeTeam));
        teamValue.put(MATCHES_GUEST_TEAM_ID, getTeamID(inGuestTeam));
        teamValue.put(MATCHES_SCORE_HOME, Integer.valueOf(inScoreHome));
        teamValue.put(MATCHES_SCORE_GUEST, Integer.valueOf(inScoreGuest));
        teamValue.put(MATCHES_ROUND, Integer.valueOf(inRound));
        teamValue.put(MATCHES_SEASON, Integer.valueOf(inSeason));


        /*Date dateAndTime = new Date();
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm"); //01.04.2016	18:30
        try {
            dateAndTime = df.parse(inDateAndTime);
        }
        catch(ParseException pe) {
            pe.printStackTrace();
        }*/

        teamValue.put(MATCHES_DATEM, inDateAndTime); //need to convert to Date before add to table

        teamValue.put(MATCHES_LOCATION, inLocation);
        teamValue.put(MATCHES_STATUS, inStatus);

        return teamValue;
    }

        public void addMatch(ContentValues _matchValue)
        {
            SQLiteDatabase db = this.getWritableDatabase();

            long rowID = db.insert(TABLE_MATCHES, null, _matchValue);
            if (rowID < 0)
                Log.v(TRACE, "addMatch FAILED");
        }

        public void addPlayer(ContentValues _playerValue)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            long rowID = db.insert(TABLE_PLAYERS, null, _playerValue);
            if (rowID < 0)
                Log.v(TRACE, "addPlayer FAILED");
        }

        public ContentValues createPlayerValue(  String inFirstName,
                                                 String inSecond,
                                                 int    inHeight,
                                                 int    inWeight,
                                                 String country,
                                                 String birth,
                                                 byte[] inPhoto,
                                                 int    inTeamId)
        {
            ContentValues teamValue = new ContentValues();
            teamValue.put(PLAYERS_FIRST_NAME, inFirstName);
            teamValue.put(PLAYERS_SECOND_NAME, inSecond);
            teamValue.put(PLAYERS_HEIGHT, inHeight);
            teamValue.put(PLAYERS_WEIGHT, inWeight);
            teamValue.put(PLAYERS_COUNTRY, country);
            teamValue.put(PLAYERS_BIRTH, birth);
            teamValue.put(PLAYERS_PHOTO, inPhoto);
            teamValue.put(PLAYERS_TEAM_ID, inTeamId);

            return teamValue;
        }


        public void addGoal(ContentValues _goalValue)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            long rowID = db.insert(TABLE_GOALS, null, _goalValue);
            if (rowID < 0)
                Log.v(TRACE, "addGoal FAILED");
        }

        public ContentValues createGoalValue(  String inMinute,
                                           int    inPlayerId,
                                           int    inMatchId,
                                           String type)
        {
            ContentValues teamValue = new ContentValues();
            teamValue.put(GOALS_MINUTE, inMinute);
            teamValue.put(GOALS_PLAYER_ID, inPlayerId);
            teamValue.put(GOALS_MATCH_ID, inMatchId);
            teamValue.put(GOALS_TYPE, type);


            return teamValue;
        }
    }