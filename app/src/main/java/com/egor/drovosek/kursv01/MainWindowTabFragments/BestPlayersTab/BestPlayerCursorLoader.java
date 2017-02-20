package com.egor.drovosek.kursv01.MainWindowTabFragments.BestPlayersTab;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.egor.drovosek.kursv01.DB.FootballDBHelper;
import com.egor.drovosek.kursv01.MainActivity;

/**
 * Created by Drovosek on 20/02/2017.
 */

public class BestPlayerCursorLoader extends CursorLoader {

    FootballDBHelper db;
    Context mContext;

    public BestPlayerCursorLoader(Context context) {
        super(context);
        mContext = context;
        db = new FootballDBHelper(mContext);
    }

    @Override
    public Cursor loadInBackground() {
        Log.i("BestPlayer", "loadInBackground - enter");
        Cursor cursor = db.getBestPlayers(MainActivity.gdSeason);
        Log.i("BestPlayer", "loadInBackground - Exit");
        return cursor;
    }

}