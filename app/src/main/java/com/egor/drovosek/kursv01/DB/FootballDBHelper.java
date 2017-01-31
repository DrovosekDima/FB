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
            db.execSQL(CREATE_TABLE_TEAMS);
            db.execSQL(CREATE_TABLE_MATCHES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            if (oldVersion < newVersion)
            {
                this.DeleteTables();
                onCreate(db);
            }
        }

        public void DeleteTables()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MATCHES);
            db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TEAMS);
            db.close();
        }

        public void ClearTables()
        {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CREATE_TABLE_MATCHES, null, null);
            db.delete(CREATE_TABLE_TEAMS, null, null);
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

    }